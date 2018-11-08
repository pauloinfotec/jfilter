package com.jfilter.service;

import com.jfilter.model.Person;
import com.jfilter.model.QPerson;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class PersonService extends BaseService<Person> {




    public List<Person> findAll(){
        EntityManager em = factory.createEntityManager();

        QPerson qPerson = QPerson.person;
        return new JPAQueryFactory(em).selectFrom(qPerson).fetch();
    }

}
