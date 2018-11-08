package com.jfilter.service;


import com.jfilter.annotation.FilterField;
import com.jfilter.annotation.FilterFieldOperatorType;
import com.jfilter.annotation.FilterFieldPredicateType;
import com.jfilter.filter.BaseFilter;
import com.jfilter.filter.SearchResultData;
import com.jfilter.model.ModelBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.util.ReflectionUtils;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BaseService<T extends ModelBase> implements Serializable {

    protected static EntityManagerFactory factory = Persistence.createEntityManagerFactory("TestPersistence");

    EntityManager em = factory.createEntityManager(); //TODO: just for test purposes

    public T save(T model) {
        beforeSave(model);
        if (model.getId() == null) {
            em.persist(model);
        } else {
            model = em.merge(model);
        }
        return model;
    }

    public T findById(Class<T> modelClass, Long id) {
        return em.find(modelClass, id);
    }

    public SearchResultData findByFilterPaginated(BaseFilter filter) {
        try {
            Long count = countByFilter(filter);
            List registros = findByFilter(filter);
            return new SearchResultData(count, registros);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Long countByFilter(BaseFilter baseFilter) throws Exception {
        JPQLQuery query = new JPAQuery(em);
        final JPQLQuery from = query.from(baseFilter.getEntityPathBase());
        from.where(generatePredicatesFromFilter(baseFilter));
        setCustomFilter(baseFilter, from);
        setPagination(baseFilter, from);
        return from.fetchCount();
    }

    @SuppressWarnings("unchecked")
    public List<ModelBase> findByFilter(BaseFilter baseFilter) {
        final JPQLQuery from = constructQuery(baseFilter);
        return from.fetch();
    }


    protected JPQLQuery constructQuery(BaseFilter baseFilter) {
        JPQLQuery query = new JPAQuery(em);
        final JPQLQuery from = query.from(baseFilter.getEntityPathBase());
        from.where(generatePredicatesFromFilter(baseFilter));
        setCustomFilter(baseFilter, from);
        setPagination(baseFilter, from);
        if (baseFilter.getOrderBy() != null) {
            from.orderBy(baseFilter.getOrderBy());
        }
        return from;
    }

    private void setPagination(BaseFilter baseFilter, JPQLQuery from) {
        if (baseFilter.getCount() != null) {
            from.offset(baseFilter.getFirstResult());
            from.limit(baseFilter.getCount());
        }
    }

    private void setCustomFilter(BaseFilter baseFilter, JPQLQuery from) {
        final BooleanBuilder customBuilder = baseFilter.getCustomBuilder();
        if (customBuilder != null) {
            from.where(customBuilder);
        }
    }

    public void beforeSave(T model) {
    }

    protected static BooleanBuilder generatePredicatesFromFilter(BaseFilter filter) {
        Set<Field> allFields = ReflectionUtils.getFields(filter.getClass());
        BooleanBuilder where = new BooleanBuilder();
        for (Field field : allFields) {
            try {
                field.setAccessible(Boolean.TRUE);
                Object fieldValue = field.get(filter);
                boolean isCollection = Collection.class.isAssignableFrom(field.getType());
                if (fieldValue != null
                        && (!isCollection || (isCollection && !CollectionUtils.isEmpty((Collection) fieldValue)))) {
                    FilterField filterFieldAnnotation = field.getAnnotation(FilterField.class);
                    boolean hasFilterFieldAnnotation = filterFieldAnnotation != null;
                    if (hasFilterFieldAnnotation) {
                        generatePredicateFromAnnotatedField(field, filterFieldAnnotation, fieldValue, where, filter);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("BaseService - Error when generating predicates...");
            }
        }
        return where;
    }

    private static void generatePredicateFromAnnotatedField(Field field, FilterField filterFieldAnnotation,
                                                            Object fieldValue, BooleanBuilder where, BaseFilter filter) {

        final String fieldName = filterFieldAnnotation.name();
        final FilterFieldPredicateType type = filterFieldAnnotation.type();
        final FilterFieldOperatorType operator = filterFieldAnnotation.operator();
        final Path path = Expressions.path(field.getType(), filter.getEntityPathBase(), fieldName);
        BooleanExpression predicate = null;

        if (fieldValue instanceof LocalDateTime) {
            if (filterFieldAnnotation.setFirstTimeOfDay()) {
                fieldValue = ((LocalDateTime) fieldValue).toLocalDate().atTime(LocalTime.MAX);
            } else if (filterFieldAnnotation.setLastTimeOfDay()) {
                fieldValue = ((LocalDateTime) fieldValue).toLocalDate().atTime(LocalTime.MIN);
                ;
            }
        }

        switch (type) {
            case EQUAL:
                predicate = Expressions.predicate(Ops.EQ, path, Expressions.constant(fieldValue));
                break;
            case NOT_EQUAL:
                predicate = Expressions.predicate(Ops.NE, path, Expressions.constant(fieldValue));
                break;
            case LIKE:
                predicate = Expressions.predicate(Ops.LIKE, path, Expressions.constant(fieldValue.toString() + "%"));
                break;
            case GREATER_THAN_EQUAL:
                predicate = Expressions.predicate(Ops.GOE, path, Expressions.constant(fieldValue));
                break;
            case LESS_THAN_EQUAL:
                predicate = Expressions.predicate(Ops.LOE, path, Expressions.constant(fieldValue));
                break;
            case HAS:
                predicate = Expressions.predicate(Ops.IN, Expressions.constant(fieldValue), path);
                break;
            case IN:
                predicate = Expressions.predicate(Ops.IN, path, Expressions.constant(fieldValue));
                break;
            case STRING_CONTAINS_IC:
                predicate = Expressions.predicate(Ops.STRING_CONTAINS_IC, path, Expressions.constant(fieldValue));
                break;
            case IS_NOT_NULL:
                predicate = Expressions.predicate(Ops.IS_NOT_NULL, path, Expressions.constant(fieldValue));
                break;
            case IS_NULL:
                predicate = Expressions.predicate(Ops.IS_NULL, path, Expressions.constant(fieldValue));
                break;
            default:
                break;
        }
        if (operator == FilterFieldOperatorType.AND) {
            where.and(predicate);
        } else {
            where.or(predicate);
        }
    }


    public JPQLQuery instanceJPQLQuery() {
        return new JPAQuery(em);
    }

}