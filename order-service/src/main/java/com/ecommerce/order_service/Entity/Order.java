package com.ecommerce.order_service.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "t_orders")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    private Long userID;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderLineItems> orderLineItemsList;

}
