package com.jfilter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterField {

    String name();

    FilterFieldPredicateType type();

    FilterFieldOperatorType operator();

    boolean setFirstTimeOfDay() default false;

    boolean setLastTimeOfDay() default false;

}
