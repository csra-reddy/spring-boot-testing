package net.javaguides.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.springboot.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Long>{

	Optional<Employee> findByEmail(String email);

}
