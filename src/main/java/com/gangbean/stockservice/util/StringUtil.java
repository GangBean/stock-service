package com.gangbean.stockservice.util;

public class StringUtil {

    public static String paddLeftWith(String origin, String delimiter, int length) {
        StringBuilder sb = new StringBuilder();
        int left = length - origin.length();
        for (int i = 0 ; i < left; i++) {
            sb.append(delimiter);
        }
        sb.append(origin);
        return sb.substring(0, length);
    }

    public static String paddLeftWith(String origin, int length) {
        return paddLeftWith(origin, " ", length);
    }
}
