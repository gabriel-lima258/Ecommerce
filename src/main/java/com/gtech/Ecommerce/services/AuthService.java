package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.email.EmailDTO;
import com.gtech.Ecommerce.dto.user.NewPasswordDTO;
import com.gtech.Ecommerce.dto.user.UserDTO;
import com.gtech.Ecommerce.entities.PasswordRecover;
import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.repositories.PasswordRecoverRepository;
import com.gtech.Ecommerce.repositories.UserRepository;
import com.gtech.Ecommerce.services.exceptions.ForbiddenException;
import com.gtech.Ecommerce.services.exceptions.ResourceNotFoundException;
import com.gtech.Ecommerce.utils.CustomUserUtil;
import com.gtech.Ecommerce.utils.EmailTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserService service;

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    // valor do front com o link
    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserUtil customUserUtil;

    @Autowired
    private EmailTemplateUtil emailTemplateUtil;

    // validação se o user é ele mesmo ou se é ADMIN
    public void validateSelfOrAdmin(Long userId) {
        User me = authenticated();

        if (me.hasRole("ROLE_ADMIN")) return;
        if (!me.getId().equals(userId)) throw new ForbiddenException("Access Denied. It should self or admin");
    }

    @Transactional
    public void createRecoverToken(EmailDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            throw new ResourceNotFoundException("Falha ao enviar o email");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(dto.getEmail());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60));
        entity = passwordRecoverRepository.save(entity);

        String htmlBody = emailTemplateUtil.buildRecoveryEmailHtml(user.getName(), recoverUri + token, tokenMinutes);

        emailService.sendHtmlEmail(dto.getEmail(), "Recuperação de Senha - Ecommerce", htmlBody);
    }

    @Transactional
    public void saveNewPassword(NewPasswordDTO dto) {
        List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(dto.getToken(), Instant.now());
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Token inválido");
        }

        User user = userRepository.findByEmail(result.getFirst().getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = userRepository.save(user);
    }

    protected User authenticated() {
        try {
            String username = customUserUtil.getLoggedUsername();
            return userRepository.findByEmail(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO findProfile() {
        User user = authenticated();
        return new UserDTO(user);
    }
}