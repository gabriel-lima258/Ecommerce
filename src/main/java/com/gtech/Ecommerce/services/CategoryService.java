package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.product.CategoryDTO;
import com.gtech.Ecommerce.dto.product.ProductMinDTO;
import com.gtech.Ecommerce.entities.Category;
import com.gtech.Ecommerce.entities.Product;
import com.gtech.Ecommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = repository.findAll(); // page já é um stream
        return result.stream().map(x -> new CategoryDTO(x)).toList();
    }
}
