package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    //Expect dari user, status,message, errors

    private int status;
    private String message;
    private Map<String,String> errors;


}
