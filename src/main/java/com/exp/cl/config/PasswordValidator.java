package com.exp.cl.config;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordValidator {

    private final PasswordProperties properties;

    public boolean isValid(String password) {
        if (password == null) return false;

        if (password.length() < properties.getMinLength() ||
            password.length() > properties.getMaxLength()) {
            return false;
        }

        if (properties.isRequireUppercase() && !password.matches(".*[A-Z].*")) return false;
        if (properties.isRequireLowercase() && !password.matches(".*[a-z].*")) return false;
        if (properties.isRequireDigit() && !password.matches(".*\\d.*")) return false;
        if (properties.isRequireSpecial() && !password.matches(".*[^a-zA-Z0-9].*")) return false;

        return true;
    }
}