package com.office360.com.microservices.apigateway.filter;



import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RoutesValidator {
	
	public static final List<String> openApiEndpoints = List.of(
            "/users/create",
            "/users/login",
            "/eureka",
            "/users/newRole"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
