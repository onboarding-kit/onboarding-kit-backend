package com.api.onboardingkit.user.service;

import com.api.onboardingkit.user.entity.User;
import com.api.onboardingkit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User saveOrUpdate(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        return existingUser.map(u -> {
            u.updateProfile(user.getNickname(), user.getRole(), user.getDetailRole(), user.getExperience());
            return userRepository.save(u);
        }).orElseGet(() -> userRepository.save(user));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}