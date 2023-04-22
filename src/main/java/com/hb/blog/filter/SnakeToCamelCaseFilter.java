package com.hb.blog.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SnakeToCamelCaseFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        Map<String, String[]> paramMap = new HashMap<>(req.getParameterMap());

        Map<String, String[]> convertedParamMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String camelCaseKey = toCamelCase(entry.getKey());
            String[] lowerCaseValues = toLowerCase(entry.getValue());

            convertedParamMap.put(camelCaseKey, lowerCaseValues);
        }
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(req) {
            @Override
            public Map<String, String[]> getParameterMap() {
                return convertedParamMap;
            }

            @Override
            public String[] getParameterValues(String name) {
                return convertedParamMap.get(name);
            }

            @Override
            public String getParameter(String name) {
                String[] values = convertedParamMap.get(name);
                if (values != null && values.length > 0) {
                    return values[0];
                } else {
                    return null;
                }
            }
        };

        chain.doFilter(requestWrapper, response);
    }

    private String toCamelCase(String snakeCase) {
        StringBuilder camelCase = new StringBuilder();
        boolean capitalizeNext = false;
        for (int i = 0; i < snakeCase.length(); i++) {
            char c = snakeCase.charAt(i);
            if (c == '_') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                camelCase.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                camelCase.append(c);
            }
        }
        return camelCase.toString();
    }
    private String[] toLowerCase(String[] values){
        String[] convertedValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            convertedValues[i] = values[i].toLowerCase();
        }
        return convertedValues;
    }
}
