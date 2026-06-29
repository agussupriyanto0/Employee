package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.mapper;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.EmployeeRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.EmployeeResponse;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    //Response dari client ngambil dari database
    public EmployeeResponse toResponse(Employee employee){
        return  EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .position(employee.getPosition())
                .build();
    }

    //Simpan ke database dari Request
    public Employee toEntity (EmployeeRequest request){
        return Employee.builder()
                .name(request.getName())
                .email(request.getEmail())
                .position(request.getPosition())
                .build();
    }
}
