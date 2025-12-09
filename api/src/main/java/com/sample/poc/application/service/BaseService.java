package com.sample.poc.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sample.poc.domain.model.User;
import com.sample.poc.domain.repository.UserRepository;
import com.sample.poc.presentation.exception.ResourceNotFoundException;
import com.sample.poc.presentation.exception.UnauthorizedException;

public class BaseService {

  @Autowired
  protected UserRepository userRepository;

  protected User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("Usuário não autenticado");
    }

    String email = authentication.getName();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email));
  }
}
