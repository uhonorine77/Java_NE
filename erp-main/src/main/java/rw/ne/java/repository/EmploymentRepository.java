package rw.ne.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ne.java.model.Employee;
import rw.ne.java.model.Employment;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, String> {
    List<Employment> findByEmployee(Employee employee);
    List<Employment> findByEmployeeAndStatus(Employee employee, Employment.EmploymentStatus status);
    Optional<Employment> findByEmployeeAndStatusOrderByJoiningDateDesc(Employee employee, Employment.EmploymentStatus status);
}