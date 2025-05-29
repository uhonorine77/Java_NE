package rw.ne.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ne.java.model.Employee;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
}