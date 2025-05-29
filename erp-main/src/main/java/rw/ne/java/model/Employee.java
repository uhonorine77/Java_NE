package rw.ne.java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements UserDetails {

    @Id
    @Column(unique = true)
    private String code;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_roles", joinColumns = @JoinColumn(name = "employee_code"))
    @Column(name = "role")
    private List<String> roles;

    private String mobile;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Employment> employments = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Payslip> payslips = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == EmployeeStatus.ACTIVE;
    }

    public enum EmployeeStatus {
        ACTIVE, DISABLED
    }
}
