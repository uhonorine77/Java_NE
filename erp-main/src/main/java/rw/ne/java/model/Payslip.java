package rw.ne.java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "payslips", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employee_code", "month", "year"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_code", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private BigDecimal houseAmount;

    @Column(nullable = false)
    private BigDecimal transportAmount;

    @Column(nullable = false)
    private BigDecimal employeeTaxAmount;

    @Column(nullable = false)
    private BigDecimal pensionAmount;

    @Column(nullable = false)
    private BigDecimal medicalInsuranceAmount;

    @Column(nullable = false)
    private BigDecimal otherAmount;

    @Column(nullable = false)
    private BigDecimal grossSalary;

    @Column(nullable = false)
    private BigDecimal netSalary;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    private PayslipStatus status = PayslipStatus.PENDING;

    public enum PayslipStatus {
        PENDING, PAID
    }
}