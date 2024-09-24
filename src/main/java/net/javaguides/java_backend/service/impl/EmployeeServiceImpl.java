package net.javaguides.java_backend.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.java_backend.dto.EmployeeDto;
import net.javaguides.java_backend.entity.Employee;
import net.javaguides.java_backend.exception.ResourceNotFoundException;
import net.javaguides.java_backend.mapper.EmployeeMapper;
import net.javaguides.java_backend.repository.EmployeeRepository;
import net.javaguides.java_backend.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourceNotFoundException("Employee is not exist with given id:"+employeeId));
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(EmployeeMapper::mapToEmployeeDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {
        Employee employee =  employeeRepository.findById(employeeId).orElseThrow(
                ()->new ResourceNotFoundException("Employee is not exists with given id : "+employeeId));

        if (updatedEmployee.getFirstName() != null && !updatedEmployee.getFirstName().isEmpty()) {
            employee.setFirstName(updatedEmployee.getFirstName());
        }

        // Only update lastName if it's not null or empty
        if (updatedEmployee.getLastName() != null && !updatedEmployee.getLastName().isEmpty()) {
            employee.setLastName(updatedEmployee.getLastName());
        }

        // Only update email if it's not null or empty
        if (updatedEmployee.getEmail() != null && !updatedEmployee.getEmail().isEmpty()) {
            employee.setEmail(updatedEmployee.getEmail());
        }
        Employee updatedEmployeeObj = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee is not exists with given id : "+employeeId)
        );
        employeeRepository.deleteById(employeeId);
    }
}
