package com.gtech.Ecommerce.factories;

import com.gtech.Ecommerce.dto.user.UserDTO;
import com.gtech.Ecommerce.entities.Role;
import com.gtech.Ecommerce.entities.User;

import java.time.LocalDate;

public class UserFactoryTest {
    public static User createUserAdmin() {
        User user = new User(1L, "Gabriel Lima", "test@gmail.com", "123456", "61992998921", LocalDate.now());
        user.getRoles().add(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomUserAdmin(Long id, String username) {
        User user = new User(id, "Gabriel Lima", username, "123456", "61992998921", LocalDate.now());
        user.getRoles().add(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createUserClient() {
        User user = new User(2L, "Maria", "maria@gmail.com", "123456", "61992998921", LocalDate.now());
        user.getRoles().add(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createCustomUserClient(Long id, String username) {
        User user = new User(id, "Maria", username, "123456", "61992998921", LocalDate.now());
        user.getRoles().add(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createUser(Long id, String name, String email, String password, String phone) {
        User user = new User(id, name, email, password, phone, LocalDate.now());
        user.getRoles().add(new Role(1L, "ROLE_ADMIN"));
        return user;
    }

    public static User createUser(Long id, String name, String email, String password, String phone, LocalDate birthDate) {
        User user = new User(id, name, email, password, phone, birthDate);
        user.getRoles().add(new Role(1L, "ROLE_ADMIN"));
        return user;
    }

    public static UserDTO createUserDTO() {
        User user = createUserClient();
        return new UserDTO(user);
    }

}
