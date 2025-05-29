package rw.ne.java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employment {

    @Id
    @Column(unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "employee_code", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private BigDecimal baseSalary;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus status = EmploymentStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDate joiningDate;

    public enum EmploymentStatus {
        ACTIVE, INACTIVE
    }
}