package com.jfilter.filter;

import com.jfilter.annotation.FilterField;
import com.jfilter.annotation.FilterFieldOperatorType;
import com.jfilter.annotation.FilterFieldPredicateType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseFilter implements Serializable {

    private Integer page = 1;
    private Integer count = 10;

    @FilterField(name = "active", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    protected Boolean active = true;

    public Integer getFirstResult() {
        if (page != null && count != null) {
            return (page - 1) * count;
        }
        return null;
    }

    public abstract EntityPathBase getEntityPathBase();

    public OrderSpecifier[] getOrderBy() {
        return null;
    }

    public BooleanBuilder getCustomBuilder() {
        return null;
    }

}