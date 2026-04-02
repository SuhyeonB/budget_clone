package com.example.budget_clone.domain.user.service;

import com.example.budget_clone.domain.user.dto.response.UserResponse;
import com.example.budget_clone.domain.user.dto.resquest.UpdateNickname;
import com.example.budget_clone.domain.user.dto.resquest.UpdatePassword;
import com.example.budget_clone.domain.user.entity.User;
import com.example.budget_clone.domain.user.repository.UserRepository;
import com.example.budget_clone.global.exception.ForbiddenException;
import com.example.budget_clone.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getMe(Long userId) {
        User user = findUser(userId);

        return UserResponse.from(user);
    }

    @Transactional
    public void updatePassword(Long userId, UpdatePassword dto) {
        User user = findUser(userId);

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new ForbiddenException("비밀번호가 올바르지 않습니다.");
        }

        String encoded = passwordEncoder.encode(dto.getNewPassword());

        user.updatePassword(encoded);
    }

    @Transactional
    public UserResponse updateNickname(Long userId, UpdateNickname dto) {
        User user = findUser(userId);

        user.updateNickname(dto.getNickname());

        return UserResponse.from(user);
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 사용자는 존재하지 않습니다."));
    }
}
