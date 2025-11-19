package com.gtech.Ecommerce.controllers;

import com.gtech.Ecommerce.dto.email.EmailDTO;
import com.gtech.Ecommerce.dto.user.NewPasswordDTO;
import com.gtech.Ecommerce.dto.user.UserDTO;
import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.repositories.UserRepository;
import com.gtech.Ecommerce.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private UserRepository repository;

    @PostMapping("/recover-token")
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO dto) {
        service.createRecoverToken(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/new-password")
    public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO dto) {
        service.saveNewPassword(dto);
        return ResponseEntity.noContent().build();
    }
}
