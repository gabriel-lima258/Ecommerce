package com.gtech.Ecommerce.repositories;

import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.factories.UserFactoryTest;
import com.gtech.Ecommerce.projections.UserDetailsProjection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {

        @Autowired
        private UserRepository repository;

        private long existingId;
        private long noExistingId;
        private long countTotalUser;
        private String name;
        private String email;

        @BeforeEach
        void setUp() throws Exception {
            existingId = 1L;
            noExistingId = 200L;
            countTotalUser = 2L;
            name = "Notebook";
            email = "test@gmail.com";
        }

        @Test
        public void findByIdShouldNotBeEmptyWhenIdExists() {
            Optional<User> result = repository.findById(existingId);
            Assertions.assertTrue(result.isPresent());
        }

        @Test
        public void findByIdShouldBeEmptyWhenIdDoesNotExists() {
            Optional<User> result = repository.findById(noExistingId);
            Assertions.assertTrue(result.isEmpty());
        }

        @Test
        public void searchUserAndRoleShouldReturnUserByEmail() {
            List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(email);
            Assertions.assertNotNull(result);
        }

        @Test
        public void saveShouldPersistAutoIncrementWhenIdIsNull() {
            User product = UserFactoryTest.createUser();
            product.setId(null);
            repository.save(product);
            Assertions.assertNotNull(product.getId());
            Assertions.assertEquals(countTotalUser + 1, product.getId());
        }

        @Test
        public void deleteShouldDeleteObjectWhenIdExists() {
            repository.deleteById(existingId);
            Optional<User> result = repository.findById(existingId);
            Assertions.assertFalse(result.isPresent());
        }

}
