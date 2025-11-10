package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService service;

    // validação se o user é ele mesmo ou se é ADMIN
    public void validateSelfOrAdmin(long userId) {
        User me = service.authenticated();
        if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenException("Access Denied");
        }
    }
}
