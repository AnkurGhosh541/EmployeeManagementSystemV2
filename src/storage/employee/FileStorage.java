package storage.employee;

import employee.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileStorage implements EmployeeStorageRepository {
    @Override
    public boolean store(HashMap<String, Employee> employeeMap, HashMap<String, String> worksUnderMap) {
        ArrayList<EmployeeTO> employeeTOList = new ArrayList<>();

        for (Map.Entry<String, Employee> stringEmployeeEntry : employeeMap.entrySet()) {
            String id = stringEmployeeEntry.getKey();
            Employee employee = stringEmployeeEntry.getValue();
            String worksUnder = worksUnderMap.get(id);
            EmployeeTO employeeTO = new EmployeeTO(id, worksUnder, employee);
            employeeTOList.add(employeeTO);
        }

        return EmployeeDAO.getInstance().storeFile(employeeTOList.toArray(new EmployeeTO[0]));
    }


    @Override
    public HashMap[] load() {
        HashMap<String, Employee> employeeMap = new HashMap<>();
        HashMap<String, String> worksUnderMap = new HashMap<>();

        EmployeeTO[] employeeTOS = EmployeeDAO.getInstance().loadFile();

        if(employeeTOS == null)
            return null;

        for (EmployeeTO employeeTO : employeeTOS) {
            employeeMap.put(employeeTO.getId(), employeeTO.getEmployee());
            worksUnderMap.put(employeeTO.getId(), employeeTO.getWorksUnder());
        }

        return new HashMap[] {employeeMap, worksUnderMap};
    }
}
