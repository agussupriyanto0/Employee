package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.controller;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.*;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity.Employee;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.mapper.EmployeeMapper;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service.EmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    private final EmployeeMapper mapper;
    //GetAll
    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<EmployeeResponse>>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int  page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue =  "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size);

        //Ditampuang disini
        //Kenapa pakai Page karena page bisa ada info pagination (total,halaman,dll) daripada list cuman nampilkan saja
        Page<Employee> employeePage;

        if (StringUtils.hasText(name)) {
            employeePage = service.searchByName(name, pageable);
        } else {
            employeePage = service.getAll(pageable);
        }

        //Disini kita bikin database mnejadi lebih strukutr nggak langsung lempar aja hasil dari database
        List<EmployeeResponse> responses =
                employeePage.getContent()
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        PaginationResponse<EmployeeResponse>paginationResponse =
                PaginationResponse.<EmployeeResponse>builder()
                        .page(employeePage.getNumber())
                        .size(employeePage.getSize())
                        .totalData(employeePage.getTotalElements())
                        .totalPages(employeePage.getTotalPages())
                        .items(responses)
                        .build();

        return ResponseEntity.ok(ApiResponse.success("Success",paginationResponse));
    }
    //Get By Id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getById(@PathVariable String id){
        Employee employee = service.getById(id);


        return ResponseEntity.ok(ApiResponse.success("Employee Found", mapper.toResponse(employee)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponse>> create (@Valid @RequestBody EmployeeRequest request){

        System.out.println("Create Endpoint Hit");

        Employee employee = mapper.toEntity(request);

        Employee savedEmployee = service.create(employee);

        return ResponseEntity.status(200).body(ApiResponse.success("Employee Created Successfully", mapper.toResponse(savedEmployee)));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> update (
                                            @PathVariable String id,
                                            @Valid
                                            @RequestBody EmployeeUpdateRequest request){

        Employee updateEmployee = service.update(id, request);

        return ResponseEntity.ok(ApiResponse.success("Employee Updated Successfully",mapper.toResponse(updateEmployee)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete (@PathVariable String id){

        service.delete(id);

        return ResponseEntity.ok(ApiResponse.success("Employee Delete Successfully",id));
    }


}
