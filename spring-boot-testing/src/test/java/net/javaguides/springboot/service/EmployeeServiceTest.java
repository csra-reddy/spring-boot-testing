package net.javaguides.springboot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	
	@Mock
	private EmployeeRepository employeeRepository;
	
	@InjectMocks
	private EmployeeService employeeService;
	
	private Employee employee;
	
	@BeforeEach
	public void setup() {
		employee = Employee.builder()
				.id(1L)
				.firstName("Chandra")
				.lastName("Reddy")
				.email("csranam@gmail.com")
				.build();
	}
	
	
	@DisplayName("JUnit test for Save method in Service Class")
	@Test
	public void testSaveEmployee() {
				
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
		
		Employee savedEmployee=employeeService.saveEmployee(employee);
		
		assertEquals("Chandra",savedEmployee.getFirstName());
		
	}
	
	
	@DisplayName("JUnit test for Save method in Service Class which throws exceptions")
	@Test
	public void testSaveEmployeeException() {
		
		when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
		
		assertThrows(ResourceNotFoundException.class,() -> {
			employeeService.saveEmployee(employee);
			});
		
		verify(employeeRepository,never()).save(any(Employee.class));
		
	}
	
	
	@DisplayName("JUnit test for getAllEmployees method in Service Class")
	@Test
	public void testGelAllEmployees() {
		
		Employee employee1 = Employee.builder()
				.id(1L)
				.firstName("Revathi")
				.lastName("Vaka")
				.email("revtishere@gmail.com")
				.build();
		
		List<Employee> employees=new ArrayList<>();
		employees.add(employee);
		employees.add(employee1);
		
		when(employeeRepository.findAll()).thenReturn(employees);
		
		List<Employee> employeeList=employeeService.getAllEmployees();
		
		assertThat(employeeList.size()).isNotNull();
		assertThat(employeeList.size()).isEqualTo(2);
		
	}
	
	
	@DisplayName("JUnit test for getAllEmployees method in Service Class(-ve Scenario)")
	@Test
	public void testEmptyGelAllEmployees() {
		
		Employee employee1 = Employee.builder()
				.id(2L)
				.firstName("Revathi")
				.lastName("Vaka")
				.email("revtishere@gmail.com")
				.build();
		
		List<Employee> employees=new ArrayList<>();
		employees.add(employee);
		employees.add(employee1);
		
		when(employeeRepository.findAll()).thenReturn(Collections.emptyList());
		
		List<Employee> employeeList=employeeService.getAllEmployees();
		
		assertThat(employeeList).isEmpty();
		assertEquals(0,employeeList.size());
		
	}
	
	
	@DisplayName("Junit test for getting Employee by Id method in the service class")
	@Test
	public void testGetEmployeeById() {
		
		Optional<Employee> employee2 = Optional.ofNullable(Employee.builder()
				.id(3L)
				.firstName("Duckie")
				.lastName("Anam")
				.email("duckie@gmail.com")
				.build());
		
		when(employeeRepository.findById(3L)).thenReturn(employee2);

		Optional<Employee> savedEmployee=employeeService.getEmployeeById(3L);
		
		assertNotNull(savedEmployee);
	}
	
	
	@DisplayName("Junit tests for update Employee method in the service class")
	@Test
	public void testUpdateEmployee() {
		
		when(employeeRepository.save(employee)).thenReturn(employee);
		
		employee.setEmail("lekha@gmail.com");
		employee.setFirstName("lekha");
		
		Employee updatedEmployee=employeeService.updateEmployee(employee);
		
		assertThat(updatedEmployee.getEmail()).isEqualTo("lekha@gmail.com");
		assertThat(updatedEmployee.getFirstName()).isEqualTo("lekha");
	}
	
	
	@DisplayName("Junit tests for delete Employee method in the service class")
	@Test
	public void testDeleteEmployee() {
		
		doNothing().when(employeeRepository).deleteById(1L);
		
		long employeeId=1L;
		employeeService.deleteEmployee(employeeId);
		
		verify(employeeRepository, times(1)).deleteById(employeeId);
	}

}
