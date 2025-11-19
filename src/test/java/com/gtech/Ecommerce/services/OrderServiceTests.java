package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.order.OrderDTO;
import com.gtech.Ecommerce.entities.Order;
import com.gtech.Ecommerce.entities.OrderItem;
import com.gtech.Ecommerce.entities.Product;
import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.factories.OrderFactoryTest;
import com.gtech.Ecommerce.factories.ProductFactoryTest;
import com.gtech.Ecommerce.factories.UserFactoryTest;
import com.gtech.Ecommerce.repositories.OrderItemRepository;
import com.gtech.Ecommerce.repositories.OrderRepository;
import com.gtech.Ecommerce.repositories.ProductRepository;
import com.gtech.Ecommerce.services.exceptions.ForbiddenException;
import com.gtech.Ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private AuthService authService;

    private Long existingOrderId, noExistingOrderId;
    private Long existingProductId, noExistingProductId;
    private Order order;
    private OrderDTO orderDTO;
    private User admin, client;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        noExistingOrderId = 121L;
        existingProductId = 1L; // ProductFactoryTest.createProduct() cria produto com ID 1L
        noExistingProductId = 998L;
        admin = UserFactoryTest.createCustomUserAdmin(1L, "Bob");
        client = UserFactoryTest.createCustomUserClient(2L, "Alex");
        order = OrderFactoryTest.createOrder(client);
        orderDTO = new OrderDTO(order);
        product = ProductFactoryTest.createProduct();

        // findById
        Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.ofNullable(order));
        Mockito.when(orderRepository.findById(noExistingOrderId)).thenReturn(Optional.empty());
        // insert - mock product by ID that exists in OrderDTO
        Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(noExistingProductId)).thenThrow(EntityNotFoundException.class);
        Mockito.when(orderRepository.save(any())).thenReturn(order);
        Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));

    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
        OrderDTO result = service.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingOrderId, result.getId());
        Mockito.verify(orderRepository).findById(existingOrderId);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
        OrderDTO result = service.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingOrderId, result.getId());
        Mockito.verify(orderRepository).findById(existingOrderId);
    }

    @Test
    public void findByIdShouldReturnThrowForbiddenExceptionWhenIdExistsAndOtherClientLogged() {
        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());
        Assertions.assertThrows(ForbiddenException.class, () -> {
            OrderDTO result = service.findById(existingOrderId);
        });
    }

    @Test
    public void findByIdShouldReturnThrowForbiddenExceptionWhenIdDoesNotExist() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            OrderDTO result = service.findById(noExistingOrderId);
        });
    }

    @Test
    public void insertShouldReturnNewOrderDTOWhenIsSelfClient() {
        // Arrange
        Mockito.when(authService.authenticated()).thenReturn(client);
        // Act
        OrderDTO result = service.insert(orderDTO);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(client.getId(), result.getClient().getId());
        Assertions.assertFalse(result.getItems().isEmpty());
        Assertions.assertEquals(orderDTO.getItems().size(), result.getItems().size());
    }

    @Test
    public void insertShouldReturnNewOrderDTOWhenIsAdmin() {
        // Arrange
        Mockito.when(authService.authenticated()).thenReturn(admin);
        // Act
        OrderDTO result = service.insert(orderDTO);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(admin.getId(), result.getClient().getId());
        Assertions.assertFalse(result.getItems().isEmpty());
        Assertions.assertEquals(orderDTO.getItems().size(), result.getItems().size());
    }

    @Test
    public void insertShouldReturnThrowUserNotFoundExceptionWhenUserNotLogged() {
        // Arrange
        Mockito.doThrow(UsernameNotFoundException.class).when(authService).authenticated();
        order.setClient(new User());
        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            OrderDTO result = service.insert(orderDTO);
        });
    }

    @Test
    public void insertShouldReturnThrowEntityNotFoundExceptionWhenProductIdDoesNotExist() {
        Mockito.when(authService.authenticated()).thenReturn(client);

        product.setId(noExistingProductId);
        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);

        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            OrderDTO result = service.insert(orderDTO);
        });
    }
}
