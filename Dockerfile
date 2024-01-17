FROM openjdk:17-jdk-alpine AS build
RUN apk update && apk add findutils
COPY ./src/ /usr/src/app/src/
COPY ./build.gradle.kts /usr/src/app/build.gradle.kts
COPY ./gradle/ /usr/src/app/gradle/
COPY ./openapi/ /usr/src/app/openapi/
COPY ./gradlew /usr/src/app/gradlew
COPY ./settings.gradle.kts /usr/src/app/settings.gradle.kts
WORKDIR /usr/src/app
#COPY --chown=gradle:gradle . /home/gradle/src
#WORKDIR /home/gradle/src
RUN ./gradlew --no-daemon --info build -x check

FROM openjdk:17
EXPOSE 8080
RUN mkdir /app
COPY --from=build /usr/src/app/build/libs/*.jar /app/

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Duser.timezone=Europe/Moscow", "-jar","/app/highload.jar"]
