package com.foodapp.foodapp.common;

public class CommonUtils {
    public static String COMPANY_DEFAULT_PRODUCT_IMG_URL = "_DEFAULT_PRODUCT";
    public static String extractNumbers(String input) {
        if (input != null) {
            return input.replaceAll("[^0-9]", ""); // Replace all non-numeric characters with an empty string
        }
        return ""; // Return an empty string if input is null
    }

    public static String createDefaultProductImgUrl(final String value) {
        return "images/" +  value + COMPANY_DEFAULT_PRODUCT_IMG_URL + ".png";
    }
}
