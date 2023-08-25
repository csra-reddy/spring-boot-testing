package net.javaguides.springboot.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	@InjectMocks
	private EmployeeController employeeController;
	
	private ObjectMapper objectMapper=new ObjectMapper();
	
	@DisplayName("JUnit test for Save method in Controller Class")
	@Test
	public void testSaveEmployee() throws Exception {
		
		Employee employee = new Employee(1L,"Chandra","Reddy","csranam@gmail.com");
		
		when(employeeService.saveEmployee(Mockito.any(Employee.class))).thenReturn(employee);
		
		ResultActions response=mockMvc.perform(MockMvcRequestBuilders
				.post("/api/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(employee)));
				
		response.andExpect(status().isCreated());
		response.andExpect(jsonPath("$.firstName",is("Chandra")));
		
	}
	
	@DisplayName("JUnit test for get all employees method in Controller class")
	@Test
	public void testGetAllEmployees() throws Exception {
		
		List<Employee> listOfEmployees=new ArrayList<>();
		listOfEmployees.add(Employee.builder().id(1L).firstName("Chandra").lastName("Reddy").email("csranam@gmail.com").build());
		listOfEmployees.add(Employee.builder().id(2L).firstName("Revathi").lastName("Vaka").email("revtishere@gmail.com").build());
		
		when(employeeService.getAllEmployees()).thenReturn(listOfEmployees);
		
		ResultActions response=mockMvc.perform(get("/api/allemployees"));
		
		response.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(listOfEmployees.size())))
				.andDo(print());
	}

	
	@DisplayName("JUnit test for get employee by id method in Controller class +ve scenario")
	@Test
	public void testGetEmployeeById() throws Exception {
		
		long employeeId=1L;
		Employee employee=Employee.builder()
				.firstName("Chandra")
				.lastName("Reddy")
				.email("csranam@gmail.com")
				.build();
		
		when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(employee));
		
		ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));
		
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName",is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName",is(employee.getLastName())))
				.andExpect(jsonPath("$.email",is(employee.getEmail())));
	}
	
	
	@DisplayName("JUnit test for get employee by id method in Controller class -ve scenario")
	@Test
	public void testInvalidGetEmployeeById() throws Exception {
		long employeeId=1L;
		Employee employee=Employee.builder()
				.firstName("Chandra")
				.lastName("Reddy")
				.email("csranam@gmail.com")
				.build();
		
		when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());
		
		ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));
		
		response.andExpect(status().isNotFound())
				.andDo(print());	
	}
	
	@DisplayName("JUnit test for update employee by id method in Controller class +ve scenario")
	@Test
	public void testUpdateEmployeeById() throws JsonProcessingException, Exception {
		long employeeId=1L;
		Employee savedEmployee=Employee.builder()
				.firstName("Chandra")
				.lastName("Reddy")
				.email("csranam@gmail.com")
				.build();
		Employee updatedEmployee=Employee.builder()
				.firstName("Revathi")
				.lastName("Vaka")
				.email("revtishere@gmail.com")
				.build();
		
		when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(savedEmployee));
		when(employeeService.updateEmployee(Mockito.any(Employee.class))).thenReturn(savedEmployee);
		
		ResultActions response=mockMvc.perform(put("/api/employees/{id}",employeeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedEmployee)));
		
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName",is(updatedEmployee.getFirstName())))
				.andExpect(jsonPath("$.lastName",is(updatedEmployee.getLastName())))
				.andExpect(jsonPath("$.email",is(updatedEmployee.getEmail())));

	}
	
	
	@DisplayName("JUnit test for update employee by id method in Controller class -ve scenario")
	@Test
	public void testNegativeUpdateEmployeeById() throws JsonProcessingException, Exception {
		
		long employeeId=1L;
		Employee savedEmployee=Employee.builder()
				.firstName("Chandra")
				.lastName("Reddy")
				.email("csranam@gmail.com")
				.build();
		Employee updatedEmployee=Employee.builder()
				.firstName("Revathi")
				.lastName("Vaka")
				.email("revtishere@gmail.com")
				.build();
		
		when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());
		when(employeeService.updateEmployee(Mockito.any(Employee.class))).thenReturn(savedEmployee);
		
		ResultActions response=mockMvc.perform(put("/api/employees/{id}",employeeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedEmployee)));
		
		response.andExpect(status().isNotFound())
				.andDo(print());
	}
	
	@DisplayName("JUnit test for delete employee by id method in Controller class")
	@Test
	public void testDeleteEmployeeById() throws Exception {
		long employeeId=1L;
		
		doNothing().when(employeeService).deleteEmployee(employeeId);
		
		ResultActions response=mockMvc.perform(delete("/api/employees/{id}",employeeId));
		
		response.andExpect(status().isOk())
				.andDo(print());
		
	}
}
