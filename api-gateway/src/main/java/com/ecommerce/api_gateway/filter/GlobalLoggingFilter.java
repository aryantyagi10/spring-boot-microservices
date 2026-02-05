package com.ecommerce.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalLoggingFilter implements GlobalFilter {

    private final Logger logger = LoggerFactory.getLogger(GlobalLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        // 1. Intercept the Request  ---PRE logic
        logger.info("Global Filter Intercepted Request: Path -> {}", exchange.getRequest().getPath());

        // 2. Pass it to the next step (The Microservice)
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {   //Code inside .then runs after microservices responds
            // 3. Intercept the Response (after microservice replies)  ---POST logic
            logger.info("Response Status Code -> {}", exchange.getResponse().getStatusCode());
        }));
    }
}
/*
-- Filter: Filter is a piece of code that intercepts a request or response to do something before or after
              the request reaches the microservices. Like - Logging, Authentication

-- exchange: exchange contains everything about the current HTTP request AND response.
             It holds:
             - Request (headers, path, method, body)
             - Response (status code, headers)
             - Attributes (extra metadata)
             Examples:
             exchange.getRequest().getPath()
             exchange.getResponse().getStatusCode()

-- chain: chain represents the next step in the filter pipeline.
          Think of it as: "What should run after THIS filter?"
          The chain may contain:
            - Other filters
            - The actual microservices call
          Without chain, request goes nowhere.

-- chain.filter(exchange): Means - I'm done with my work, now pass this request to the next filter or the
                           microservice.


-- Mono: Mono represents an asynchronous operation that will complete later.
         Mono<Void>: This filter doesn't return data, it only signals when processing is finished.


--Quick Analogy :

-Filter -> Security Guard
-exchange -> Visitor file(name, ID, purpose)
-chain -> Door to next room

* If guard doesn't open the door -> visitor is blocked.

* */
