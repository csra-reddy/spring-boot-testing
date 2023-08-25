package net.javaguides.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;

@RestController
@RequestMapping("/api")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee saveEmployee(@RequestBody Employee employee) {
		return employeeService.saveEmployee(employee);
	}
	
	@GetMapping("/allemployees")
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId){
		return employeeService.getEmployeeById(employeeId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployeeById(@PathVariable("id") long employeeId, @RequestBody Employee employee){
		return employeeService.getEmployeeById(employeeId)
				.map(savedEmployee -> {
					
					savedEmployee.setFirstName(employee.getFirstName());
					savedEmployee.setLastName(employee.getLastName());
					savedEmployee.setEmail(employee.getEmail());
					
					Employee updatedEmployee=employeeService.updateEmployee(savedEmployee);
					return new ResponseEntity<>(updatedEmployee,HttpStatus.OK);
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId){
		employeeService.deleteEmployee(employeeId);
		return new ResponseEntity<String>("Employee deleted successfully",HttpStatus.OK);
	}

}
