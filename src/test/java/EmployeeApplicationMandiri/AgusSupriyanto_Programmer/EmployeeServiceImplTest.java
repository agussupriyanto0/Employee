package EmployeeApplicationMandiri.AgusSupriyanto_Programmer;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.EmployeeUpdateRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity.Employee;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception.DuplicateEmailException;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception.EmployeeNotFoundException;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.repository.EmployeeRepository;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;



    @Test
    void getAllSuccess(){

        Employee employee1 = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        Employee employee2 = Employee.builder()
                .id("2")
                .name("Agus Supriyanto")
                .email("agussupriyanto001999@gmail.com")
                .position("Software Engineering")
                .build();

        List<Employee> employees = List.of(employee1,employee2);

        when(repository.findAll())
                .thenReturn(employees);

        List<Employee> result = employeeService.getAll();

        assertEquals(2, result.size());

        assertEquals(
                "Agus Supriyanto",
                result.get(0).getName()
        );

        assertEquals(
                "Software Engineering",
                result.get(1).getPosition()
        );

        verify(repository, times(1))
                .findAll();
    }

    //GetByIdSuccess
    @Test
    void getByIdSuccess(){

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(employee));


        Employee result =
                employeeService.getById("1");

        assertEquals(
                "Agus Supriyanto",
                result.getName()
        );

        assertEquals(
                "Senior Production Support",
                result.getPosition()
        );

        // why call repository because repository have method mock
        verify(repository,times(1))
                .findById("1");
     }


     @Test
    void  createdSuccess(){

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        when(repository.existsByEmail("agussupriyanto228@gmail.com"))
                .thenReturn(false);

        when(repository.save(employee))
                .thenReturn(employee)     ;

        Employee result = employeeService.create(employee);

        assertEquals(
                "Agus Supriyanto",
                result.getName()
        );

        assertEquals(
                "agussupriyanto228@gmail.com",
                result.getEmail()
        );

        verify(repository,times(1))
                .existsByEmail("agussupriyanto228@gmail.com");

        verify(repository,times(1))
                .save(employee);


    }

    @Test
    void updateSuccess(){

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus S")
                .email("agussupriyanto28@gmail.com")
                .position("Senior Production Support")
                .build();

        EmployeeUpdateRequest request = new EmployeeUpdateRequest();

        request.setName("Agus Supriyanto");
        request.setEmail("agussupriyanto228@gmail.com");

        when(repository.findById("1"))
                .thenReturn(Optional.of(employee));

        when(repository.existsByEmail("agussupriyanto228@gmail.com"))
                .thenReturn(false);

        when(repository.save(any(Employee.class)))
                .thenReturn(employee);

        Employee result =employeeService.update("1",request);

        assertEquals(
                "Agus Supriyanto",
                result.getName());

        assertEquals(
                "agussupriyanto228@gmail.com",
                result.getEmail()
        );

        verify(repository,times(1))
                .findById("1");

        verify(repository,times(1))
                .save(employee);

    }


    @Test
    void updateFailedEmployeeNotFound(){

        EmployeeUpdateRequest request = new EmployeeUpdateRequest();

        request.setName("Agus Update");

        when(repository.findById("1"))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeService.update("1",request)
                );

        assertEquals(
                "Employee Not Found",
                exception.getMessage());

        verify(repository,never())
                .save(any());
    }

    @Test
    void updateFailedDuplicateEmail(){

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        EmployeeUpdateRequest request = new EmployeeUpdateRequest();

        request.setEmail("agussupriyanto00199@gmail.com");

        when(repository.findById("1"))
                .thenReturn(Optional.of(employee));

        when(repository.existsByEmail("agussupriyanto00199@gmail.com"))
                .thenReturn(true);

        DuplicateEmailException exception =
                assertThrows(
                        DuplicateEmailException.class,
                        () -> employeeService.update("1",request)
                );

        assertEquals(
                "Email Already Exists",
                exception.getMessage()
        );

        verify(repository,never())
                .save(any());
    }


    @Test
    void deleteSuccess(){

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(employee));

        employeeService.delete("1");

        verify(repository,times(1))
                .findById("1");

        verify(repository,times(1))
                .delete(employee);
    }

    @Test
    void getByIdNotFound(){

        when(repository.findById("1"))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeService.getById("1")
                );

        assertEquals(
                "Employee Not Found",
                exception.getMessage()
        );

        verify(repository,times(1))
                .findById("1");
    }


    @Test
    void createDuplicateEmail(){

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        when(repository.existsByEmail("agussupriyanto228@gmail.com"))
                .thenReturn(true);

        DuplicateEmailException exception =
                assertThrows(DuplicateEmailException.class,
                        () -> employeeService.create(employee));

        assertEquals(
                "Email Already Exists",
                exception.getMessage());

        verify(repository,never())
                .save(any());
    }

    @Test
    void deleteFailedEmployeeNotFound(){

        when(repository.findById("1"))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeService.delete("1")
                );

        assertEquals(
                "Employee Not Found",
                exception.getMessage()
        );

        verify(repository,never())
                .delete(any());
    }

    @Test
    void getAllWithPaginationSuccess(){

        Pageable pageable = PageRequest.of(0,10);

        List<Employee> employees = List.of(
                Employee.builder()
                        .id("1")
                        .name("Agus Supriyanto")
                        .build()
        );

        Page<Employee> page =
                new PageImpl<>(employees);

        when(repository.findAll(pageable))
                .thenReturn(page);

        Page<Employee> result = employeeService.getAll(pageable);

        assertEquals(1, result.getContent().size());

        verify(repository,times(1))
                .findAll(pageable);
    }

    @Test
    void searchByNameSuccess(){

        Pageable pageable = PageRequest.of(0,10);

        Employee employee =
                Employee.builder()
                        .id("1")
                        .name("Agus Supriyanto")
                        .build();

        Page<Employee> page =
                new PageImpl<>(List.of(employee));

        when(repository.findByNameContainingIgnoreCase(
                "Agus Supriyanto",
                pageable))
                .thenReturn(page);

        Page<Employee> result =
                employeeService.searchByName(
                        "Agus Supriyanto",
                        pageable
                );

        assertEquals(
                1,
                result.getContent().size()
        );

        assertEquals(
                "Agus Supriyanto",
                result.getContent().get(0).getName()
        );

        verify(repository,times(1))
                .findByNameContainingIgnoreCase(
                        "Agus Supriyanto",
                        pageable);

    }
}
