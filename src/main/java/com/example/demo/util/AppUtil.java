package com.example.demo.util;

public class AppUtil {

    public static String formatCronField(String fieldName, Object fieldValue) {
        return String.format("%-14s%s%n", fieldName, fieldValue);
    }
}
