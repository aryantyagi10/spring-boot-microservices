package com.ecommerce.order_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration              // 1. Sources of bean definitions
public class WebClientConfig {

    @Bean                   // 2. Creates a bean named 'webClientBuilder'
    @LoadBalanced
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}


/* WebClientBuilder is a factory that creates WebClient objects.
   WebClient = actual HTTP client
   WebClientBuilder = tool to configure and create it
 */

