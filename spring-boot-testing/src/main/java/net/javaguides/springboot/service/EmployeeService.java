package net.javaguides.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	public Employee saveEmployee(Employee employee) {
		
		Optional<Employee> savedEmployee=employeeRepository.findByEmail(employee.getEmail());
		if(savedEmployee.isPresent()) {
			throw new ResourceNotFoundException("Employee already exist with given email:" +employee.getEmail());
		}
		return employeeRepository.save(employee);
	}
	
	
	public List<Employee> getAllEmployees(){
		return employeeRepository.findAll();
	}
	
	public Optional<Employee> getEmployeeById(long Id) {
		return employeeRepository.findById(Id);
	}
	
	public Employee updateEmployee(Employee updatedEmployee) {
		return employeeRepository.save(updatedEmployee);
	}
	
	public void deleteEmployee(long Id) {
		employeeRepository.deleteById(Id);
	}



}
