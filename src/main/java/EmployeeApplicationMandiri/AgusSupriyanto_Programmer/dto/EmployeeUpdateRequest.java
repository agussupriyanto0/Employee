package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeUpdateRequest {

    private String name;

    @Email(message = "Email format invalid")
    private String email;

    private String position;

}
