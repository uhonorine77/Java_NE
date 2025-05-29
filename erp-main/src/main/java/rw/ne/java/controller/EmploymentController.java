package rw.ne.java.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.ne.java.dto.common.ApiResponse;
import rw.ne.java.dto.employment.EmploymentDTO;
import rw.ne.java.service.EmploymentService;

import java.util.List;

@RestController
@RequestMapping("/api/employments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmploymentController {

    private final EmploymentService employmentService;

    public EmploymentController(EmploymentService employmentService) {
        this.employmentService = employmentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getAllEmployments() {
        try {
            List<EmploymentDTO> employments = employmentService.getAllEmployments();
            return ResponseEntity.ok(ApiResponse.success("Employments retrieved successfully", employments));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getEmploymentById(@PathVariable String code) {
        try {
            EmploymentDTO employment = employmentService.getEmploymentById(code);
            return ResponseEntity.ok(ApiResponse.success("Employment retrieved successfully", employment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/employee/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<?> getEmploymentsByEmployee(@PathVariable String employeeCode) {
        try {
            List<EmploymentDTO> employments = employmentService.getEmploymentsByEmployee(employeeCode);
            return ResponseEntity.ok(ApiResponse.success("Employments retrieved successfully", employments));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> createEmployment(@Valid @RequestBody EmploymentDTO employmentDTO) {
        try {
            EmploymentDTO createdEmployment = employmentService.createEmployment(employmentDTO);
            return ResponseEntity.ok(ApiResponse.success("Employment created successfully", createdEmployment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> updateEmployment(@PathVariable String code, @Valid @RequestBody EmploymentDTO employmentDTO) {
        try {
            EmploymentDTO updatedEmployment = employmentService.updateEmployment(code, employmentDTO);
            return ResponseEntity.ok(ApiResponse.success("Employment updated successfully", updatedEmployment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEmployment(@PathVariable String code) {
        try {
            employmentService.deleteEmployment(code);
            return ResponseEntity.ok(ApiResponse.success("Employment deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}