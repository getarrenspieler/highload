plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.openapi.generator") version "7.2.0"
}

group = "ru.gts"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val generatedDir = "build/generated"

sourceSets {
    getByName("main") {
        java {
            srcDir("$projectDir/$generatedDir/src/main/java")
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.flywaydb:flyway-maven-plugin:8.5.4")
    implementation("org.flywaydb:flyway-core:8.5.4")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
    implementation("com.auth0:java-jwt:4.3.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:postgresql:1.19.3")
}

val generatedSourcesDir = "${layout.buildDirectory}/generated/api"

openApiGenerate {
    generatorName = "spring"
    inputSpec = "$projectDir/openapi/openapi.json"
    outputDir = "$projectDir/$generatedDir"
    apiPackage = "ru.otus.highload.api"
    invokerPackage = "ru.otus.highload.invoker"
    modelPackage = "ru.otus.highload.model"

    inlineSchemaNameMappings = mapOf(
        "_login_post_500_response" to "ApplicationError",
        "_login_post_200_response" to "LoginResponse",
        "_user_register_post_200_response" to "UserRegisterResponse"
    )

    configOptions = mapOf(
        "interfaceOnly" to "true",
        "useSpringBoot3" to "true",
        "useTags" to "true",
        "dateLibrary" to "java8",
        "openApiNullable" to "false"
    )


//    modelFilesConstrainedTo = listOf("User")
}




tasks {
    val openApiGenerate by getting

    val compileJava by getting {
        dependsOn(openApiGenerate)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
