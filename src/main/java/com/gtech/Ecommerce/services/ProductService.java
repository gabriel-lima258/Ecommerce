package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.product.CategoryDTO;
import com.gtech.Ecommerce.dto.product.ProductDTO;
import com.gtech.Ecommerce.dto.product.ProductMinDTO;
import com.gtech.Ecommerce.dto.upload.UriDTO;
import com.gtech.Ecommerce.entities.Category;
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
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    @Autowired
    S3Service s3Service;

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
        return result.map(ProductMinDTO::new);
    }


    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product(); // instancia entity
        copyDtoToEntity(productDTO, entity); // copia DTO recebido para entity
        entity = repository.save(entity); // salva entity

        return new ProductDTO(entity); // devolve DTO
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            // referencia o id product sem abrir o banco
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);

            return new ProductDTO(entity);
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

    public UriDTO uploadFile(MultipartFile file) {
        URL url = s3Service.uploadFile(file, "products");
        return new UriDTO(url.toString());
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        // limpar as categorias antes de inserir ou atualizar elas
        entity.getCategories().clear();

        // varrer as categorias de produto
        for (CategoryDTO catDto: dto.getCategories()) {
            Category category = new Category();
            category.setId(catDto.getId()); // copia o valor do post dentro de uma nova categoria
            entity.getCategories().add(category); // adiciona o category no set dentro de produto
        }
    }
}
