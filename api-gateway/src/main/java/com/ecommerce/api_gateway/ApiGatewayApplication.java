package com.ecommerce.api_gateway;

import com.ecommerce.api_gateway.filter.AuthenticationFilter;
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
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, AuthenticationFilter authFilter){

        // We create a new Config object for the filter
        AuthenticationFilter.Config config = new AuthenticationFilter.Config();

        return builder.routes()
                // Route 1: Product Service
                .route("product-service", r -> r.path("/api/product/**")
                        .filters(f -> f.filter(authFilter.apply(config)))
                        .uri("lb://product-service")) // <--- Lowercase matches Eureka better

                // Route 2: Order Service
                .route("order-service", r -> r.path("/api/order/**")
                        .filters(f -> f.filter(authFilter.apply(config)))
                        .uri("lb://order-service"))

                // Route 3: User Service
                .route("user-service", r -> r.path("/api/user/**")
                        .uri("lb://user-service"))

                .build();
    }


}
