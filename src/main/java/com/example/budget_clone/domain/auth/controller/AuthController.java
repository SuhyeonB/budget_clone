package com.example.budget_clone.domain.auth.controller;

import com.example.budget_clone.domain.auth.dto.request.LoginRequest;
import com.example.budget_clone.domain.auth.dto.request.SignupRequest;
import com.example.budget_clone.domain.auth.dto.response.TokensResponse;
import com.example.budget_clone.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest dto) {
        authService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponse> login(@RequestBody LoginRequest dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

}
