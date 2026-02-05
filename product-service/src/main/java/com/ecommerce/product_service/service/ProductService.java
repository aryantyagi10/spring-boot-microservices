package com.ecommerce.product_service.service;


import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service                            // 1. Tells Spring: "This holds business logic"
@RequiredArgsConstructor           // 2. Lombok: Generates constructor for dependency injection
@Slf4j                            // 3. Lombok: Gives us a 'log' variable for logging
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest){

        //Step 1: Map DTO to Entity
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .skuCode(productRequest.getSkuCode())
                .price(productRequest.getPrice())
                .stockQuantity(productRequest.getStockQuantity())
                .build();

        //Step 2: Save to Database
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());

        //Step 3: Map Entity back to Response DTO
        return mapToProductResponse(product);
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();

        //Convert list of Entities to list of DTOs
        return products.stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    //Checks if a product exists and has enough quantity
    public boolean isInStock(String skuCode, Integer quantity){
        return productRepository.findBySkuCode(skuCode)
                .map(product -> product.getStockQuantity() >= quantity)
                .orElse(false);
    }


    //Helper Method to convert Entity -> DTO
    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .skuCode(product.getSkuCode())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
}
