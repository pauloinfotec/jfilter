package com.jfilter.service;

import com.jfilter.model.Person;
import com.jfilter.model.QPerson;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class PersonService {

    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("TestPersistence");


    public List<Person> findAll(){
        EntityManager em = factory.createEntityManager();

        QPerson qPerson = QPerson.person;
        return new JPAQueryFactory(em).selectFrom(qPerson).fetch();
    }

}
