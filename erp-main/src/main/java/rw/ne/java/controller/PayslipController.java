package rw.ne.java.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.ne.java.dto.common.ApiResponse;
import rw.ne.java.dto.payslip.PayrollProcessRequest;
import rw.ne.java.dto.payslip.PayslipDTO;
import rw.ne.java.service.PayslipService;

import java.util.List;

@RestController
@RequestMapping("/api/payslips")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PayslipController {

    private final PayslipService payslipService;

    public PayslipController(PayslipService payslipService) {
        this.payslipService = payslipService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getAllPayslips() {
        try {
            List<PayslipDTO> payslips = payslipService.getAllPayslips();
            return ResponseEntity.ok(ApiResponse.success("Payslips retrieved successfully", payslips));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<?> getPayslipById(@PathVariable Long id) {
        try {
            PayslipDTO payslip = payslipService.getPayslipById(id);
            return ResponseEntity.ok(ApiResponse.success("Payslip retrieved successfully", payslip));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/employee/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<?> getPayslipsByEmployee(@PathVariable String employeeCode) {
        try {
            List<PayslipDTO> payslips = payslipService.getPayslipsByEmployee(employeeCode);
            return ResponseEntity.ok(ApiResponse.success("Payslips retrieved successfully", payslips));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/month-year")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getPayslipsByMonthAndYear(
            @RequestParam Integer month, 
            @RequestParam Integer year) {
        try {
            List<PayslipDTO> payslips = payslipService.getPayslipsByMonthAndYear(month, year);
            return ResponseEntity.ok(ApiResponse.success("Payslips retrieved successfully", payslips));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/process")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> processPayroll(@Valid @RequestBody PayrollProcessRequest request) {
        try {
            List<PayslipDTO> processedPayslips = payslipService.processPayroll(request);
            return ResponseEntity.ok(ApiResponse.success("Payroll processed successfully", processedPayslips));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approvePayroll(
            @RequestParam Integer month, 
            @RequestParam Integer year) {
        try {
            List<PayslipDTO> approvedPayslips = payslipService.approvePayroll(month, year);
            return ResponseEntity.ok(ApiResponse.success("Payroll approved successfully", approvedPayslips));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}