package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.ProductDTO;
import com.gtech.Ecommerce.entities.Product;
import com.gtech.Ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    // services devolvem DTO ao inves da propria entidade
    @Transactional(readOnly = true) // aumenta a perfomance de leitura e bloquia o write
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).get();
        return new ProductDTO(product);
    }

    // usando Page e Pageable para retornar dados paginados
    @Transactional(readOnly = true) // aumenta a perfomance de leitura e bloquia o write
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> result = repository.findAll(pageable); // page já é um stream
        return result.map(x -> new ProductDTO(x));
    }
}
