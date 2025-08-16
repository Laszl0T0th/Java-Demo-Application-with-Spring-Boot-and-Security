package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.EmployeeModel;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeModel, Integer> {
//	public EmployeeRepository findAll();
}
