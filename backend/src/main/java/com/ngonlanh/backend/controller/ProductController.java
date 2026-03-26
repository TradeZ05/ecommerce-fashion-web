package com.ngonlanh.backend.controller;

import com.ngonlanh.backend.entity.Product;
import com.ngonlanh.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Lấy danh sách toàn bộ sản phẩm
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Thêm sản phẩm mới
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // Xóa sản phẩm theo ID
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productRepository.deleteById(id);
    }

    // Cập nhật món ăn theo ID
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn này!"));
                
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setImageUrl(productDetails.getImageUrl());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        
        return productRepository.save(product);
    }
}