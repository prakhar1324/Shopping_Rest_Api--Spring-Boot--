package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.UserRepo;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private ProductRepo productRepo;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            return "User already exists";
        }
      
        user.setRole("USER"); 
        userRepo.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        User user = userRepo.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "Login successful";
        }
        return "Invalid credentials";
    }

    @GetMapping("/logout")
    public String logout() {
        return "Logged out successfully";
    }
    
    @GetMapping("/filterbycategories/{productCategories}")
    public List<Product> filterByCategories(@PathVariable("productCategories") String productCategories) {
        return productRepo.findByProductCategories(productCategories);
    }
    @GetMapping("/sort")
    public List<Product> sortProducts(@RequestParam String sortBy, @RequestParam String order) {
//        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        return productRepo.findByOrderByProductIdAsc();
    }
}
