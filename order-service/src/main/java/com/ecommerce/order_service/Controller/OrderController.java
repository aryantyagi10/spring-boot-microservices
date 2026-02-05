package com.ecommerce.order_service.Controller;

import com.ecommerce.order_service.DTO.OrderRequest;
import com.ecommerce.order_service.DTO.OrderServiceResponse;
import com.ecommerce.order_service.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest){
        OrderServiceResponse response = orderService.placeOrder(orderRequest);

        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getMessage());
        }else {
         return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response.getMessage());
        }
    }
}
