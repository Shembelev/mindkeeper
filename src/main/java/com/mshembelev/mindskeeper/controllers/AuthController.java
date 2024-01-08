package com.mshembelev.mindskeeper.controllers;

import com.mshembelev.mindskeeper.dto.auth.JwtAuthenticationResponse;
import com.mshembelev.mindskeeper.dto.auth.SignInRequest;
import com.mshembelev.mindskeeper.dto.auth.SignUpRequest;
import com.mshembelev.mindskeeper.dto.auth.UpdateResponse;
import com.mshembelev.mindskeeper.models.UserModel;
import com.mshembelev.mindskeeper.security.AuthenticationService;
import com.mshembelev.mindskeeper.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @Operation(summary = "Обновление пользователя")
    @PostMapping("/update")
    public UpdateResponse update(){
        return userService.update();
    }
}