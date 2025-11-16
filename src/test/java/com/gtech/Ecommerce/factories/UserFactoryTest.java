package com.gtech.Ecommerce.factories;

import com.gtech.Ecommerce.dto.user.UserDTO;
import com.gtech.Ecommerce.entities.Role;
import com.gtech.Ecommerce.entities.User;

import java.time.LocalDate;

public class UserFactoryTest {
    public static User createUser() {
        User user = new User(1L, "Gabriel Lima", "test@gmail.com", "61992998921", "1123454", LocalDate.now());
        user.getRoles().add(new Role(1L, "ADMIN"));
        return user;
    }

    public static UserDTO createUserDTO() {
        User user = createUser();
        return new UserDTO(user);
    }
}
