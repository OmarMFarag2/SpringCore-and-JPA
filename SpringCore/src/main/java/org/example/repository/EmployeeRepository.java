package org.example.repository;

import org.example.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    public void save(Employee e);
    public void delete(long id);
    public Employee findById(long id);
    public List<Employee> findAll();
}
