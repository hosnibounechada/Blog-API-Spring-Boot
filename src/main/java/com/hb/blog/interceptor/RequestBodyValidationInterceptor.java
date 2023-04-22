package com.hb.blog.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hb.blog.payload.request.BaseRequestDTO;
import com.hb.blog.payload.request.user.CreateUserRequest;
import com.hb.blog.payload.request.user.UpdateUserRequest;
import com.hb.blog.validator.ObjectValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.stream.Collectors;

import static com.hb.blog.constant.HttpMethod.POST;
import static com.hb.blog.constant.HttpMethod.PUT;

@Component
public class RequestBodyValidationInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper;

    public RequestBodyValidationInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod().toLowerCase();
        String uri = request.getRequestURI();

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        BaseRequestDTO requestDTO = null;
        if (uri.startsWith("/api/v1/users")) {
                switch (method) {
                    case POST -> requestDTO = objectMapper.readValue(requestBody, CreateUserRequest.class);
                    case PUT -> requestDTO = objectMapper.readValue(requestBody, UpdateUserRequest.class);
                }
                ObjectValidator.validate(requestDTO);
        }

        /*if(uri.startsWith("/api/v1/users")){
            Class<? extends BaseRequestDTO> dtoClass = getRequestDtoClass(method);
            if (dtoClass != null) {
                BaseRequestDTO requestDTO = objectMapper.readValue(request.getReader(), dtoClass);
                ObjectValidator.validate(requestDTO);
            }
        }*/

        return true; // Or false to stop processing the request
    }

    private Class<? extends BaseRequestDTO> getRequestDtoClass(String method) {
        return switch (method) {
            case POST -> CreateUserRequest.class;
            case PUT -> UpdateUserRequest.class;
            default -> null;
        };
    }
}
