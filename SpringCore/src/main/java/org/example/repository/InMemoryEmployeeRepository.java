package org.example.repository;

import org.example.model.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Profile("dev")
public class InMemoryEmployeeRepository implements EmployeeRepository {

    HashMap<Long, Employee> employees;
    InMemoryEmployeeRepository(HashMap<Long,Employee> employees){
        this.employees = employees;
    }

    @Override
    public void save(Employee e) {
        employees.put(e.getId(),e);
    }

    @Override
    public void delete(long id) {
        employees.remove(id);
    }

    @Override
    public Employee findById(long id) {
        return employees.get(id);
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }
}
