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


    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = new Product(); // instancia entity
        copyDtoToEntity(productDTO, product); // copia DTO recebido para entity
        product = repository.save(product); // salva entity

        return new ProductDTO(product); // devolve DTO
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        // referencia o id product sem abrir o banco
        Product product = repository.getReferenceById(id);
        copyDtoToEntity(productDTO, product);
        product = repository.save(product);

        return new ProductDTO(product);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void copyDtoToEntity(ProductDTO productDTO, Product product) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImgUrl(productDTO.getImgUrl());
    }

}
