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
import com.gtech.Ecommerce.services.exceptions.ResourceNotFoundException;
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
public class UserServiceTests {

    private void copyDTOFields(UserDTO target, UserDTO source) {
        try {
            java.lang.reflect.Field nameField = UserDTO.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(target, source.getName());
            
            java.lang.reflect.Field emailField = UserDTO.class.getDeclaredField("email");
            emailField.setAccessible(true);
            emailField.set(target, source.getEmail());
            
            java.lang.reflect.Field phoneField = UserDTO.class.getDeclaredField("phone");
            phoneField.setAccessible(true);
            phoneField.set(target, source.getPhone());
            
            java.lang.reflect.Field birthDateField = UserDTO.class.getDeclaredField("birthDate");
            birthDateField.setAccessible(true);
            birthDateField.set(target, source.getBirthDate());
            
            target.getRoles().addAll(source.getRoles());
        } catch (Exception e) {
            throw new RuntimeException("Error copying DTO fields", e);
        }
    }

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    private Long existingId, noExistingId, dependentId;
    private String existingUsername, noExistingUsername;
    private User user;
    private List<UserDetailsProjection> userDetails;
    private PageImpl<User> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 121L;
        dependentId = 2L;
        existingUsername = "admin@gmail.com";
        noExistingUsername = "alex@gmail.com";
        user = UserFactoryTest.createCustomUserClient(existingId, existingUsername);
        userDetails = UserDetailsFactoryTest.createCustomAdminUser(existingUsername);
        page = new PageImpl<>(List.of(user)); // instancio uma page de produtos

        // findAll
        Mockito.when(userRepository.searchByName(anyString(), (Pageable) any())).thenReturn(page);
        // findById
        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.findById(noExistingId)).thenReturn(Optional.empty());
        // insert
        Mockito.when(userRepository.save(any())).thenReturn(user);
        // Mock password encoder
        Mockito.when(passwordEncoder.encode(any())).thenReturn(any());
        // update
        Mockito.when(userRepository.getReferenceById(existingId)).thenReturn(user);
        Mockito.when(userRepository.getReferenceById(noExistingId)).thenThrow(EntityNotFoundException.class);
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // delete
        Mockito.when(userRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(userRepository.existsById(dependentId)).thenReturn(true);
        Mockito.when(userRepository.existsById(noExistingId)).thenReturn(false);
        Mockito.doNothing().when(userRepository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(userRepository).deleteById(dependentId);
        // loadUserByUsername
        Mockito.when(userRepository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetails);
        Mockito.when(userRepository.searchUserAndRolesByEmail(noExistingUsername)).thenReturn(new ArrayList<>());
    }

    @Test
    public void findAllShouldReturnListOfUserDTO() {
        PageRequest pageable = PageRequest.of(0, 12);
        Page<UserDTO> result = service.findAll("Maria", pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Maria", result.iterator().next().getName());
    }

    @Test
    public void findByIdShouldReturnUserDTOWhenIdExists() {
        UserDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Mockito.verify(userRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldReturnThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            UserDTO result = service.findById(noExistingId);
            Assertions.assertNull(result);
            Mockito.verify(userRepository).findById(noExistingId);
        });
    }

    @Test
    public void insertShouldReturnNewUserDTO() {
        // Arrange
        String plainPassword = "123456";
        String encodedPassword = "$2a$10$BZEayVp6X1Ry93e44/Rnze0hpK5J3ThbAdUm2OzH.GSWjA4zmtGHW";
        Long roleId = 1L;
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        
        // Create UserInsertDTO from User
        User tempUser = new User(null, "Fernando", "fernando@gmail.com", plainPassword, "61992632209", birthDate);
        tempUser.getRoles().add(new Role(roleId, "ROLE_ADMIN"));
        UserDTO baseDTO = new UserDTO(tempUser);
        UserInsertDTO dto = new UserInsertDTO(tempUser);
        dto.setPassword(plainPassword);
        // Copy fields from UserDTO since UserInsertDTO(User) doesn't call super
        copyDTOFields(dto, baseDTO);
        
        // Create saved user with encoded password
        User savedUser = UserFactoryTest.createUser(1L, "Fernando", "fernando@gmail.com", encodedPassword, "61992632209", birthDate);
        Role role = savedUser.getRoles().iterator().next();
        
        // Mock
        Mockito.when(passwordEncoder.encode(plainPassword)).thenReturn(encodedPassword);
        Mockito.when(roleRepository.getReferenceById(roleId)).thenReturn(role);
        Mockito.doAnswer(invocation -> {
            User userArg = invocation.getArgument(0);
            userArg.setId(savedUser.getId());
            return savedUser;
        }).when(userRepository).save(any(User.class));
        
        // Act
        UserDTO result = service.insert(dto);
        
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(savedUser.getId(), result.getId());
        Assertions.assertEquals("Fernando", result.getName());
        Assertions.assertEquals("fernando@gmail.com", result.getEmail());
        Assertions.assertEquals("61992632209", result.getPhone());
        Assertions.assertEquals(birthDate, result.getBirthDate());
        Assertions.assertFalse(result.getRoles().isEmpty());
        
        Mockito.verify(passwordEncoder).encode(plainPassword);
        Mockito.verify(roleRepository).getReferenceById(roleId);
    }

    @Test
    public void updateShouldReturnUpdatedUserWhenIdExists() {
        // Arrange
        LocalDate newBirthDate = LocalDate.of(1995, 5, 15);
        Long roleId = 1L;
        
        // Create UserUpdateDTO from User
        User tempUser = new User(null, "Fernando Silva", "fernando.silva@gmail.com", "password", "61987654321", newBirthDate);
        tempUser.getRoles().add(new Role(roleId, "ROLE_ADMIN"));
        UserDTO baseDTO = new UserDTO(tempUser);
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        copyDTOFields(updateDTO, baseDTO);
        
        // Create updated user
        User updatedUser = UserFactoryTest.createUser(existingId, "Fernando Silva", "fernando.silva@gmail.com", user.getPassword(), "61987654321", newBirthDate);
        Role role = updatedUser.getRoles().iterator().next();
        
        // Mock
        Mockito.when(roleRepository.getReferenceById(roleId)).thenReturn(role);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        // Act
        UserDTO result = service.update(existingId, updateDTO);
        
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals("Fernando Silva", result.getName());
        Assertions.assertEquals("fernando.silva@gmail.com", result.getEmail());
        Assertions.assertEquals("61987654321", result.getPhone());
        Assertions.assertEquals(newBirthDate, result.getBirthDate());
        Assertions.assertFalse(result.getRoles().isEmpty());
        
        Mockito.verify(userRepository).getReferenceById(existingId);
        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(roleRepository).getReferenceById(roleId);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        UserUpdateDTO updateDTO = new UserUpdateDTO();

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(noExistingId, updateDTO);
        });

        Mockito.verify(userRepository).getReferenceById(noExistingId);
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    @Test
    public void deleteShouldDeleteCategoryWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        Mockito.verify(userRepository).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(noExistingId);});
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsDependent() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundWhenUsernameDoesNotExist() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(noExistingUsername);
        });
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailWhenUsernameExists() {
        UserDetails result = service.loadUserByUsername(existingUsername);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUsername, result.getUsername());
    }

}
