package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username can't empty")
    private String username;

    @NotBlank(message = "Password can't empty")
    private String password;
}
