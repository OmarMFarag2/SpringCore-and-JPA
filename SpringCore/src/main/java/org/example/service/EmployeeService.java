package org.example.service;

import org.example.model.Employee;

import java.util.HashMap;
import java.util.List;

public interface EmployeeService {
    public void addEmployee(Employee e);
    public Employee getEmployeeById(long id);
    public List<Employee> getAllEmployees();
    public void fireEmployee(long id);
}
