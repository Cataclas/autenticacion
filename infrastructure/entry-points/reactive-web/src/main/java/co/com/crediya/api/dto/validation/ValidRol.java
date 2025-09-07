package co.com.crediya.api.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RolValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRol {
    String message() default "El rol debe ser: ADMINISTRADOR, ASESOR o SOLICITANTE";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}