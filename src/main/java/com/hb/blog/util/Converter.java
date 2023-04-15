package com.hb.blog.util;

public class Converter {
    public static String camelCaseToSnakeCase(String input) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return input.replaceAll(regex, replacement).toLowerCase();
    }
}
