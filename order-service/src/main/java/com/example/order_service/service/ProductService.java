package com.example.order_service.service;

import com.example.order_service.dto.ProductDto;
import com.example.order_service.entity.Product;
import com.example.order_service.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<ProductDto> findAll() {
    return productRepository.findAll().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  private ProductDto toDto(Product product) {
    return new ProductDto(
        product.getId(),
        product.getTitle(),
        product.getDescription(),
        product.getPrice()
    );
  }
}
