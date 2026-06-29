package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.EmployeeUpdateRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity.Employee;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception.DuplicateEmailException;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception.EmployeeNotFoundException;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository repository;

    @Override
    public List<Employee> getAll() {
        return repository.findAll();
    }

    @Override
    public Employee getById(String id) {
        //Apabila tidak ada maka menggunakan optional
        Optional<Employee> optionalEmployee = repository.findById(id);

        if (optionalEmployee.isPresent()){
            return optionalEmployee.get();
        }else{
            throw new EmployeeNotFoundException("Employee Not Found");
        }
    }

    @Override
    public Employee create(Employee employee) {

        if (repository.existsByEmail(employee.getEmail())){
            throw new DuplicateEmailException("Email Already Exists");
        }
        return repository.save(employee);
    }

    @Override
    public Employee update(String id, EmployeeUpdateRequest request) {
        Employee update = getById(id);

        if(request.getEmail() != null &&
                !request.getEmail().equals(update.getEmail())&&
                repository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException("Email Already Exists");
        }

        if (request.getName() != null){
            update.setName(request.getName());
        }

        if (request.getEmail() != null){
            update.setEmail(request.getEmail());
        }

        if(request.getPosition() != null){
            update.setPosition(request.getPosition());
        }

        return repository.save(update);
    }

    @Override
    public void delete(String id) {
        Employee employee = getById(id);
        repository.delete(employee);

        //ini lebih baik di controller aja
        //return "Employee Name " + employee.getName() + " With ID : " + employee.getId();
    }


    @Override
    public Page<Employee> getAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    @Override
    public Page<Employee> searchByName(String name, Pageable pageable){
        return repository.findByNameContainingIgnoreCase(name,pageable);
    }

}
