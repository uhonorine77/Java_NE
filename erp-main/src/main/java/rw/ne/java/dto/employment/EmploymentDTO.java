package rw.ne.java.dto.employment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ne.java.model.Employment;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDTO {
    private String code;
    
    @NotBlank(message = "Employee code is required")
    private String employeeCode;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    @NotNull(message = "Base salary is required")
    @Positive(message = "Base salary must be positive")
    private BigDecimal baseSalary;
    
    private Employment.EmploymentStatus status;
    
    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    // Convert Entity to DTO
    public static EmploymentDTO fromEntity(Employment employment) {
        EmploymentDTO dto = new EmploymentDTO();
        dto.setCode(employment.getCode());
        dto.setEmployeeCode(employment.getEmployee().getCode());
        dto.setDepartment(employment.getDepartment());
        dto.setPosition(employment.getPosition());
        dto.setBaseSalary(employment.getBaseSalary());
        dto.setStatus(employment.getStatus());
        dto.setJoiningDate(employment.getJoiningDate());
        return dto;
    }
}