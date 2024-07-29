package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

import com.example.demo.model.Admin;
import com.example.demo.model.Product;
import com.example.demo.model.Sales;
import com.example.demo.repo.AdminRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.SalesRepo;



@RestController
@RequestMapping("/admin/")
public class AdminControl {
    @Autowired
    ProductRepo repo;
    
    @Autowired
    SalesRepo salesRepo;
     
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private AdminRepo adminRepo;
    
    @PostMapping("/register")
    public String registerAdmin(@RequestBody Admin admin) {
        if (adminRepo.findByUsername(admin.getUsername()) != null) {
            return "Admin already exists";
        }
      
        admin.setRole("ADMIN");
        adminRepo.save(admin);
        return "Admin registered successfully";
    }

  
    @PostMapping("/login")
    public String loginAdmin(@RequestParam String username, @RequestParam String password) {
        Admin admin = adminRepo.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return "Login successful";
        }
        return "Invalid credentials";
    }

   
    @GetMapping("/logout")
    public String logout() {
        return "Logged out successfully";
    }

    @PostMapping("/add")
    public String addProduct(@RequestBody Product product) {
        repo.save(product);
        return "Product Added Successfully !! ";
    }

    
    @GetMapping("/showall")
    public List<Product> listProject() {
        return repo.findAll();
    }

    @GetMapping("/searchbyid/{productId}")
    public Product searchById(@PathVariable("productId") int id) {
        Optional<Product> optionalProduct = repo.findById(id);
        return optionalProduct.orElse(null);
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") int id) {
        repo.deleteById(id);
        return "Product Deleted Successfully !! ";
    }

    @PutMapping("/update/{productId}")
    public String updateProduct(@PathVariable("productId") int id, @RequestBody Product product) {
        Optional<Product> updatedProduct = repo.findById(id);
        if (updatedProduct.isPresent()) {
            Product p = updatedProduct.get();
            p.setProductName(product.getProductName());
            p.setProductCost(product.getProductCost());
            p.setProductCategories(product.getProductCategories());
            repo.save(p);
            return "Changes Has Been Saved !! ";
        } else {
            return "No Product Found with Id: " + id;
        }
    }

    @GetMapping("/filterbycategories/{productCategories}")
    public List<Product> filterByCategories(@PathVariable("productCategories") String name) {
        return repo.findByProductCategories(name);
    }
    
    @GetMapping("/sort")
    public List<Product> sorting(@RequestParam int maxcost, @RequestParam int mincost) {
        return repo.findByProductCostBetween(mincost, maxcost);
    }
    
    @GetMapping("/count")
    public long countItems() {
        return repo.count();
    }
    
    @GetMapping("/checkStock")
    public String checkStock() {
        List<Product> products = repo.findAll();
        StringBuilder message = new StringBuilder();
        boolean sendEmail = false;

        for (Product product : products) {
            if (product.getProductQuantitity() < 10) {
                message.append("Product ID: ").append(product.getProductId())
                        .append(", Product Name: ").append(product.getProductName())
                        .append(" is low on stock. Current stock: ").append(product.getProductQuantitity())
                        .append("\n");
//                return "Product ID" + product.getProductId() +"Product Name:"+ product.getProductName()+", Product Quantity:" + product.getProductQuantitity();
                sendEmail = true;
            }
        }

        if (sendEmail) {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo("itzprakhu@gmail.com"); 
            email.setSubject("Low Stock Alert");
            email.setText(message.toString());
            mailSender.send(email);
            return "Low stock alert email sent.";
        } else {
            return "All products have sufficient stock.";
        }
    }
    
    @PostMapping("/addSales")
    public String addSales(@RequestBody Sales sales) {
        salesRepo.save(sales);
        return "Sales record added successfully!";
    }
    
    @GetMapping("/allSales")
    public List<Sales> getAllSales() {
        return salesRepo.findAll();
    }
    
    @GetMapping("/salesReport")
    public List<Sales> getSalesReport(@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        return salesRepo.findSalesBetweenDates(start, end);
    }
    
    
     
    
}
