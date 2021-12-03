package com.orcus.hha_report_manager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.orcus.hha_report_manager.security.beans.HTTPRequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orcus.hha_report_manager.model.Employee;
import com.orcus.hha_report_manager.repository.EmployeeRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class EmployeeController {

    private static final Integer INITIAL_SCORE = 0;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HTTPRequestUser httpRequestUser;

    @GetMapping("/employee")
    public ResponseEntity<Employee> getEmployee(){
        return new ResponseEntity<>(httpRequestUser.getEmployee(), HttpStatus.OK);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam(required = false) String department, String username) {
        try {
            List<Employee> employees = new ArrayList<Employee>();
            if (department == null && username == null)
                employeeRepository.findAll().forEach(employees::add);
            else if (username == null && department != null) {
                employeeRepository.findByDepartmentContains(department).forEach(employees::add);
            }
            else if (username != null && department == null){
                employeeRepository.findByUsername(username).forEach(employees::add);
            }
            else if(department != null && username != null){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id) {
        Optional<Employee> employeeData = employeeRepository.findById(id);

        if (employeeData.isPresent()) {
            return new ResponseEntity<>(employeeData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        try {
            if(employee.accessPassword() == null || employee.accessPassword().isEmpty())
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            employee.setPassword(passwordEncoder.encode(employee.accessPassword()));
            Employee newEmployee = employeeRepository.save(employee);
            return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/employee")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee newEmployee){
        var oldEmployee = httpRequestUser.getEmployee();
        var updatedEmployee = updateEmployee(newEmployee, oldEmployee);
        return new ResponseEntity<>(employeeRepository.save(updatedEmployee), HttpStatus.OK);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long id, @RequestBody Employee employee) {
        Optional<Employee> employeeData = employeeRepository.findById(id);

        if (employeeData.isPresent()) {
            Employee employeeToChange = employeeData.get();
            var updatedEmployee = updateEmployee(employee, employeeToChange);
            return new ResponseEntity<>(employeeRepository.save(updatedEmployee), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Update oldEmployee is newEmployee's values except id and username.
     * Null values are skipped.
     * */
    private Employee updateEmployee(Employee newEmployee, Employee oldEmployee) {
        if(Objects.nonNull(newEmployee.getFirstName())){
            oldEmployee.setFirstName(newEmployee.getFirstName());
        }
        if(Objects.nonNull(newEmployee.getLastName())){
            oldEmployee.setLastName(newEmployee.getLastName());
        }
        if(Objects.nonNull(newEmployee.getDepartment())){
            oldEmployee.setDepartment(newEmployee.getDepartment());
        }
        if(Objects.nonNull(newEmployee.getScore())){
            oldEmployee.setScore(newEmployee.getScore());
        }
        if(Objects.nonNull(newEmployee.isDepartmentHead())){
            oldEmployee.setDepartmentHead(newEmployee.isDepartmentHead());
        }
        return oldEmployee;
    }

    @PutMapping("/employees/{id}/score")
    public ResponseEntity<Employee> incrementEmployeeScore(@PathVariable("id") long id, @RequestParam(required = false) Integer amount) {
        Optional<Employee> employeeData = employeeRepository.findById(id);

        if (employeeData.isPresent()) {
            Employee employeeToChange = employeeData.get();
            if(employeeToChange.getScore() == null){
                employeeToChange.setScore(0);
            }
            if(amount != null){
                employeeToChange.setScore(employeeToChange.getScore() + amount);
            }
            else {
                employeeToChange.setScore(employeeToChange.getScore() + 1);
            }
            return new ResponseEntity<>(employeeRepository.save(employeeToChange), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable("id") long id) {
        try {
            employeeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/employees")
    public ResponseEntity<HttpStatus> deleteAllEmployees() {
        try {
            employeeRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/employees/departmentheads")
    public ResponseEntity<List<Employee>> findByPublished() {
        try {
            List<Employee> employees = employeeRepository.findByIsDepartmentHead(true);

            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}