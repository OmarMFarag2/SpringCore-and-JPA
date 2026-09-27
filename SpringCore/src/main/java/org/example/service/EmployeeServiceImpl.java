package org.example.service;
import org.example.Notify.NotificationManager;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeValidator employeeValidator;
    private final NotificationManager notificationManager;

    private final AtomicLong idGenerator = new AtomicLong(1);

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeValidator employeeValidator,
            NotificationManager notificationManager) {

        this.employeeRepository = employeeRepository;
        this.employeeValidator = employeeValidator;
        this.notificationManager = notificationManager;
    }

    @Override
    public void addEmployee(Employee employee) {

        employeeValidator.validate(employee);

        employee.setId(idGenerator.getAndIncrement());

        employeeRepository.save(employee);

        notificationManager.notifyAll(
                "New employee added: " + employee.getName()
        );
    }

    @Override
    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void fireEmployee(long id) {
        employeeRepository.delete(id);
    }
}