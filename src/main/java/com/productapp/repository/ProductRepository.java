package com.productapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.productapp.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,JpaSpecificationExecutor<Product> {
    List<Product> findByCategoryIgnoreCase(String category);
    List<Product> findByNameContainingIgnoreCase(String name);
    
List<Product> findByPriceBetween(double min, double max);

}
