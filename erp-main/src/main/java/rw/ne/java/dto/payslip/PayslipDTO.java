package rw.ne.java.dto.payslip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ne.java.model.Payslip;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayslipDTO {
    private Long id;
    private String employeeCode;
    private String employeeName;
    private BigDecimal baseSalary;
    private BigDecimal houseAmount;
    private BigDecimal transportAmount;
    private BigDecimal employeeTaxAmount;
    private BigDecimal pensionAmount;
    private BigDecimal medicalInsuranceAmount;
    private BigDecimal otherAmount;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    private Integer month;
    private Integer year;
    private Payslip.PayslipStatus status;

    // Convert Entity to DTO
    public static PayslipDTO fromEntity(Payslip payslip) {
        PayslipDTO dto = new PayslipDTO();
        dto.setId(payslip.getId());
        dto.setEmployeeCode(payslip.getEmployee().getCode());
        dto.setEmployeeName(payslip.getEmployee().getFirstName() + " " + payslip.getEmployee().getLastName());
        // Base salary is not stored in Payslip entity, so it's not set here
        dto.setHouseAmount(payslip.getHouseAmount());
        dto.setTransportAmount(payslip.getTransportAmount());
        dto.setEmployeeTaxAmount(payslip.getEmployeeTaxAmount());
        dto.setPensionAmount(payslip.getPensionAmount());
        dto.setMedicalInsuranceAmount(payslip.getMedicalInsuranceAmount());
        dto.setOtherAmount(payslip.getOtherAmount());
        dto.setGrossSalary(payslip.getGrossSalary());
        dto.setNetSalary(payslip.getNetSalary());
        dto.setMonth(payslip.getMonth());
        dto.setYear(payslip.getYear());
        dto.setStatus(payslip.getStatus());
        return dto;
    }
}