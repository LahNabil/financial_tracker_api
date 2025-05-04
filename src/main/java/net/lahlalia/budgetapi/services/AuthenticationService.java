package net.lahlalia.budgetapi.services;

import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.AuthenticationRequest;
import net.lahlalia.budgetapi.dtos.AuthenticationResponse;
import net.lahlalia.budgetapi.dtos.RegistrationRequest;
import net.lahlalia.budgetapi.entities.User;
import net.lahlalia.budgetapi.repositories.RoleRepository;
import net.lahlalia.budgetapi.repositories.UserRepository;
import net.lahlalia.budgetapi.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public void register(RegistrationRequest request) {
        var userRole= roleRepository.findByName("USER")
                .orElseThrow(()-> new IllegalStateException("ROLE USER NOT initialized"));
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("fullname", user.fullName());
        var jwt = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwt).build();
    }
}
