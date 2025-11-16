package com.gtech.Ecommerce.services.validations;

import java.util.ArrayList;
import java.util.List;

import com.gtech.Ecommerce.dto.FieldMessageDTO;
import com.gtech.Ecommerce.dto.user.UserInsertDTO;
import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessageDTO> list = new ArrayList<>();

        // tratando o erro de email unico
        User user = repository.findByEmail(dto.getEmail());
        if (user != null) {
            list.add(new FieldMessageDTO("email", "Este email já existe"));
        }

        for (FieldMessageDTO e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}