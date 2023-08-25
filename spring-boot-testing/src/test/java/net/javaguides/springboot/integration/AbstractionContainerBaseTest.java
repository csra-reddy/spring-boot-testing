package net.javaguides.springboot.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class AbstractionContainerBaseTest {
	
	static final PostgreSQLContainer postgresqlContainer;
	
	static {
		postgresqlContainer=new PostgreSQLContainer("postgres")
		        .withDatabaseName("test")
		        .withUsername("test")
		        .withPassword("test");
			postgresqlContainer.start();
	}
	   
	   @DynamicPropertySource
	   static void setProperties(DynamicPropertyRegistry registry) {
	       registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
	       registry.add("spring.datasource.username", postgresqlContainer::getUsername);
	       registry.add("spring.datasource.password", postgresqlContainer::getPassword);
	   }

}
