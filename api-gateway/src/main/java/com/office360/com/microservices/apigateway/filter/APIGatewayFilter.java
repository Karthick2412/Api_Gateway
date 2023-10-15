package com.office360.com.microservices.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.office360.com.microservices.apigateway.util.JwtUtil;

@Component
public class APIGatewayFilter extends AbstractGatewayFilterFactory<APIGatewayFilter.Config>{

	
	public APIGatewayFilter() {
        super(Config.class);
    }
	
	public static class Config {

    }

	@Autowired
	private RoutesValidator validator;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    jwtUtil.validateToken(authHeader);

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
	}

}
