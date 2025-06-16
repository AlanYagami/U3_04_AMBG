package mx.edu.utez.actividad3_4.service;

import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

// Implementa principio de Responsabilidad Única (SRP) - SOLID
// Implementa Input Validation - Principio de Seguridad

@Service
public class ValidationService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10,15}$");

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public boolean isValidString(String str, int minLength, int maxLength) {
        return str != null &&
                str.trim().length() >= minLength &&
                str.trim().length() <= maxLength &&
                !containsSpecialChars(str);
    }

    private boolean containsSpecialChars(String str) {
        // Previene algunos ataques de injection básicos
        String[] blacklist = {"<", ">", "'", "\"", ";", "--", "/*", "*/", "script"};
        String lowerStr = str.toLowerCase();
        for (String item : blacklist) {
            if (lowerStr.contains(item)) {
                return true;
            }
        }
        return false;
    }
}