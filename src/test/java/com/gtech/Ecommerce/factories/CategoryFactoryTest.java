package com.gtech.Ecommerce.factories;


import com.gtech.Ecommerce.dto.product.CategoryDTO;
import com.gtech.Ecommerce.entities.Category;
import com.gtech.Ecommerce.entities.Category;

public class CategoryFactoryTest {
    public static Category createCategory() {
        Category category = new Category(1L, "Tecnologia");
        return category;
    }

    public static Category createCategory(Long id, String name) {
        return new Category(id, name);
    }

    public static CategoryDTO createCategoryDTO() {
        Category category = createCategory();
        return new CategoryDTO(category);
    }
}
