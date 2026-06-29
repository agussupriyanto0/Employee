package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {

    @NotBlank(message = "Name Can't Empty")
    private String name;

    @Email(message = "Your Format is Wrong")
    @NotBlank(message = "Email Can't Empty")
    private String email;

    @NotBlank(message = "Position Can't Empty")
    private String position;
}
