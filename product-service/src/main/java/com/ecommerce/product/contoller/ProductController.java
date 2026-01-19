package com.ecommerce.product.contoller;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                           // 1. Tells Spring: "This class handles REST API requests"
@RequestMapping("/api/product")          // 2. Base URL: All methods here start with /api/product
@RequiredArgsConstructor                // 3. Injects ProductService automatically
public class ProductController {

    private final ProductService productService;

    @PostMapping                            // Maps to POST requests (Create)
    @ResponseStatus(HttpStatus.CREATED)    // Returns 201 (Created) instead of default 200
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest productRequest){
        // @RequestBody: Converts JSON -> Java Object
        // @Valid: Triggers the validation rules in the DTO
        return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity){
        return productService.isInStock(skuCode, quantity);
    }
}
