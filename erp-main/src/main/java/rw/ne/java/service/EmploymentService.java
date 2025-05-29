package rw.ne.java.service;

import org.springframework.stereotype.Service;
import rw.ne.java.dto.employment.EmploymentDTO;
import rw.ne.java.model.Employee;
import rw.ne.java.model.Employment;
import rw.ne.java.repository.EmployeeRepository;
import rw.ne.java.repository.EmploymentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmploymentService {

    private final EmploymentRepository employmentRepository;
    private final EmployeeRepository employeeRepository;

    public EmploymentService(EmploymentRepository employmentRepository, EmployeeRepository employeeRepository) {
        this.employmentRepository = employmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<EmploymentDTO> getAllEmployments() {
        return employmentRepository.findAll().stream()
                .map(EmploymentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public EmploymentDTO getEmploymentById(String code) {
        Employment employment = employmentRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employment not found with code: " + code));
        return EmploymentDTO.fromEntity(employment);
    }

    public List<EmploymentDTO> getEmploymentsByEmployee(String employeeCode) {
        Employee employee = employeeRepository.findById(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + employeeCode));
        
        return employmentRepository.findByEmployee(employee).stream()
                .map(EmploymentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public EmploymentDTO createEmployment(EmploymentDTO employmentDTO) {
        Employee employee = employeeRepository.findById(employmentDTO.getEmployeeCode())
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + employmentDTO.getEmployeeCode()));

        Employment employment = new Employment();
        employment.setCode(employmentDTO.getCode() != null ? employmentDTO.getCode() : 
                "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        employment.setEmployee(employee);
        employment.setDepartment(employmentDTO.getDepartment());
        employment.setPosition(employmentDTO.getPosition());
        employment.setBaseSalary(employmentDTO.getBaseSalary());
        employment.setStatus(employmentDTO.getStatus() != null ? employmentDTO.getStatus() : Employment.EmploymentStatus.ACTIVE);
        employment.setJoiningDate(employmentDTO.getJoiningDate());

        Employment savedEmployment = employmentRepository.save(employment);
        return EmploymentDTO.fromEntity(savedEmployment);
    }

    public EmploymentDTO updateEmployment(String code, EmploymentDTO employmentDTO) {
        Employment employment = employmentRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employment not found with code: " + code));

        employment.setDepartment(employmentDTO.getDepartment());
        employment.setPosition(employmentDTO.getPosition());
        employment.setBaseSalary(employmentDTO.getBaseSalary());
        employment.setStatus(employmentDTO.getStatus());
        employment.setJoiningDate(employmentDTO.getJoiningDate());

        Employment updatedEmployment = employmentRepository.save(employment);
        return EmploymentDTO.fromEntity(updatedEmployment);
    }

    public void deleteEmployment(String code) {
        Employment employment = employmentRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employment not found with code: " + code));
        
        employment.setStatus(Employment.EmploymentStatus.INACTIVE);
        employmentRepository.save(employment);
    }

    public Employment getActiveEmployment(Employee employee) {
        return employmentRepository.findByEmployeeAndStatusOrderByJoiningDateDesc(employee, Employment.EmploymentStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active employment found for employee: " + employee.getCode()));
    }
}