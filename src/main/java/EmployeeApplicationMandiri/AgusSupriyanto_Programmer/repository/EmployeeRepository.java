package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.repository;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

     boolean existsByEmail(String email);

     Page<Employee> findByNameContainingIgnoreCase (
             String name,
             Pageable pageable
     );

}
