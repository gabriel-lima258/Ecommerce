package com.gtech.Ecommerce.repositories;

import com.gtech.Ecommerce.entities.Role;
import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
