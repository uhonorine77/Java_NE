package rw.ne.java.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String code;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public JwtResponse(String token, String code, String email, String firstName, String lastName, List<String> roles) {
        this.token = token;
        this.code = code;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}