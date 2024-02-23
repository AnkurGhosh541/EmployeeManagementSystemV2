package storage.employee;

import employee.Employee;

import java.util.HashMap;

public class EmployeeRepository extends BaseRepository {
    @Override
    public EmployeeStorageRepository getRepository(String type) {
        if (type.equalsIgnoreCase("file")) {
            repository = new FileStorage();
        } else if(type.equalsIgnoreCase("database")) {
            repository = new DatabaseStorage();
        }

        return repository;
    }

    public boolean save(HashMap<String, Employee> employeeMap, HashMap<String, String> worksUnderMap) {
        return repository.store(employeeMap, worksUnderMap);
    }

    public HashMap[] read() {
        return repository.load();
    }
}
