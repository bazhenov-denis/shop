package com.example.order_service.controller;

import com.example.order_service.dto.ProductDto;
import com.example.order_service.entity.Product;
import com.example.order_service.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

  public ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public List<ProductDto> getAllProducts() {
    return productService.findAll();
  }

  @GetMapping("/{id}")
  public Optional<Product> getProductsById(@PathVariable Long id) {
    return productService.findById(id);
  }
}
