package com.gtech.Ecommerce.factories;

import com.gtech.Ecommerce.dto.product.ProductDTO;
import com.gtech.Ecommerce.entities.Category;
import com.gtech.Ecommerce.entities.Product;

public class ProductFactoryTest {
    public static Product createProduct() {
        Category category = CategoryFactoryTest.createCategory();
        Product product = new Product(1L, "Iphone", "Celular super novo", 12000.0, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
        product.getCategories().add(category);
        return product;
    }

    public static Product createProduct(Long id, String name, String description, Double price, String imgUrl) {
        Category category = CategoryFactoryTest.createCategory();
        Product product = new Product(id, name, description, price, imgUrl);
        product.getCategories().add(category);
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product);
    }
}
