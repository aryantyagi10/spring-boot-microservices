package com.ecommerce.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                // Route 1: Product Service
                .route("product-service", r -> r.path("/api/product/**")
                        .uri("lb://product-service")) // <--- Lowercase matches Eureka better

                // Route 2: Order Service
                .route("order-service", r -> r.path("/api/order/**")
                        .uri("lb://order-service"))

                // Route 3: User Service
                .route("user-service", r -> r.path("/api/user/**")
                        .uri("lb://user-service"))

                .build();
    }


}
