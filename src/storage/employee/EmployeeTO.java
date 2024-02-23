package storage.employee;

import employee.Employee;

import java.io.Serializable;

public class EmployeeTO implements Serializable {
    private final String id;
    private final String worksUnder;
    private final Employee employee;

    public EmployeeTO(String id, String worksUnder, Employee employee) {
        this.id = id;
        this.worksUnder = worksUnder;
        this.employee = employee;
    }

    public String getId() {
        return id;
    }

    public String getWorksUnder() {
        return worksUnder;
    }

    public Employee getEmployee() {
        return employee;
    }
}
