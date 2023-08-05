package com.code.auditor.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context){
        return Pattern.compile(EMAIL_PATTERN)
                .matcher(email)
                .matches();
    }
}