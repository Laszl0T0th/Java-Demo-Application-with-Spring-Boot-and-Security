package com.example.demo.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.demo.models.EmployeeModel;
import com.example.demo.models.EmployeeModelCreate;

public interface EmployeeService {

	CompletableFuture<List<EmployeeModel>> getEmployeesAsync();

	CompletableFuture<String> createEmployeeAsync(EmployeeModelCreate employeeModelCreate);

	CompletableFuture<EmployeeModel> getEmployeeByIdAsync(int id);

	CompletableFuture<String> updateEmployeeAsync(EmployeeModel employeeModel);

	CompletableFuture<Void> deleteEmployeeAsync(int id);

}