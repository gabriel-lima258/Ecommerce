package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.user.UserDTO;
import com.gtech.Ecommerce.dto.user.UserInsertDTO;
import com.gtech.Ecommerce.dto.user.UserUpdateDTO;
import com.gtech.Ecommerce.entities.Role;
import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.factories.UserDetailsFactoryTest;
import com.gtech.Ecommerce.factories.UserFactoryTest;
import com.gtech.Ecommerce.projections.UserDetailsProjection;
import com.gtech.Ecommerce.repositories.RoleRepository;
import com.gtech.Ecommerce.repositories.UserRepository;
import com.gtech.Ecommerce.services.exceptions.DatabaseException;
import com.gtech.Ecommerce.services.exceptions.ForbiddenException;
import com.gtech.Ecommerce.services.exceptions.ResourceNotFoundException;
import com.gtech.Ecommerce.utils.CustomUserUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @InjectMocks
    private AuthService service;

    @Mock
    private CustomUserUtil userUtil;

    @Mock
    private UserRepository userRepository;

    private Long existingId, noExistingId;
    private String existingUsername, noExistingUsername;
    private User user, admin, selfClient, otherClient;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 121L;
        existingUsername = "admin@gmail.com";
        noExistingUsername = "alex@gmail.com";
        user = UserFactoryTest.createCustomUserClient(existingId, existingUsername);
        admin = UserFactoryTest.createUserAdmin();
        selfClient = UserFactoryTest.createCustomUserClient(3L, "Maria");
        otherClient = UserFactoryTest.createCustomUserClient(4L, "Eduardo");

        // authenticated
        Mockito.when(userRepository.findByEmail(existingUsername)).thenReturn(user);
        Mockito.when(userRepository.findByEmail(noExistingUsername)).thenReturn(null);

    }

    @Test
    public void authenticatedShouldReturnUserWhenUsernameExists() {
        Mockito.when(userUtil.getLoggedUsername()).thenReturn(existingUsername);
        User result = service.authenticated();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(existingUsername, result.getUsername());
    }

    @Test
    public void authenticatedShouldThrowUsernameNotFoundWhenUsernameDoesNotExist() {
        Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.authenticated();
        });
    }

    @Test
    public void getMeShouldReturnUserDTOWhenIsAuthenticated() {
        // usando spy para mock de service de classes internas
        AuthService spyService = Mockito.spy(service);
        Mockito.doReturn(user).when(spyService).authenticated();
        UserDTO result = spyService.findProfile();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUsername, result.getEmail());
    }

    @Test
    public void authenticatedShouldThrowUsernameNotFoundWhenIsNotAuthenticated() {
        AuthService spyService = Mockito.spy(service);
        Mockito.doThrow(UsernameNotFoundException.class).when(spyService).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDTO result = spyService.findProfile();
        });
    }

    @Test
    public void validateSelfOrAdminShouldReturnPermissionWhenIsAdmin() {
        AuthService spyService = Mockito.spy(service);
        Mockito.doReturn(admin).when(spyService).authenticated();

        Long userId = admin.getId();

        Assertions.assertDoesNotThrow(() -> {
            spyService.validateSelfOrAdmin(userId);
        });
    }

    @Test
    public void validateSelfOrAdminShouldReturnPermissionWhenIsSelfClient() {
        AuthService spyService = Mockito.spy(service);
        Mockito.doReturn(selfClient).when(spyService).authenticated();

        Long userId = selfClient.getId();

        Assertions.assertDoesNotThrow(() -> {
            spyService.validateSelfOrAdmin(userId);
        });
    }

    @Test
    public void validateSelfOrAdminShouldReturnForbiddenExceptionWhenIsNotAdminOrSelfClient() {
        // classe interna tem que chamar spy
        AuthService spyService = Mockito.spy(service);
        Mockito.doReturn(selfClient).when(spyService).authenticated();

        Long userId = otherClient.getId();

        Assertions.assertThrows(ForbiddenException.class, () -> {
            spyService.validateSelfOrAdmin(userId);
        });
    }

}
