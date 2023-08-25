package net.javaguides.springboot.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest extends AbstractionContainerBaseTest {
	
  	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setup() {
		employeeRepository.deleteAll();
	}
	
	@DisplayName("JUnit test for Save method in Controller Class")
	@Test
	public void testSaveEmployee() throws Exception {
		
		Employee employee = Employee.builder()
				.firstName("Farha")
				.lastName("Begum")
				.email("farha@gmail.com")
				.build();
		
		ResultActions response=mockMvc.perform(post("/api/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(employee)));
				
		response.andExpect(status().isCreated())
				.andDo(print())
				.andExpect(jsonPath("$.firstName",is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName",is(employee.getLastName())))
				.andExpect(jsonPath("$.email",is(employee.getEmail())));
	}
	
	@DisplayName("JUnit test for get all employees method in Controller class")
	@Test
	public void testGetAllEmployees() throws Exception {
		
		List<Employee> listOfEmployees=new ArrayList<>();
		listOfEmployees.add(Employee.builder().id(1L).firstName("Chandra").lastName("Reddy").email("csranam@gmail.com").build());
		listOfEmployees.add(Employee.builder().id(2L).firstName("Revathi").lastName("Vaka").email("revtishere@gmail.com").build());
		employeeRepository.saveAll(listOfEmployees);
		
		ResultActions response=mockMvc.perform(get("/api/allemployees"));
		
		response.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(listOfEmployees.size())))
				.andDo(print());
	}
	
	@DisplayName("JUnit test for get employee by id method in Controller class +ve scenario")
	@Test
	public void testGetEmployeeById() throws Exception {
		
		Employee employee=Employee.builder()
				.firstName("Sameera")
				.lastName("Reddy")
				.email("sameerareddy@gmail.com")
				.build();
		employeeRepository.save(employee);
		
		ResultActions response = mockMvc.perform(get("/api/employees/{id}",employee.getId()));
		
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
				.firstName("Juniper")
				.lastName("Reddy")
				.email("juniper@gmail.com")
				.build();
		employeeRepository.save(employee);
		
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
			employeeRepository.save(savedEmployee);
			
			Employee updatedEmployee=Employee.builder()
					.firstName("Revathi")
					.lastName("Vaka")
					.email("revtishere@gmail.com")
					.build();
			
			
			ResultActions response=mockMvc.perform(put("/api/employees/{id}",savedEmployee.getId())
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
			
			long employeeId=1000L;
			Employee savedEmployee=Employee.builder()
					.firstName("Chandra")
					.lastName("Reddy")
					.email("csranam@gmail.com")
					.build();
			employeeRepository.save(savedEmployee);
			
			Employee updatedEmployee=Employee.builder()
					.firstName("Revathi")
					.lastName("Vaka")
					.email("revtishere@gmail.com")
					.build();
			
			ResultActions response=mockMvc.perform(put("/api/employees/{id}",employeeId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(updatedEmployee)));
			
			response.andExpect(status().isNotFound())
					.andDo(print());
		}
		
		@DisplayName("JUnit test for delete employee by id method in Controller class")
		@Test
		public void testDeleteEmployeeById() throws Exception {
			Employee savedEmployee=Employee.builder()
					.firstName("Chandra")
					.lastName("Reddy")
					.email("csranam@gmail.com")
					.build();
			employeeRepository.save(savedEmployee);
			
			ResultActions response=mockMvc.perform(delete("/api/employees/{id}",savedEmployee.getId()));
			
			response.andExpect(status().isOk())
					.andDo(print());
			
		}

}
