package com.jfilter.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;


@Entity
@Data
public class Person extends ModelBase {

    @Column(nullable = false, length = 50)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return active == person.active &&
                Objects.equals(id, person.id) &&
                Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, active, name);
    }
}
