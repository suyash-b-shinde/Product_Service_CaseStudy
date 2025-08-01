package com.productapp.service;

import java.util.List;
import java.util.Optional;

import com.productapp.entity.Product;

public interface ProductService {
    Product createProduct(Product product);
    Optional<Product> getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> searchProductsByName(String name);
    List<Product> searchByPriceRange(double min, double max);
    List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice);


    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}
