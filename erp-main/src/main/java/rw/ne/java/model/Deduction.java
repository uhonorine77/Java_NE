package rw.ne.java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "deductions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deduction {

    @Id
    @Column(unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String deductionName;

    @Column(nullable = false)
    private BigDecimal percentage;
}