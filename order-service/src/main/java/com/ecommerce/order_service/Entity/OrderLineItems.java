package com.ecommerce.order_service.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "t_order_line_items")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skuCode;

    private BigDecimal price;

    private Integer quantity;

}
