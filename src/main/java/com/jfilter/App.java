package com.jfilter;


import com.jfilter.filter.PersonFilter;
import com.jfilter.filter.SearchResultData;
import com.jfilter.model.Person;
import com.jfilter.service.PersonService;

import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        PersonService personService = new PersonService();

        PersonFilter personFilter = new PersonFilter();

        //first batch
        SearchResultData<Person> paginated = personService.findByFilterPaginated(personFilter);
        paginated.getResultData().stream().forEach(p -> System.out.println(p.getName()));

        System.out.println("-------");

        //second batch now with 30 itens
        personFilter.setPage(2);
        personFilter.setCount(30);
        paginated = personService.findByFilterPaginated(personFilter);
        paginated.getResultData().stream().forEach(p -> System.out.println(p.getName()));

    }

    private static void findAll(PersonService personService) {
        List<Person> all = personService.findAll();
        all.stream().forEach(person -> System.out.println(person.getName()));
    }
}
