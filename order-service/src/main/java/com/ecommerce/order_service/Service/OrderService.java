package com.ecommerce.order_service.Service;

import com.ecommerce.order_service.DTO.OrderLineItemsDto;
import com.ecommerce.order_service.DTO.OrderRequest;
import com.ecommerce.order_service.Entity.Order;
import com.ecommerce.order_service.Entity.OrderLineItems;
import com.ecommerce.order_service.Repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional               // Ensures "All or Nothing" database transaction
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;


    public void placeOrder(OrderRequest orderRequest){

        // --- STEP 1: Check User Exists (Call User Service) ---
        // URL: http://localhost:8082/api/user/{id}

        try {
            webClientBuilder.build().get()
                    .uri("http://localhost:8082/api/user/" + orderRequest.getUserId())
                    .retrieve()
                    .bodyToMono(Object.class)  // We don't care about the details, just that it exists
                    .block(); // Wait for answer
        } catch (Exception e){
            // If User Service returns 404, WebClient throws an error.
            throw new IllegalArgumentException("Invalid User ID. Cannot place order");
        }


        // --- STEP 2: Check Stock (Inter-Service Communication) ---

        // We loop through every item the user wants to buy
        for(OrderLineItemsDto item : orderRequest.getOrderLineItemsDtoList()){

            // Call Product Service: GET http://localhost:8080/api/product/in-stock
            Boolean isInStock = webClientBuilder.build().get()
                    .uri("http://localhost:8080/api/product/in-stock",          //uri is the address that identifies a resource on a server
                            uriBuilder -> uriBuilder                      //uriBuilder is a helper that builds a URI step by step safely
                                    .queryParam("skuCode", item.getSkuCode())  //It is written after ? in the url - Use Case: Filter, search, sort
                                    .queryParam("quantity", item.getQuantity())
                                    .build())
                    .retrieve() // Send the request
                    .bodyToMono(Boolean.class) // Convert response to Boolean
                    .block(); // Wait for the answer (Synchronous)

            // If any item is out of stock, reject the WHOLE order
            if(Boolean.FALSE.equals(isInStock)){      //Boolean.False is used to avoid NullPointerException - If isInStock is null - condition false(no crash)
                throw new IllegalArgumentException("Product with SKU " + item.getSkuCode() + " is not in stock");
            }
        }

        // --- STEP 3: Save Order (Only runs if Step 1 passes) ---

        // 1. Map the items first using the helper method
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToEntity)
                .toList();

        // 2. Build the Order using the Builder Pattern
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .userID(orderRequest.getUserId())
                .orderLineItemsList(orderLineItems)
                .build();


        // 3. Save to DB
        // Because of CascadeType.ALL, this saves the Order AND the Items
        orderRepository.save(order);
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto){
        return OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .skuCode(orderLineItemsDto.getSkuCode())
                .build();
    }
}
