package storage.employee;

import employee.Employee;

import java.util.HashMap;

public interface EmployeeStorageRepository {
    public boolean store(HashMap<String, Employee> employeeMap, HashMap<String, String> worksUnderMap);
    public HashMap[] load();
}