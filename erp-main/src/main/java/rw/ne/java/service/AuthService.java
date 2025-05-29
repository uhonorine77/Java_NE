package rw.ne.java.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rw.ne.java.dto.auth.JwtResponse;
import rw.ne.java.dto.auth.LoginRequest;
import rw.ne.java.dto.auth.SignupRequest;
import rw.ne.java.model.Employee;
import rw.ne.java.repository.EmployeeRepository;
import rw.ne.java.security.JwtTokenProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager, EmployeeRepository employeeRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        Employee employee = (Employee) authentication.getPrincipal();
        return new JwtResponse(jwt, employee.getCode(), employee.getEmail(), 
                employee.getFirstName(), employee.getLastName(), employee.getRoles());
    }

    public Employee registerUser(SignupRequest signupRequest) {
        if (employeeRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new employee's account
        Employee employee = new Employee();
        employee.setCode("EMP" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        employee.setFirstName(signupRequest.getFirstName());
        employee.setLastName(signupRequest.getLastName());
        employee.setEmail(signupRequest.getEmail());
        employee.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        employee.setMobile(signupRequest.getMobile());
        employee.setDateOfBirth(signupRequest.getDateOfBirth());
        employee.setStatus(Employee.EmployeeStatus.ACTIVE);

        // Set roles
        List<String> roles = signupRequest.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = new ArrayList<>();
            roles.add("ROLE_EMPLOYEE");
        }
        employee.setRoles(roles);

        return employeeRepository.save(employee);
    }
}