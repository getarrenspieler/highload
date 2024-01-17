package ru.gts.highload.web;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import ru.gts.highload.model.UserInfo;
import ru.gts.highload.security.service.TokenService;
import ru.gts.highload.service.UserService;
import ru.otus.highload.api.DefaultApi;
import ru.otus.highload.model.LoginPostRequest;
import ru.otus.highload.model.LoginResponse;
import ru.otus.highload.model.User;
import ru.otus.highload.model.UserRegisterPostRequest;
import ru.otus.highload.model.UserRegisterResponse;

@RestController
@SecurityRequirement(name = "auth")
@RequiredArgsConstructor
public class MainController implements DefaultApi {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;


    @Override
    public ResponseEntity<UserRegisterResponse> userRegisterPost(UserRegisterPostRequest userRegisterPostRequest) {
        UserInfo userInfo = UserInfo.builder()
                .firstName(userRegisterPostRequest.getFirstName())
                .secondName(userRegisterPostRequest.getSecondName())
                .birthdate(userRegisterPostRequest.getBirthdate())
                .city(userRegisterPostRequest.getCity())
                .biography(userRegisterPostRequest.getBiography())
                .password(passwordEncoder.encode(userRegisterPostRequest.getPassword()))
                .build();

        UserRegisterResponse response = new UserRegisterResponse();
        response.userId(userService.createUser(userInfo));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<LoginResponse> loginPost(LoginPostRequest loginPostRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginPostRequest.getId(),
                loginPostRequest.getPassword()
        ));

        String token = tokenService.generateToken(loginPostRequest.getId());
        return ResponseEntity.ok(new LoginResponse().token(token));
    }

    @Override
    public ResponseEntity<User> userGetIdGet(
//            @NotBlank(message = "id не должен быть пустым")
            String id) {
//        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        final UserInfo userInfo = userService.get(id);
        if (userInfo != null) {
            final User user = new User()
                    .id(userInfo.getId())
                    .city(userInfo.getCity())
                    .biography(userInfo.getBiography())
                    .firstName(userInfo.getFirstName())
                    .secondName(userInfo.getSecondName())
                    .birthdate(userInfo.getBirthdate());
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.notFound().build();
    }
}
