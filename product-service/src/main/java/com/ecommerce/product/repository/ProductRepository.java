package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //Finds a product by its SKU Code
    Optional<Product> findBySkuCode(String skuCode);
}
