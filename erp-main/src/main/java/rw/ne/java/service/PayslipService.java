package rw.ne.java.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ne.java.dto.payslip.PayrollProcessRequest;
import rw.ne.java.dto.payslip.PayslipDTO;
import rw.ne.java.model.Deduction;
import rw.ne.java.model.Employee;
import rw.ne.java.model.Employment;
import rw.ne.java.model.Payslip;
import rw.ne.java.repository.DeductionRepository;
import rw.ne.java.repository.EmployeeRepository;
import rw.ne.java.repository.PayslipRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayslipService {

    private final PayslipRepository payslipRepository;
    private final EmployeeRepository employeeRepository;
    private final DeductionRepository deductionRepository;
    private final EmploymentService employmentService;
    private final MessageService messageService;

    public PayslipService(PayslipRepository payslipRepository, EmployeeRepository employeeRepository,
                          DeductionRepository deductionRepository, EmploymentService employmentService,
                          MessageService messageService) {
        this.payslipRepository = payslipRepository;
        this.employeeRepository = employeeRepository;
        this.deductionRepository = deductionRepository;
        this.employmentService = employmentService;
        this.messageService = messageService;
    }

    public List<PayslipDTO> getAllPayslips() {
        return payslipRepository.findAll().stream()
                .map(PayslipDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public PayslipDTO getPayslipById(Long id) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found with id: " + id));
        return PayslipDTO.fromEntity(payslip);
    }

    public List<PayslipDTO> getPayslipsByEmployee(String employeeCode) {
        Employee employee = employeeRepository.findById(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + employeeCode));
        
        return payslipRepository.findByEmployee(employee).stream()
                .map(PayslipDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PayslipDTO> getPayslipsByMonthAndYear(Integer month, Integer year) {
        return payslipRepository.findByMonthAndYear(month, year).stream()
                .map(PayslipDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PayslipDTO> processPayroll(PayrollProcessRequest request) {
        Integer month = request.getMonth();
        Integer year = request.getYear();
        
        List<Employee> activeEmployees = employeeRepository.findAll().stream()
                .filter(employee -> employee.getStatus() == Employee.EmployeeStatus.ACTIVE)
                .collect(Collectors.toList());
        
        List<Payslip> processedPayslips = new ArrayList<>();
        
        for (Employee employee : activeEmployees) {
            // Check if payslip already exists for this employee in the given month/year
            if (payslipRepository.existsByEmployeeAndMonthAndYear(employee, month, year)) {
                throw new RuntimeException("Payslip already exists for employee " + employee.getCode() + 
                        " for month " + month + "/" + year);
            }
            
            try {
                // Get active employment for the employee
                Employment employment = employmentService.getActiveEmployment(employee);
                BigDecimal baseSalary = employment.getBaseSalary();
                
                // Calculate housing and transport allowances (14% each of base salary)
                BigDecimal housingPercentage = getDeductionPercentage("Housing");
                BigDecimal transportPercentage = getDeductionPercentage("Transport");
                
                BigDecimal housingAmount = calculatePercentage(baseSalary, housingPercentage);
                BigDecimal transportAmount = calculatePercentage(baseSalary, transportPercentage);
                
                // Calculate gross salary
                BigDecimal grossSalary = baseSalary.add(housingAmount).add(transportAmount);
                
                // Calculate deductions
                BigDecimal taxPercentage = getDeductionPercentage("Employee Tax");
                BigDecimal pensionPercentage = getDeductionPercentage("Pension");
                BigDecimal medicalPercentage = getDeductionPercentage("Medical Insurance");
                BigDecimal otherPercentage = getDeductionPercentage("Others");
                
                BigDecimal taxAmount = calculatePercentage(baseSalary, taxPercentage);
                BigDecimal pensionAmount = calculatePercentage(baseSalary, pensionPercentage);
                BigDecimal medicalAmount = calculatePercentage(baseSalary, medicalPercentage);
                BigDecimal otherAmount = calculatePercentage(baseSalary, otherPercentage);
                
                // Calculate net salary
                BigDecimal totalDeductions = taxAmount.add(pensionAmount).add(medicalAmount).add(otherAmount);
                BigDecimal netSalary = grossSalary.subtract(totalDeductions);
                
                // Validate that total deductions don't exceed gross salary
                if (totalDeductions.compareTo(grossSalary) > 0) {
                    throw new RuntimeException("Total deductions exceed gross salary for employee " + employee.getCode());
                }
                
                // Create and save payslip
                Payslip payslip = new Payslip();
                payslip.setEmployee(employee);
                payslip.setHouseAmount(housingAmount);
                payslip.setTransportAmount(transportAmount);
                payslip.setEmployeeTaxAmount(taxAmount);
                payslip.setPensionAmount(pensionAmount);
                payslip.setMedicalInsuranceAmount(medicalAmount);
                payslip.setOtherAmount(otherAmount);
                payslip.setGrossSalary(grossSalary);
                payslip.setNetSalary(netSalary);
                payslip.setMonth(month);
                payslip.setYear(year);
                payslip.setStatus(Payslip.PayslipStatus.PENDING);
                
                processedPayslips.add(payslipRepository.save(payslip));
            } catch (Exception e) {
                throw new RuntimeException("Error processing payroll for employee " + employee.getCode() + ": " + e.getMessage());
            }
        }
        
        return processedPayslips.stream()
                .map(PayslipDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PayslipDTO> approvePayroll(Integer month, Integer year) {
        List<Payslip> pendingPayslips = payslipRepository.findByMonthAndYearAndStatus(
                month, year, Payslip.PayslipStatus.PENDING);
        
        if (pendingPayslips.isEmpty()) {
            throw new RuntimeException("No pending payslips found for month " + month + "/" + year);
        }
        
        List<Payslip> approvedPayslips = new ArrayList<>();
        
        for (Payslip payslip : pendingPayslips) {
            payslip.setStatus(Payslip.PayslipStatus.PAID);
            Payslip approvedPayslip = payslipRepository.save(payslip);
            approvedPayslips.add(approvedPayslip);
            
            // Generate message for the employee
            messageService.createPaymentMessage(approvedPayslip);
        }
        
        return approvedPayslips.stream()
                .map(PayslipDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private BigDecimal getDeductionPercentage(String deductionName) {
        return deductionRepository.findByDeductionName(deductionName)
                .map(Deduction::getPercentage)
                .orElseThrow(() -> new RuntimeException("Deduction not found with name: " + deductionName));
    }

    private BigDecimal calculatePercentage(BigDecimal amount, BigDecimal percentage) {
        return amount.multiply(percentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}