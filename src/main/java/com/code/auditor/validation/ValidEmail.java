package com.code.auditor.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidEmail {
    String message() default "Невалиден имейл";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}