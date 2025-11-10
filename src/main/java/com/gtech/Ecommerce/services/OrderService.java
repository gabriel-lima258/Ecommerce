package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.order.OrderDTO;
import com.gtech.Ecommerce.entities.Order;
import com.gtech.Ecommerce.repositories.OrderRepository;
import com.gtech.Ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado")
        ); // joga excessao tratada
        return new OrderDTO(order);
    }
}
