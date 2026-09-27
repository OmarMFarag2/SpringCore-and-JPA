package org.example;
import org.example.config.AppConfig;
import org.example.model.Employee;
import org.example.service.EmployeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    static void main() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();

        context.getEnvironment().setActiveProfiles("dev");

        context.register(AppConfig.class);

        context.refresh();

        EmployeeService employeeService =
                context.getBean(EmployeeService.class);

        Employee employee = new Employee();

        employee.setName("Omar");
        employee.setDepartment("IT");
        employee.setSalary(15000);

        employeeService.addEmployee(employee);

        System.out.println(employeeService.getAllEmployees());

        context.close();
    }
}
