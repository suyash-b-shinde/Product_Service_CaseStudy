package com.productapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.productapp.entity.Product;
import com.productapp.exception.ResourceNotFoundException;
import com.productapp.repository.ProductRepository;
import com.productapp.specification.ProductSpecification;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

@Override
public Product updateProduct(Long id, Product updatedProduct) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

    product.setName(updatedProduct.getName());
    product.setDescription(updatedProduct.getDescription());
    product.setPrice(updatedProduct.getPrice());
    product.setStockQuantity(updatedProduct.getStockQuantity());
    product.setCategory(updatedProduct.getCategory());

    return productRepository.save(product);
}




    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> searchByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // ✅ NEW — Dynamic Search using Specification
    @Override
    public List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice) {
        Specification<Product> spec = Specification
                .where(ProductSpecification.nameContains(name))
                .and(ProductSpecification.categoryEquals(category))
                .and(ProductSpecification.priceBetween(minPrice, maxPrice));

        return productRepository.findAll(spec);
    }
}
