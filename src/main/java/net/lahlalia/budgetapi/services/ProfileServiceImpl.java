package net.lahlalia.budgetapi.services;

import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.ProfileResponse;
import net.lahlalia.budgetapi.dtos.UpdatePasswordRequest;
import net.lahlalia.budgetapi.entities.User;
import net.lahlalia.budgetapi.repositories.UserRepository;
import net.lahlalia.budgetapi.security.JwtService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// services/ProfileServiceImpl.java
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public ProfileResponse getProfile() {
        User user = getCurrentUser();
        return new ProfileResponse(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail()
        );
    }


    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        User user = getCurrentUser();
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}

