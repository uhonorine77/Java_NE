package rw.ne.java.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.ne.java.dto.common.ApiResponse;
import rw.ne.java.dto.employee.EmployeeDTO;
import rw.ne.java.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getAllEmployees() {
        try {
            List<EmployeeDTO> employees = employeeService.getAllEmployees();
            return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employees));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getEmployeeById(@PathVariable String code) {
        try {
            EmployeeDTO employee = employeeService.getEmployeeById(code);
            return ResponseEntity.ok(ApiResponse.success("Employee retrieved successfully", employee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getEmployeeByEmail(@PathVariable String email) {
        try {
            EmployeeDTO employee = employeeService.getEmployeeByEmail(email);
            return ResponseEntity.ok(ApiResponse.success("Employee retrieved successfully", employee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
            return ResponseEntity.ok(ApiResponse.success("Employee created successfully", createdEmployee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> updateEmployee(@PathVariable String code, @Valid @RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO updatedEmployee = employeeService.updateEmployee(code, employeeDTO);
            return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updatedEmployee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEmployee(@PathVariable String code) {
        try {
            employeeService.deleteEmployee(code);
            return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}