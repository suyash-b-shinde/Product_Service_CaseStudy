package com.productapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.productapp.entity.Product;
import com.productapp.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // Optional for frontend testing
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // Simple search by name only
    @GetMapping("/search/name")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProductsByName(name));
    }

    // Advanced search with optional filters: name, category, minPrice, maxPrice
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(productService.searchProducts(name, category, minPrice, maxPrice));
    }

    // Additional price-range search endpoints (optional, you may keep or remove)
    @GetMapping("/price")
    public ResponseEntity<List<Product>> searchByPrice(@RequestParam double min, @RequestParam double max) {
        return ResponseEntity.ok(productService.searchByPriceRange(min, max));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterByPrice(
            @RequestParam("minPrice") double minPrice,
            @RequestParam("maxPrice") double maxPrice) {
        return ResponseEntity.ok(productService.searchByPriceRange(minPrice, maxPrice));
    }
}
