package co.com.crediya.api.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class RolValidator implements ConstraintValidator<ValidRol, String> {
    
    private static final List<String> ROLES_VALIDOS = Arrays.asList("ADMINISTRADOR", "ASESOR", "SOLICITANTE");
    
    @Override
    public boolean isValid(String rol, ConstraintValidatorContext context) {
        return rol != null && ROLES_VALIDOS.contains(rol.toUpperCase());
    }
}