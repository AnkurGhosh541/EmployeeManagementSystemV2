package storage.employee;

import employee.Employee;
import employee.Leader;
import employee.Worker;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeDAO {
    private static EmployeeDAO employeeDAO = null;
    private final String url = "jdbc:mysql://localhost:3306/";
    private final String dbName = "ems";
    private final String userName = "root";
    private final String password = "root";

    private EmployeeDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        createDatabase();
        createSQLTable();
    }

    public static EmployeeDAO getInstance() {
        if(employeeDAO == null) {
            employeeDAO = new EmployeeDAO();
        }
        return employeeDAO;
    }

    private void createDatabase() {
        try (Connection connection = DriverManager.getConnection(url, userName, password)) {
            String query = "create database if not exists " + dbName + ";";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createSQLTable() {
        try (Connection connection = DriverManager.getConnection(url + dbName, userName, password)) {

            String query = "create table if not exists employees( `id` varchar(50) NOT NULL, `name` varchar(100) NOT NULL, `designation` varchar(30) NOT NULL, `dept` varchar(25) NOT NULL, `salary` int NOT NULL, `works_under` varchar(50) DEFAULT NULL, PRIMARY KEY (`id`)); ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.executeUpdate();

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean storeFile(EmployeeTO[] employeeTOS) {
        try (FileOutputStream fos = new FileOutputStream("resources/employee.db")) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (EmployeeTO employeeTO : employeeTOS) {
                oos.writeObject(employeeTO);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public EmployeeTO[] loadFile() {
        ArrayList<EmployeeTO> employeeTOList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream("resources/employee.db")) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                try {
                    EmployeeTO employeeTO = (EmployeeTO) ois.readObject();
                    employeeTOList.add(employeeTO);

                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            employeeTOList = null;
        }

        if (employeeTOList != null)
            return employeeTOList.toArray(new EmployeeTO[0]);
        else
            return null;
    }

    public boolean storeDatabase(EmployeeTO[] employeeTOS) {
        try (Connection connection = DriverManager.getConnection(url + dbName, userName, password)) {

            PreparedStatement clearTableStatement = connection.prepareStatement("delete from employees;");
            clearTableStatement.execute();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into employees values (?,?,?,?,?,?);");

            for (EmployeeTO employeeTO : employeeTOS) {
                Employee e = employeeTO.getEmployee();
                preparedStatement.setString(1, e.getId());
                preparedStatement.setString(2, e.getName());
                preparedStatement.setString(3, e.getDesignation());
                preparedStatement.setString(4, e.getDept());
                preparedStatement.setInt(5, e.getSalary());
                preparedStatement.setString(6, employeeTO.getWorksUnder());

                preparedStatement.execute();
            }

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public EmployeeTO[] loadDatabase() {
        ArrayList<EmployeeTO> employeeTOS = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url + dbName, userName, password)) {

            PreparedStatement preparedStatement = connection.prepareStatement("select * from employees;");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String designation = resultSet.getString("designation");
                String dept = resultSet.getString("dept");
                int salary = resultSet.getInt("salary");
                String worksUnder = resultSet.getString("works_under");

                EmployeeTO employeeTO;
                if (designation.equalsIgnoreCase("developer") ||
                designation.equalsIgnoreCase("worker")) {
                    Worker w = new Worker(id, name, designation, dept, salary);
                    employeeTO = new EmployeeTO(id, worksUnder, w);
                } else {
                    Leader l = new Leader(id, name, designation, dept, salary);
                    employeeTO =  new EmployeeTO(id, worksUnder, l);
                }

                employeeTOS.add(employeeTO);
            }

            preparedStatement.close();

        } catch (SQLException e) {
            return null;
        }

        return employeeTOS.toArray(new EmployeeTO[0]);
    }

}
