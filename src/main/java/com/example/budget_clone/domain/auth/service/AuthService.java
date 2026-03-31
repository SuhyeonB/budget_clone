package com.example.budget_clone.domain.auth.service;

import com.example.budget_clone.domain.auth.dto.request.LoginRequest;
import com.example.budget_clone.domain.auth.dto.request.SignupRequest;
import com.example.budget_clone.domain.auth.dto.response.TokensResponse;
import com.example.budget_clone.domain.user.entity.User;
import com.example.budget_clone.domain.user.repository.UserRepository;
import com.example.budget_clone.global.exception.BadRequestException;
import com.example.budget_clone.global.exception.ForbiddenException;
import com.example.budget_clone.global.exception.NotFoundException;
import com.example.budget_clone.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(SignupRequest dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("이미 가입된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User user = User.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .nickname(dto.getNickname())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public TokensResponse login(LoginRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ForbiddenException("잘못된 비밀번호입니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(user.getId(), user.getEmail());

        return new TokensResponse(accessToken, refreshToken);
    }
}

