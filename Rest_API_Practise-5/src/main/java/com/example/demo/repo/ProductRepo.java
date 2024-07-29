package com.example.demo.repo;

import org.springdoc.core.converters.models.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Product;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findByProductCategories(String productCategories);
    List<Product> findByProductCostBetween(int mincost, int maxcost);
    List<Product> findByProductQuantitity(int quantitity);
    List<Product> findByOrderByProductIdAsc();
   
}
