package com.sample.poc.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sample.poc.application.dto.AuthResponse;
import com.sample.poc.application.dto.LoginRequest;
import com.sample.poc.application.dto.RegisterRequest;
import com.sample.poc.domain.model.User;
import com.sample.poc.domain.repository.UserRepository;
import com.sample.poc.infrastructure.security.JwtUtil;
import com.sample.poc.presentation.exception.EmailAlreadyExistsException;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenProvider;

    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email já em uso: " + registerRequest.getEmail());
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            String email = authentication.getName();
            String accessToken = jwtTokenProvider.generateAccessToken(email);

            Optional<User> user = userRepository.findByEmail(email);
            String nome = user.get().getName();
            return new AuthResponse(nome, accessToken, jwtTokenProvider.getJwtExpirationMs());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Email ou senha inválidos", e);
        }
    }
}
