package rw.ne.java.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ne.java.model.Employee;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private String code;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private LocalDate dateOfBirth;
    private List<String> roles;
    private Employee.EmployeeStatus status;

    // Convert Entity to DTO
    public static EmployeeDTO fromEntity(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setCode(employee.getCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setMobile(employee.getMobile());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setRoles(employee.getRoles());
        dto.setStatus(employee.getStatus());
        return dto;
    }
}