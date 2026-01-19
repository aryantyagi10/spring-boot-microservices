package com.ecommerce.order_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private Long userId;
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
