package rw.ne.java.dto.deduction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ne.java.model.Deduction;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductionDTO {
    private String code;
    
    @NotBlank(message = "Deduction name is required")
    private String deductionName;
    
    @NotNull(message = "Percentage is required")
    @Positive(message = "Percentage must be positive")
    private BigDecimal percentage;

    // Convert Entity to DTO
    public static DeductionDTO fromEntity(Deduction deduction) {
        DeductionDTO dto = new DeductionDTO();
        dto.setCode(deduction.getCode());
        dto.setDeductionName(deduction.getDeductionName());
        dto.setPercentage(deduction.getPercentage());
        return dto;
    }
}