package com.jfilter.annotation;


import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;

public enum FilterFieldPredicateType {

    EQUAL(Ops.EQ),
    NOT_EQUAL(Ops.NE),
    LIKE(Ops.LIKE),
    GREATER_THAN_EQUAL(Ops.GT),
    LESS_THAN_EQUAL(Ops.LT),
    HAS(Ops.IN),
    IN(Ops.IN),
    STRING_CONTAINS_IC(Ops.STRING_CONTAINS_IC),
    IS_NOT_NULL(Ops.IS_NOT_NULL),
    IS_NULL(Ops.IS_NULL);

    private Operator operator;

    FilterFieldPredicateType(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }
}
