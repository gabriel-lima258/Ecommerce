package com.gtech.Ecommerce.factories;

import com.gtech.Ecommerce.dto.user.UserDTO;
import com.gtech.Ecommerce.dto.user.UserInsertDTO;
import com.gtech.Ecommerce.dto.user.UserUpdateDTO;
import com.gtech.Ecommerce.entities.Role;
import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.projections.UserDetailsProjection;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDetailsFactoryTest {

    public static List<UserDetailsProjection> createCustomClientUser(String username) {
        List<UserDetailsProjection> list = new ArrayList<>();
        list.add(new UserDetailsImpl(username, "123456", 1L, "ROLE_CLIENT"));
        return list;
    }

    public static List<UserDetailsProjection> createCustomAdminUser(String username) {
        List<UserDetailsProjection> list = new ArrayList<>();
        list.add(new UserDetailsImpl(username, "123456", 2L, "ROLE_ADMIN"));
        return list;
    }

    public static List<UserDetailsProjection> createCustomClientAdminUser(String username) {
        List<UserDetailsProjection> list = new ArrayList<>();
        list.add(new UserDetailsImpl(username, "123456", 1L, "ROLE_CLIENT"));
        list.add(new UserDetailsImpl(username, "123456", 2L, "ROLE_ADMIN"));
        return list;
    }
}

class UserDetailsImpl implements UserDetailsProjection {
    private String username;
    private String password;
    private Long roleId;
    private String authority;

    UserDetailsImpl() {
    }

    UserDetailsImpl(String username, String password, Long roleId, String authority) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.authority = authority;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
