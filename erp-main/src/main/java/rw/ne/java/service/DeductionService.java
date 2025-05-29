package rw.ne.java.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import rw.ne.java.dto.deduction.DeductionDTO;
import rw.ne.java.model.Deduction;
import rw.ne.java.repository.DeductionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeductionService {

    private final DeductionRepository deductionRepository;

    public DeductionService(DeductionRepository deductionRepository) {
        this.deductionRepository = deductionRepository;
    }

    @PostConstruct
    public void initDeductions() {
        // Initialize default deductions if they don't exist
        if (deductionRepository.count() == 0) {
            createDefaultDeduction("Employee Tax", new BigDecimal("30"));
            createDefaultDeduction("Pension", new BigDecimal("6"));
            createDefaultDeduction("Medical Insurance", new BigDecimal("5"));
            createDefaultDeduction("Housing", new BigDecimal("14"));
            createDefaultDeduction("Transport", new BigDecimal("14"));
            createDefaultDeduction("Others", new BigDecimal("5"));
        }
    }

    private void createDefaultDeduction(String name, BigDecimal percentage) {
        Deduction deduction = new Deduction();
        deduction.setCode("DED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        deduction.setDeductionName(name);
        deduction.setPercentage(percentage);
        deductionRepository.save(deduction);
    }

    public List<DeductionDTO> getAllDeductions() {
        return deductionRepository.findAll().stream()
                .map(DeductionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public DeductionDTO getDeductionById(String code) {
        Deduction deduction = deductionRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Deduction not found with code: " + code));
        return DeductionDTO.fromEntity(deduction);
    }

    public DeductionDTO getDeductionByName(String name) {
        Deduction deduction = deductionRepository.findByDeductionName(name)
                .orElseThrow(() -> new RuntimeException("Deduction not found with name: " + name));
        return DeductionDTO.fromEntity(deduction);
    }

    public DeductionDTO createDeduction(DeductionDTO deductionDTO) {
        if (deductionRepository.existsByDeductionName(deductionDTO.getDeductionName())) {
            throw new RuntimeException("Deduction name is already in use!");
        }

        Deduction deduction = new Deduction();
        deduction.setCode(deductionDTO.getCode() != null ? deductionDTO.getCode() : 
                "DED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        deduction.setDeductionName(deductionDTO.getDeductionName());
        deduction.setPercentage(deductionDTO.getPercentage());

        Deduction savedDeduction = deductionRepository.save(deduction);
        return DeductionDTO.fromEntity(savedDeduction);
    }

    public DeductionDTO updateDeduction(String code, DeductionDTO deductionDTO) {
        Deduction deduction = deductionRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Deduction not found with code: " + code));

        // Check if the name is being changed and if it's already in use
        if (!deduction.getDeductionName().equals(deductionDTO.getDeductionName()) && 
                deductionRepository.existsByDeductionName(deductionDTO.getDeductionName())) {
            throw new RuntimeException("Deduction name is already in use!");
        }

        deduction.setDeductionName(deductionDTO.getDeductionName());
        deduction.setPercentage(deductionDTO.getPercentage());

        Deduction updatedDeduction = deductionRepository.save(deduction);
        return DeductionDTO.fromEntity(updatedDeduction);
    }

    public void deleteDeduction(String code) {
        if (!deductionRepository.existsById(code)) {
            throw new RuntimeException("Deduction not found with code: " + code);
        }
        deductionRepository.deleteById(code);
    }
}