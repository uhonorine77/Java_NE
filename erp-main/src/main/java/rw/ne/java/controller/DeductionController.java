package rw.ne.java.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.ne.java.dto.common.ApiResponse;
import rw.ne.java.dto.deduction.DeductionDTO;
import rw.ne.java.service.DeductionService;

import java.util.List;

@RestController
@RequestMapping("/api/deductions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DeductionController {

    private final DeductionService deductionService;

    public DeductionController(DeductionService deductionService) {
        this.deductionService = deductionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<?> getAllDeductions() {
        try {
            List<DeductionDTO> deductions = deductionService.getAllDeductions();
            return ResponseEntity.ok(ApiResponse.success("Deductions retrieved successfully", deductions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getDeductionById(@PathVariable String code) {
        try {
            DeductionDTO deduction = deductionService.getDeductionById(code);
            return ResponseEntity.ok(ApiResponse.success("Deduction retrieved successfully", deduction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getDeductionByName(@PathVariable String name) {
        try {
            DeductionDTO deduction = deductionService.getDeductionByName(name);
            return ResponseEntity.ok(ApiResponse.success("Deduction retrieved successfully", deduction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> createDeduction(@Valid @RequestBody DeductionDTO deductionDTO) {
        try {
            DeductionDTO createdDeduction = deductionService.createDeduction(deductionDTO);
            return ResponseEntity.ok(ApiResponse.success("Deduction created successfully", createdDeduction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> updateDeduction(@PathVariable String code, @Valid @RequestBody DeductionDTO deductionDTO) {
        try {
            DeductionDTO updatedDeduction = deductionService.updateDeduction(code, deductionDTO);
            return ResponseEntity.ok(ApiResponse.success("Deduction updated successfully", updatedDeduction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDeduction(@PathVariable String code) {
        try {
            deductionService.deleteDeduction(code);
            return ResponseEntity.ok(ApiResponse.success("Deduction deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}