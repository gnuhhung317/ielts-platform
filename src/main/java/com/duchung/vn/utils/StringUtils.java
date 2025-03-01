package com.duchung.vn.utils;

import java.text.Normalizer;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Random RANDOM = new Random();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Pattern SPECIAL_CHARS = Pattern.compile("[^\\p{L}\\p{N}]");
    private static final Pattern NORMALIZE_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateRandomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    public static String toSlug(String input) {
        if (isEmpty(input)) {
            return "";
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = NORMALIZE_PATTERN.matcher(normalized).replaceAll("");
        normalized = normalized.toLowerCase();
        normalized = SPECIAL_CHARS.matcher(normalized).replaceAll("-");
        normalized = normalized.replaceAll("\\s+", "-");
        normalized = normalized.replaceAll("-+", "-");
        normalized = normalized.replaceAll("^-|-$", "");

        return normalized;
    }

    public static String maskEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        String name = parts[0];
        String domain = parts[1];

        String maskedName;
        if (name.length() <= 2) {
            maskedName = name;
        } else {
            maskedName = name.substring(0, 2) + "*".repeat(name.length() - 2);
        }

        return maskedName + "@" + domain;
    }

    public static String maskPhone(String phone) {
        if (isEmpty(phone) || phone.length() < 8) {
            return phone;
        }

        int length = phone.length();
        int visibleDigits = 4;
        int maskedLength = length - visibleDigits;

        return "*".repeat(maskedLength) + phone.substring(maskedLength);
    }

    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }

        if (str.length() <= maxLength) {
            return str;
        }

        return str.substring(0, maxLength - 3) + "...";
    }

    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}