package rw.ne.java.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rw.ne.java.dto.employee.EmployeeDTO;
import rw.ne.java.model.Employee;
import rw.ne.java.repository.EmployeeRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(String code) {
        Employee employee = employeeRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + code));
        return EmployeeDTO.fromEntity(employee);
    }

    public EmployeeDTO getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
        return EmployeeDTO.fromEntity(employee);
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        Employee employee = new Employee();
        employee.setCode(employeeDTO.getCode() != null ? employeeDTO.getCode() : 
                "EMP" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPassword(passwordEncoder.encode("password")); // Default password
        employee.setMobile(employeeDTO.getMobile());
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setRoles(employeeDTO.getRoles());
        employee.setStatus(employeeDTO.getStatus() != null ? employeeDTO.getStatus() : Employee.EmployeeStatus.ACTIVE);

        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeDTO.fromEntity(savedEmployee);
    }

    public EmployeeDTO updateEmployee(String code, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + code));

        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setMobile(employeeDTO.getMobile());
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setRoles(employeeDTO.getRoles());
        employee.setStatus(employeeDTO.getStatus());

        Employee updatedEmployee = employeeRepository.save(employee);
        return EmployeeDTO.fromEntity(updatedEmployee);
    }

    public void deleteEmployee(String code) {
        Employee employee = employeeRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + code));
        
        employee.setStatus(Employee.EmployeeStatus.DISABLED);
        employeeRepository.save(employee);
    }
}