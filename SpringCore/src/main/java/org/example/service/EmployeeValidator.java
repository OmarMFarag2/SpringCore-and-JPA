package org.example.service;
import org.example.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator {

    public void validate(Employee employee) {

        if (employee.getName() == null ||
                employee.getName().isBlank()) {

            throw new InvalidEmployeeException(
                    "Employee name cannot be blank"
            );
        }

        if (employee.getSalary() < 0) {

            throw new InvalidEmployeeException(
                    "Employee salary cannot be negative"
            );
        }
    }
}