package Day7;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    int id;
    String name;
    double salary;
    String department;

    public Employee(int id, String name, double salary, String department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Salary: " + salary + ", Department: " + department;
    }
}

public class EmployeeDatabase {
    private static final String FILE_NAME = "employees.dat";

    public static void main(String[] args) {
        try {
            List<Employee> employees = loadEmployees();
            
            addEmployee(employees, new Employee(101, "Alice", 55000, "HR"));
            addEmployee(employees, new Employee(102, "Bob", 60000, "IT"));
            
            searchEmployee(employees, 101);
            updateSalary(employees, 102, 65000);
            deleteEmployee(employees, 101);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static List<Employee> loadEmployees() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Employee>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load employees: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void saveEmployees(List<Employee> employees) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(employees);
        } catch (IOException e) {
            System.err.println("Failed to save employees: " + e.getMessage());
        }
    }

    private static void addEmployee(List<Employee> employees, Employee employee) {
        employees.add(employee);
        saveEmployees(employees);
        System.out.println("Employee added: " + employee);
    }

    private static void searchEmployee(List<Employee> employees, int id) {
        for (Employee emp : employees) {
            if (emp.id == id) {
                System.out.println("Employee found: " + emp);
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    private static void updateSalary(List<Employee> employees, int id, double newSalary) {
        for (Employee emp : employees) {
            if (emp.id == id) {
                emp.salary = newSalary;
                saveEmployees(employees);
                System.out.println("Salary updated: " + emp);
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    private static void deleteEmployee(List<Employee> employees, int id) {
        employees.removeIf(emp -> emp.id == id);
        saveEmployees(employees);
        System.out.println("Employee deleted.");
    }
}
