package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.ProductDTO;
import com.gtech.Ecommerce.dto.ProductMinDTO;
import com.gtech.Ecommerce.entities.Product;
import com.gtech.Ecommerce.repositories.ProductRepository;
import com.gtech.Ecommerce.services.exceptions.DatabaseException;
import com.gtech.Ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    // services devolvem DTO ao inves da propria entidade
    @Transactional(readOnly = true) // aumenta a perfomance de leitura e bloquia o write
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado")
        ); // joga excessao tratada
        return new ProductDTO(product);
    }

    // usando Page e Pageable para retornar dados paginados
    @Transactional(readOnly = true) // aumenta a perfomance de leitura e bloquia o write
    public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
        Page<Product> result = repository.searchByName(name, pageable); // page já é um stream
        return result.map(x -> new ProductMinDTO(x));
    }


    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = new Product(); // instancia entity
        copyDtoToEntity(productDTO, product); // copia DTO recebido para entity
        product = repository.save(product); // salva entity

        return new ProductDTO(product); // devolve DTO
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            // referencia o id product sem abrir o banco
            Product product = repository.getReferenceById(id);
            copyDtoToEntity(dto, product);
            product = repository.save(product);

            return new ProductDTO(product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    // metodo usado quando há conflito entre entidades many to many
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
    }

}
