package org.example.entity;

import jakarta.persistence.Entity;

@Entity
public class Employee extends Person {

    private String position;

    public Employee() {
    }

    public Employee(String name, String position) {
        super(name);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}