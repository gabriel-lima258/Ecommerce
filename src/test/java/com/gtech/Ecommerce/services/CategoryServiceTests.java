package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.product.CategoryDTO;
import com.gtech.Ecommerce.dto.product.ProductDTO;
import com.gtech.Ecommerce.entities.Category;
import com.gtech.Ecommerce.factories.CategoryFactoryTest;
import com.gtech.Ecommerce.repositories.CategoryRepository;
import com.gtech.Ecommerce.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long noExistingId;
    private Category category;
    private CategoryDTO categoryDTO;
    private List<Category> list;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 121L;
        category = CategoryFactoryTest.createCategory();
        categoryDTO = CategoryFactoryTest.createCategoryDTO();
        list = new ArrayList<>();
        list.add(category);

        // findAll
        Mockito.when(categoryRepository.findAll()).thenReturn(list);
        // findById
        Mockito.when(categoryRepository.findById(existingId)).thenReturn(Optional.ofNullable(category));
        Mockito.when(categoryRepository.findById(noExistingId)).thenReturn(Optional.empty());
        // insert
        Mockito.when(categoryRepository.save(any())).thenReturn(category);
    }

    @Test
    public void findAllShouldReturnListOfCategoryDTO() {
        List<CategoryDTO> result = service.findAll();
        Assertions.assertNotNull(result);
        Mockito.verify(categoryRepository).findAll(Sort.by("name"));
    }

    @Test
    public void findByIdShouldReturnCategoryDTOWhenIdExists() {
        CategoryDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Mockito.verify(categoryRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldReturnThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            CategoryDTO result = service.findById(noExistingId);
            Assertions.assertNull(result);
            Mockito.verify(categoryRepository).findById(noExistingId);
        });
    }

    @Test
    public void insertShouldReturnNewCategoryDTO() {
        CategoryDTO result = service.insert(categoryDTO);
        Assertions.assertNotNull(result);
    }
}
