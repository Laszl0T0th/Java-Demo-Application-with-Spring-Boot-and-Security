package com.example.demo.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.models.EmployeeModel;
import com.example.demo.models.EmployeeModelCreate;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    
	@Value("${spring.datasource.url}")
    private String URL; 
    
    @Value("${spring.datasource.username}")
    private String USER; 
    
    @Value("${spring.datasource.password}")
    private String PASSWORD; 
    
    @Override
	public CompletableFuture<List<EmployeeModel>> getEmployeesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            List<EmployeeModel> emps = new ArrayList<>();
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                preparedStatement = connection.prepareStatement("SELECT * FROM EMPLOYEE");
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    EmployeeModel emp = new EmployeeModel();
                    emp.setID(resultSet.getInt("ID"));
                    emp.setFirstName(resultSet.getString("FirstName"));
                    emp.setLastName(resultSet.getString("LastName"));
                    emp.setAddress(resultSet.getString("Address"));
                    emp.setImageUrl(resultSet.getString("image_url"));
                    emps.add(emp);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return emps;
        });
    }

    @Override
	public CompletableFuture<String> createEmployeeAsync(EmployeeModelCreate employeeModelCreate) {
        return CompletableFuture.supplyAsync(() -> {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            String message;

            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                preparedStatement = connection.prepareStatement("INSERT INTO EMPLOYEE (FirstName, LastName, Address, image_url) VALUES (?, ?, ?, ?)");
                preparedStatement.setString(1, employeeModelCreate.getFirstName());
                preparedStatement.setString(2, employeeModelCreate.getLastName());
                preparedStatement.setString(3, employeeModelCreate.getAddress());
                preparedStatement.setString(4, employeeModelCreate.getImageUrl());

                int rowsAffected = preparedStatement.executeUpdate();
                message = (rowsAffected > 0) ? "Employee created successfully!" : "Failed to create employee.";
            } catch (SQLException e) {
                e.printStackTrace();
                message = "Error occurred: " + e.getMessage();
            } finally {
                try {
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return message;
        });
    }
    
    @Override
	public CompletableFuture<EmployeeModel> getEmployeeByIdAsync(int id) {
        return CompletableFuture.supplyAsync(() -> {
            EmployeeModel emp = null;
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                preparedStatement = connection.prepareStatement("SELECT * FROM EMPLOYEE WHERE ID = ?");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    emp = new EmployeeModel();
                    emp.setID(resultSet.getInt("ID"));
                    emp.setFirstName(resultSet.getString("FirstName"));
                    emp.setLastName(resultSet.getString("LastName"));
                    emp.setAddress(resultSet.getString("Address"));
                    emp.setImageUrl(resultSet.getString("image_url"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return emp;
        });
    }


    @Override
	public CompletableFuture<String> updateEmployeeAsync(EmployeeModel employeeModel) {
        return CompletableFuture.supplyAsync(() -> {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            String message;

            System.out.println("updateEmployeeAsync call");
            System.out.println("ID: " + employeeModel.getID());
            System.out.println("FirstName: " + employeeModel.getFirstName());
            System.out.println("LastName: " + employeeModel.getLastName());
            System.out.println("Address: " + employeeModel.getAddress());
            System.out.println("ImageUrl: " + employeeModel.getImageUrl());


            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);

                preparedStatement = connection.prepareStatement("UPDATE EMPLOYEE SET FirstName = ?, LastName = ?, Address = ?, image_url = ? WHERE ID = ?");
                preparedStatement.setString(1, employeeModel.getFirstName());
                preparedStatement.setString(2, employeeModel.getLastName());
                preparedStatement.setString(3, employeeModel.getAddress());
                preparedStatement.setString(4, employeeModel.getImageUrl());
                preparedStatement.setInt(5, employeeModel.getID());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    message = "Employee updated successfully!";
                } else {
                    message = "Failed to update employee. No rows affected.";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                message = "Error occurred: " + e.getMessage();
            } finally {
                try {
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return message;
        });
    }


    @Override
	public CompletableFuture<Void> deleteEmployeeAsync(int id) {
        return CompletableFuture.runAsync(() -> {
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                preparedStatement = connection.prepareStatement("DELETE FROM EMPLOYEE WHERE ID = ?");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
