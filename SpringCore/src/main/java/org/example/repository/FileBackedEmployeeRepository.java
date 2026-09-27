package org.example.repository;

import org.example.model.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Profile("prod")
public class FileBackedEmployeeRepository implements EmployeeRepository {

    private static final String FILE_NAME = "employees.txt";

    private final Map<Long, Employee> employees = new HashMap<>();

    public FileBackedEmployeeRepository() {
        loadFromFile();
    }

    @Override
    public void save(Employee employee) {
        employees.put(employee.getId(),employee);
        saveToFile();
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

    private void loadFromFile() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                Employee employee = new Employee();

                employee.setId(Long.parseLong(parts[0]));
                employee.setName(parts[1]);
                employee.setDepartment(parts[2]);
                employee.setSalary(Double.parseDouble(parts[3]));

                employees.put(employee.getId(), employee);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load employees", e);
        }
    }

    private void saveToFile() {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(FILE_NAME))) {

            for (Employee employee : employees.values()) {

                writer.write(
                        employee.getId() + "," +
                                employee.getName() + "," +
                                employee.getDepartment() + "," +
                                employee.getSalary()
                );

                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to save employees", e);
        }
    }
}