package com.iteletric.iteletricapi.config.validation.customvalidation.optionalnotblanck;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OptionalNotBlankValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalNotBlank {
    String message() default "O campo não pode estar em branco";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
