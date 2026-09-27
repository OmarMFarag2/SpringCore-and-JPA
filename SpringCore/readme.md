# Employee Manager

A Spring Core application demonstrating dependency injection, component scanning, profiles, collection injection, bean scopes, lifecycle callbacks, externalized configuration, validation, and repository abstraction.

## Technologies

- Java
- Spring Framework / Spring Core
- Maven
- HashMap
- File I/O
- `AtomicLong`

## Project Structure

```text
src/
└── main/
    ├── java/
    │   └── org.example/
    │       ├── config/
    │       │   └── AppConfig.java
    │       │
    │       ├── model/
    │       │   └── Employee.java
    │       │
    │       ├── repository/
    │       │   ├── EmployeeRepository.java
    │       │   ├── InMemoryEmployeeRepository.java
    │       │   └── FileBackedEmployeeRepository.java
    │       │
    │       ├── service/
    │       │   ├── EmployeeService.java
    │       │   ├── EmployeeServiceImpl.java
    │       │   ├── EmployeeValidator.java
    │       │   └── InvalidEmployeeException.java
    │       │
    │       ├── notify/
    │       │   ├── Notifier.java
    │       │   ├── EmailNotifier.java
    │       │   ├── SmsNotifier.java
    │       │   ├── PushNotifier.java
    │       │   └── NotificationManager.java
    │       │
    │       ├── audit/
    │       │   └── AuditLogger.java
    │       │
    │       └── Main.java
    │
    └── resources/
        └── application.properties
```

## Features

### 1. Employee Domain and Repository Layer

The application contains an `Employee` model with:

- `id`
- `name`
- `department`
- `salary`

The repository abstraction is defined by:

```java
EmployeeRepository
```

with operations for:

- Saving employees
- Finding an employee by ID
- Finding all employees

Two implementations are provided:

```text
InMemoryEmployeeRepository
FileBackedEmployeeRepository
```

The in-memory repository stores employees in a:

```java
HashMap<Long, Employee>
```

The file-backed repository also uses a `HashMap<Long, Employee>` during runtime and persists the employees to `employees.txt`.

---

## 2. Service Layer

The service layer is represented by:

```text
EmployeeService
EmployeeServiceImpl
```

`EmployeeServiceImpl` does not instantiate its dependencies using `new`.

Instead, dependencies are injected through the constructor:

```java
public EmployeeServiceImpl(
        EmployeeRepository employeeRepository,
        EmployeeValidator employeeValidator,
        NotificationManager notificationManager)
```

The service provides:

- `addEmployee()`
- `getEmployeeById()`
- `getAllEmployees()`
- `giveRaise()`

Employee IDs are generated using:

```java
AtomicLong
```

The service also contains the application's business logic for employee raises.

---

## 3. Spring Configuration

`AppConfig` is the main Spring configuration class.

It uses:

```java
@Configuration
@ComponentScan("org.example")
@PropertySource("classpath:application.properties")
```

`@ComponentScan` allows Spring to automatically discover components annotated with:

- `@Service`
- `@Repository`
- `@Component`

Constructor injection is used throughout the application.

---

## 4. Profiles

The application provides two repository implementations.

### Development profile

```java
@Repository
@Profile("dev")
public class InMemoryEmployeeRepository
```

The `dev` profile stores data only in memory.

### Production profile

```java
@Repository
@Profile("prod")
public class FileBackedEmployeeRepository
```

The `prod` profile stores employees in memory during execution and persists them to:

```text
employees.txt
```

The active profile is selected in `Main` before the Spring context is refreshed:

```java
context.getEnvironment().setActiveProfiles("dev");
```

Changing it to:

```java
context.getEnvironment().setActiveProfiles("prod");
```

switches the repository implementation without changing `EmployeeServiceImpl`.

This demonstrates dependency inversion and Spring profile-based configuration.

---

## 5. Notification System

The application contains a `Notifier` interface with three implementations:

```text
EmailNotifier
SmsNotifier
PushNotifier
```

Each implementation is a Spring component.

The notification implementations use `@Order`:

```java
@Order(1)
EmailNotifier
```

```java
@Order(2)
SmsNotifier
```

```java
@Order(3)
PushNotifier
```

`NotificationManager` uses collection injection:

```java
private final List<Notifier> notifiers;
```

Spring automatically injects all available `Notifier` beans into the list.

`NotificationManager` then iterates over the list and calls:

```java
notifier.send(message);
```

The result is a predictable notification order:

```text
EmailNotifier
SmsNotifier
PushNotifier
```

Notifications are triggered when:

- A new employee is added
- An employee receives a raise

---

## 6. Validation

`EmployeeValidator` is a Spring component and is injected into `EmployeeServiceImpl`.

Before an employee is saved, the service validates the employee.

The validator rejects:

- Blank employee names
- Negative salaries

Invalid data causes:

```java
InvalidEmployeeException
```

to be thrown.

The exception is handled in `Main` so that validation failures do not terminate the application.

---

## 7. Bean Scopes

Most application components use Spring's default scope:

```text
singleton
```

Examples include:

- `EmployeeServiceImpl`
- `EmployeeValidator`
- `NotificationManager`
- Repository implementations
- Notifier implementations

A singleton bean has one instance managed by the Spring container for the lifetime of the application context.

### Prototype AuditLogger

`AuditLogger` uses:

```java
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
```

A prototype bean is created every time Spring is asked for a new instance.

The application demonstrates this by requesting multiple `AuditLogger` instances and printing their identity information.

---

## 8. Scoped-Bean Problem

`EmployeeServiceImpl` is a singleton, while `AuditLogger` is a prototype.

Directly injecting a prototype bean into the singleton would not give the service a new logger on every method call.

To solve this, the service uses:

```java
ObjectProvider<AuditLogger>
```

Instead of injecting the actual prototype instance, Spring injects an `ObjectProvider`.

When a new audit logger is needed:

```java
auditLoggerProvider.getObject()
```

is called.

Spring then creates a fresh `AuditLogger` instance.

This allows the singleton service to obtain a new prototype object whenever required.

---

## 9. Bean Lifecycle

The application demonstrates Spring bean lifecycle callbacks.

`@PostConstruct` is used to print a message when a bean is initialized.

`@PreDestroy` is used to print a message when a bean is destroyed.

The application explicitly closes the Spring context:

```java
context.close();
```

This triggers the destruction callbacks for applicable singleton beans.

Prototype beans are managed differently by Spring: after creation, their destruction lifecycle is generally not managed automatically by the container.

---

## 10. Externalized Configuration

Application configuration is stored in:

```text
src/main/resources/application.properties
```

Example:

```properties
company.name=Training Company
company.currency=EGP
notification.retry-count=3
raise.max-percentage=20
```

The values are loaded using:

```java
@PropertySource("classpath:application.properties")
```

and injected using:

```java
@Value
```

For example:

```java
@Value("${raise.max-percentage}")
private double maxRaisePercentage;
```

The service rejects raise requests that exceed the configured maximum percentage.

This keeps configurable values outside the Java source code.

---

## 11. Dependency Injection

The project primarily uses **constructor injection**.

For example:

```java
public EmployeeServiceImpl(
        EmployeeRepository employeeRepository,
        EmployeeValidator employeeValidator,
        NotificationManager notificationManager)
```

Constructor injection was chosen because the dependencies are required for the service to function.

Collection injection is used specifically for the notification system:

```java
public NotificationManager(List<Notifier> notifiers)
```

This allows Spring to automatically provide all `Notifier` implementations.

`ObjectProvider` is used for the prototype `AuditLogger` because the singleton service needs a fresh instance whenever it performs an operation that requires auditing.

---

## 12. Main Application Flow

`Main` demonstrates the application's features in the following order:

1. Set the active Spring profile.
2. Create and configure the Spring application context.
3. Retrieve `EmployeeService` from the container.
4. Add a valid employee.
5. Validate the employee.
6. Generate an employee ID.
7. Save the employee.
8. Trigger all three notification implementations.
9. Attempt to add an invalid employee.
10. Handle the validation exception.
11. Give an employee a valid raise.
12. Trigger the raise notification.
13. Attempt a raise above the configured maximum.
14. Demonstrate prototype `AuditLogger` instances.
15. List all employees.
16. Display externalized configuration values.
17. Close the application context.
18. Display bean destruction callbacks.

---

## 13. Example Console Output

A successful execution produces output similar to:

```text
EmployeeValidator initialized

EmailNotifier: New employee added: Omar
SmsNotifier: New employee added: Omar
PushNotifier: New employee added: Omar

Invalid employee:
Employee name cannot be blank

Employee 1 received a 10.0% raise

EmailNotifier: Employee Omar received a raise
SmsNotifier: Employee Omar received a raise
PushNotifier: Employee Omar received a raise

Raise rejected:
Raise percentage cannot exceed 20.0%

AuditLogger instance: 12345678
AuditLogger instance: 87654321

Are audit logger instances different? true

Employees:
Employee{id=1, name='Omar', department='IT', salary=16500.0}

Company: Training Company
Currency: EGP
Notification retry count: 3
Maximum raise percentage: 20.0

EmployeeService destroyed
NotificationManager destroyed
EmployeeValidator destroyed
```

The exact output and object identity values may vary between executions.

---

## 14. Running the Application

Make sure the project is using the configured Java version and Maven dependencies.

Build the project:

```bash
mvn clean compile
```

Then run `Main`.

To test the in-memory repository:

```java
context.getEnvironment().setActiveProfiles("dev");
```

To test the file-backed repository:

```java
context.getEnvironment().setActiveProfiles("prod");
```

When using the production profile, employee data is persisted to:

```text
employees.txt
```

---

## Key Spring Core Concepts Demonstrated

This project demonstrates:

- Dependency Injection
- Constructor Injection
- Inversion of Control
- Component Scanning
- `@Configuration`
- `@Bean`
- `@Component`
- `@Service`
- `@Repository`
- `@Profile`
- Collection Injection
- `@Order`
- Singleton Scope
- Prototype Scope
- `ObjectProvider`
- Bean Lifecycle
- `@PostConstruct`
- `@PreDestroy`
- `@PropertySource`
- `@Value`
- Custom exceptions
- Repository abstraction
- Business logic in the service layer
- Externalized configuration