package EmployeeApplicationMandiri.AgusSupriyanto_Programmer;


import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.controller.EmployeeController;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.EmployeeRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.EmployeeResponse;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.EmployeeUpdateRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity.Employee;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception.*;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.mapper.EmployeeMapper;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.repository.UserRepository;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service.EmployeeService;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private  EmployeeService employeeService;

    @MockitoBean
    private EmployeeMapper mapper;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getByIdSuccess() throws Exception {

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();


        when(employeeService.getById("1"))
                .thenReturn(employee);

        when(mapper.toResponse(employee))
                .thenReturn(response);

        mockMvc.perform(get("/api/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value(200))
                .andExpect(jsonPath("$.message")
                        .value("Employee Found"))
                .andExpect(jsonPath("$.data.id")
                        .value("1"))
                .andExpect(jsonPath("$.data.name")
                        .value("Agus Supriyanto"))
                .andExpect(jsonPath("$.data.email")
                        .value("agussupriyanto228@gmail.com"))
                .andExpect(jsonPath("$.data.position")
                        .value("Senior Production Support"))

        ;

    }

    @Test
    void getByIdNotFound() throws Exception {

        when(employeeService.getById("1"))
                .thenThrow(
                        new EmployeeNotFoundException(
                                "Employee Not Found"
                        )
                );


        mockMvc.perform(get("/api/employee/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Employee Not Found"));
    }

    @Test
    void deleteSuccess()throws Exception{



        //Kalau void maka menggunakan doNothing, kalau
        doNothing()
                .when(employeeService)
                .delete("1");

        mockMvc.perform(delete("/api/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Employee Delete Successfully"))
                .andExpect(jsonPath("$.data")
                        .value("1"));
    }


    @Test
    void deleteFailedEmployeeNotFound() throws Exception {

        doThrow(
                new EmployeeNotFoundException(
                        "Employee Not Found"
                ))
                .when(employeeService)
                .delete("1");

        mockMvc.perform(delete("/api/employee/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Employee Not Found"));
    }


    @Test
    void handleNotFoundSuccess(){

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler();

        EmployeeNotFoundException exception =
                new EmployeeNotFoundException(
                        "Employee Not Found"
                );

        ErrorResponse errorResponse =
                handler.handleNotFound(exception);

        assertEquals(
                404,
                errorResponse.getStatus()
        );

        assertEquals(
                "Employee Not Found",
                errorResponse.getMessage()
        );

        assertNull(
                errorResponse.getErrors()
        );
    }


    @Test
    void handleDuplicateEmailSuccess(){

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler();

        DuplicateEmailException exception =
                new DuplicateEmailException(
                        "Email Already Exists"
                );

        ErrorResponse response =
                handler.handleEmailDuplicate(exception);

        assertEquals(
                409,
                response.getStatus()
        );

        assertEquals(
                "Email Already Exists",
                response.getMessage()
        );

        assertNull(
                response.getErrors()
        );

    }

    @Test
    void handleUsernameAlreadyExistsSuccess(){

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler();

        UsernameAlreadyExistsException exception =
                new UsernameAlreadyExistsException(
                        "Username Already Exists"
                );

        ErrorResponse response =
                handler.handleUsernameAlready(exception);

        assertEquals(
                409,
                response.getStatus()
        );

        assertEquals(
                "Username Already Exists",
                response.getMessage()
        );

        assertNull(
                response.getErrors()
        );
    }


    @Test
    void handleInvalidCredentialSuccess(){

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler();

        InvalidCrendentialException exception =
                new InvalidCrendentialException(
                        "Username or Password Wrong"
                );

        ErrorResponse response =
                handler.handleInvalidCrendetial(exception);

        assertEquals(
                401,
                response.getStatus()
        );

        assertEquals(
                "Username or Password Wrong",
                response.getMessage()
        );

        assertNull(
                response.getErrors()
        );
    }

    @Test
    void createSuccess() throws Exception {

        EmployeeRequest request = EmployeeRequest.builder()
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();


        when(mapper.toEntity(any(EmployeeRequest.class)))
                .thenReturn(employee);

        when(employeeService.create(employee))
                .thenReturn(employee);

        when(mapper.toResponse(employee))
                .thenReturn(response);

        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message")
                        .value("Employee Created Successfully"))
                .andExpect(jsonPath("$.data.id")
                        .value("1"))
                .andExpect(jsonPath("$.data.name")
                        .value("Agus Supriyanto"))
                .andExpect(jsonPath("$.data.email")
                        .value("agussupriyanto228@gmail.com"))
                .andExpect(jsonPath("$.data.position")
                        .value("Senior Production Support"));


    }


    @Test
    void createDuplicateEmail() throws Exception {

        EmployeeRequest request = EmployeeRequest.builder()
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        Employee employee = Employee.builder()
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Senior Production Support")
                .build();

        when(mapper.toEntity(any(EmployeeRequest.class)))
                .thenReturn(employee);

        when(employeeService.create(employee))
                .thenThrow(
                        new DuplicateEmailException(
                                "Email Already Exists"
                        )
                );

        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status")
                        .value(409))
                .andExpect(jsonPath("$.message")
                        .value("Email Already Exists"));


    }



    @Test
    void createValidationFailed() throws Exception {

        EmployeeRequest request = EmployeeRequest.builder()
                .name("")
                .email("")
                .position("")
                .build();

        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation error"));

    }

    @Test
    void updateSuccess() throws Exception {

        EmployeeUpdateRequest request = EmployeeUpdateRequest.builder()
                .name("Agus Supriyanto")
                .email("agusupriyanto228@gmial.com")
                .position("Software Engineer")
                .build();

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyantoo")
                .email("agussupriyanto228@gmail.com")
                .position("Production Support")
                .build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id("1")
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Software Engineer")
                .build();

        when(employeeService.update(
                eq("1"),
                any(EmployeeUpdateRequest.class)))
                .thenReturn(employee);

        when(mapper.toResponse(employee))
                .thenReturn(response);

        mockMvc.perform(patch("/api/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value(200))
                .andExpect(jsonPath("$.message")
                        .value("Employee Updated Successfully"))
                .andExpect(jsonPath("$.data.id")
                        .value("1"))
                .andExpect(jsonPath("$.data.name")
                        .value("Agus Supriyanto"))
                .andExpect(jsonPath("$.data.position")
                        .value("Software Engineer"))
                .andExpect(jsonPath("$.data.email")
                        .value("agussupriyanto228@gmail.com"));


    }


    @Test
    void updateFailedDuplicateEmail() throws Exception {

        EmployeeUpdateRequest request = EmployeeUpdateRequest.builder()
                .email("agussupriyanto228@gmail.com")
                .build();

        when(employeeService.update(
                eq("1"),
                any(EmployeeUpdateRequest.class)
        )).thenThrow( new DuplicateEmailException(
                "Email Already Exists"
        ));

        mockMvc.perform(patch("/api/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status")
                        .value(409))
                .andExpect(jsonPath("$.message")
                        .value("Email Already Exists"));


    }

    @Test
    void  updateValidationFailed() throws Exception {

        EmployeeUpdateRequest request = EmployeeUpdateRequest.builder()
                .email("agussuipriyant2323o222222.com")
                .build();

        mockMvc.perform(patch("/api/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation error"))
                .andExpect(jsonPath("$.errors.email")
                        .exists());
    }

    @Test
    void updateEmployeeNotFound() throws Exception {

        EmployeeUpdateRequest request = EmployeeUpdateRequest.builder()
                .name("Agus Supriyanto")
                .email("agussupriyanto228@gmail.com")
                .position("Production Support")
                .build();

        when(employeeService.update(
                eq("1"),
                any(EmployeeUpdateRequest.class)
        )).thenThrow(
                new EmployeeNotFoundException(
                        "Employee Not Found"
                )
        );

        mockMvc.perform(patch("/api/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.message")
                        .value("Employee Not Found"));
    }

    @Test
    void getAllSuccess() throws Exception {

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .position("Asisten Manager Production Support")
                .email("agussupriyanto228@gmail.com")
                .build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id("1")
                .name("Agus Supriyanto")
                .position("Asisten Manager Production Support")
                .email("agussupriyanto228@gmail.com")
                .build();


        Page<Employee> employeePage =
                new PageImpl<>(
                        List.of(employee),
                        PageRequest.of(0,5),
                        1
                );

        when(employeeService.getAll(any(Pageable.class)))
                .thenReturn(employeePage);

        when(mapper.toResponse(employee))
                .thenReturn(response);

        mockMvc.perform(get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value(200))
                .andExpect(jsonPath("$.message")
                        .value("Success"))

                .andExpect(jsonPath("$.data.page")
                        .value(0))
                .andExpect(jsonPath("$.data.size()")
                        .value(5))
                .andExpect(jsonPath("$.data.totalData")
                        .value(1))
                .andExpect(jsonPath("$.data.totalPages")
                        .value(1))

                .andExpect(jsonPath("$.data.items[0].id")
                        .value("1"))
                .andExpect(jsonPath("$.data.items[0].name")
                        .value("Agus Supriyanto"))
                .andExpect(jsonPath("$.data.items[0].email")
                        .value("agussupriyanto228@gmail.com"))
                .andExpect(jsonPath("$.data.items[0].position")
                        .value("Asisten Manager Production Support"));
    }


    @Test
    void searchByNameSuccess() throws Exception {

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .position("Asisten Manager Production Support")
                .email("agussupriyanto228@gmail.com")
                .build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id("1")
                .name("Agus Supriyanto")
                .position("Asisten Manager Production Support")
                .email("agussupriyanto228@gmail.com")
                .build();

        Page<Employee> employeePage = new PageImpl<>(
                List.of(employee),
                PageRequest.of(0,5),1
        );

        when(employeeService.searchByName(
                eq("Agus"),
                any(Pageable.class)
        )).thenReturn(employeePage);

        when(mapper.toResponse(employee))
                .thenReturn(response);

        mockMvc.perform(get("/api/employee")
                .param("name","Agus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value(200))
                .andExpect(jsonPath("$.data.totalData")
                        .value(1))
                .andExpect(jsonPath("$.data.items[0].name")
                        .value("Agus Supriyanto"));

        verify(employeeService).searchByName(
                eq("Agus"),
                any(Pageable.class)
        );
        verify(employeeService, never())
                .getAll(any(Pageable.class));
    }

    @Test
    void getAllWithPagination() throws Exception {

        Employee employee = Employee.builder()
                .id("1")
                .name("Agus Supriyanto")
                .position("Asisten Manager Production Support")
                .email("agussupriyanto228@gmail.com")
                .build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id("1")
                .name("Agus Supriyanto")
                .position("Asisten Manager Production Support")
                .email("agussupriyanto228@gmail.com")
                .build();
        Page<Employee> employeePage = new PageImpl<>(
                List.of(employee),
                PageRequest.of(1, 10),
                20
        );

        when(employeeService.getAll(any(Pageable.class)))
                .thenReturn(employeePage);

        when(mapper.toResponse(employee))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/employee")
                                .param("page", "1")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.totalData").value(20))
                .andExpect(jsonPath("$.data.totalPages").value(2));


    }
}
