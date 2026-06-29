package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T>{

    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(int status, String message, T data){
        return new ApiResponse<>(status,message,data);
    }

    public static <T> ApiResponse<T> success(String message,T data){
        return  new ApiResponse<>(200,message,data);
    }

    public static <T> ApiResponse<T> created (String message,T data){
        return new ApiResponse<>(201,message,data);
    }
}
