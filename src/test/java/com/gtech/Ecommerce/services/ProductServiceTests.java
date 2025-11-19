package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.product.ProductDTO;
import com.gtech.Ecommerce.dto.product.ProductMinDTO;
import com.gtech.Ecommerce.entities.Product;
import com.gtech.Ecommerce.factories.ProductFactoryTest;
import com.gtech.Ecommerce.repositories.ProductRepository;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository productRepository;

    private Long existingId;
    private Long noExistingId;
    private Long dependentId;
    private Product product;
    private ProductDTO productDTO;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 121L;
        dependentId = 2L;
        product = ProductFactoryTest.createProduct();
        productDTO = ProductFactoryTest.createProductDTO();
        page = new PageImpl<>(List.of(product)); // instancio uma page de produtos

        // findAll
        Mockito.when(productRepository.searchByName(anyString(), (Pageable) any())).thenReturn(page);
        // findById
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.ofNullable(product));
        Mockito.when(productRepository.findById(noExistingId)).thenReturn(Optional.empty());
        // insert
        Mockito.when(productRepository.save(any())).thenReturn(product);
        // update
        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(noExistingId)).thenThrow(EntityNotFoundException.class);
        // delete
        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);
        Mockito.when(productRepository.existsById(noExistingId)).thenReturn(false);
        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

    }

    @Test
    public void findAllShouldReturnListOfProductDTO() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProductMinDTO> result = service.findAll("Iphone", pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Iphone", result.iterator().next().getName());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Mockito.verify(productRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldReturnThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = service.findById(noExistingId);
            Assertions.assertNull(result);
            Mockito.verify(productRepository).findById(noExistingId);
        });
    }

    @Test
    public void insertShouldReturnNewProductDTO() {
        ProductDTO result = service.insert(productDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getId(), result.getId());
    }

    @Test
    public void updateShouldReturnUpdatedProductWhenIdExists() {
        ProductDTO result = service.update(existingId, productDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
    }

    @Test
    public void deleteShouldDeleteCategoryWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        Mockito.verify(productRepository).deleteById(existingId);
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
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = service.update(noExistingId, productDTO);
            Mockito.verify(productRepository).getReferenceById(noExistingId);
        });
    }
}
