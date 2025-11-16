package com.gtech.Ecommerce.dto.user;

import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.services.validations.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

    private String password;

    public UserInsertDTO() {
    }

    public UserInsertDTO(String password) {
        this.password = password;
    }

    public UserInsertDTO(User entity) {
        password = entity.getPassword();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
