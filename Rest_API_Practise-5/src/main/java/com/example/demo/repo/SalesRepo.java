package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Sales;

import java.util.Date;
import java.util.List;

public interface SalesRepo extends JpaRepository<Sales, Integer> {
    
    @Query("SELECT s FROM Sales s WHERE s.salesDate BETWEEN :startDate AND :endDate")
    List<Sales> findSalesBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}