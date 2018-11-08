package com.jfilter.filter;

import com.jfilter.annotation.FilterField;
import com.jfilter.annotation.FilterFieldOperatorType;
import com.jfilter.annotation.FilterFieldPredicateType;
import com.jfilter.model.QPerson;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.Data;

@Data
public class PersonFilter extends BaseFilter {

    public static final OrderSpecifier[] ORDER_BY = new OrderSpecifier[]{QPerson.person.name.asc()};

    @FilterField(name = "name", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.STRING_CONTAINS_IC)
    private String name;


    @Override
    public EntityPathBase getEntityPathBase() {
        return QPerson.person;
    }

    @Override
    public OrderSpecifier[] getOrderBy() {
        return ORDER_BY;
    }
}
