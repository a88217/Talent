package com.circule.talent.utils;

import com.circule.talent.model.User;
import com.circule.talent.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        var email = authentication.getName();
        return userRepository.findByEmail(email).get();
    }

    public boolean isCurrentUser(long userId) {
        var userEmail = userRepository.findById(userId).get().getEmail();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return userEmail.equals(authentication.getName());
    }

    public User getTestUser() {
        return  userRepository.findByEmail("muzalev.as@gmail.com")
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
