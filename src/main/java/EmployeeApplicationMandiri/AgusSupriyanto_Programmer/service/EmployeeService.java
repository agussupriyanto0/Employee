package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.EmployeeUpdateRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    //Function

    //GetAll
    List<Employee> getAll();

    //Search ByName

    //Get By ID
    Employee getById (String id);

    //Create
    Employee create (Employee employee);

    //Update
    Employee update (String id, EmployeeUpdateRequest employeeUpdateRequest);

    //Delete
    void delete (String id);

    //Page
    Page<Employee> getAll (Pageable pageable);
    Page<Employee> searchByName(String name, Pageable pageable);
}
