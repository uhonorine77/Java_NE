package rw.ne.java.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ne.java.dto.auth.JwtResponse;
import rw.ne.java.dto.auth.LoginRequest;
import rw.ne.java.dto.auth.SignupRequest;
import rw.ne.java.dto.common.ApiResponse;
import rw.ne.java.dto.employee.EmployeeDTO;
import rw.ne.java.model.Employee;
import rw.ne.java.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(ApiResponse.success("User authenticated successfully", jwtResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            Employee employee = authService.registerUser(signupRequest);
            EmployeeDTO employeeDTO = EmployeeDTO.fromEntity(employee);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", employeeDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}