package com.gtech.Ecommerce.factories;

import com.gtech.Ecommerce.dto.product.ProductDTO;
import com.gtech.Ecommerce.entities.Category;
import com.gtech.Ecommerce.entities.Product;

public class ProductFactoryTest {
    public static Product createProduct() {
        Product product = new Product(1L, "Iphone", "Celular super novo", 12000.0, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
        product.getCategories().add(new Category(1L, "Eletrodoméstico"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product);
    }
}
