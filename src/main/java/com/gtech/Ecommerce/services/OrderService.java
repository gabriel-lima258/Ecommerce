package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.order.OrderDTO;
import com.gtech.Ecommerce.dto.order.OrderItemDTO;
import com.gtech.Ecommerce.dto.product.CategoryDTO;
import com.gtech.Ecommerce.dto.product.ProductDTO;
import com.gtech.Ecommerce.entities.*;
import com.gtech.Ecommerce.repositories.OrderItemRepository;
import com.gtech.Ecommerce.repositories.OrderRepository;
import com.gtech.Ecommerce.repositories.ProductRepository;
import com.gtech.Ecommerce.repositories.UserRepository;
import com.gtech.Ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado")
        ); // joga excessao tratada
        // se não for o dono do pedido ou se nao for admin joga exceção
        authService.validateSelfOrAdmin(order.getClient().getId());
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        // instancia o order
        Order order = new Order();

        // coloco os atributos de order
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        // crio o user autenticado e set em order
        User user = authService.authenticated();
        order.setClient(user);

        // percorre item do dto que o user passou
        for (OrderItemDTO itemDto: dto.getItems()) {
            // crio o produto pelo id do dto que o user passou
            Product product = productRepository.getReferenceById(itemDto.getProductId());
            // por fim instancio o order item, passando as instancias
            OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }

        repository.save(order);
        // tambem temos que salvar a classe de associacao de order e product
        orderItemRepository.saveAll(order.getItems());

        return new OrderDTO(order); // devolve DTO
    }
}
