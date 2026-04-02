package com.example.budget_clone.domain.auth.service;

import com.example.budget_clone.domain.auth.dto.request.LoginRequest;
import com.example.budget_clone.domain.auth.dto.request.SignupRequest;
import com.example.budget_clone.domain.auth.dto.response.TokenResponse;
import com.example.budget_clone.domain.auth.dto.response.TokensResponse;
import com.example.budget_clone.domain.auth.entity.RefreshToken;
import com.example.budget_clone.domain.auth.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
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

        refreshTokenRepository.save(new RefreshToken(refreshToken, user.getId()));

        return new TokensResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse reissue (String refreshToken) {
        // 1. Refresh Token 서명 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new ForbiddenException("유효하지 않은 refresh token");
        }

        // 2. Redis에 저장된 값과 일치하는지 확인
        RefreshToken savedToken = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new ForbiddenException("이미 만료된 토큰입니다."));

        User user = userRepository.findById(savedToken.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

        // 3. Access Token 재발급
        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail());

        return new TokenResponse(accessToken);
    }
}

