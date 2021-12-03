package com.orcus.hha_report_manager.security.beans;

import com.orcus.hha_report_manager.model.Department;
import com.orcus.hha_report_manager.model.Employee;
import com.orcus.hha_report_manager.repository.DepartmentRepository;
import com.orcus.hha_report_manager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class HTTPRequestUser {
    private String username;

    private Department department;

    private Employee employee;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Department getDepartment() {
        // Lazy instantiation
        if (this.department == null) {
            this.department = getDepartmentByName();
        }
        return this.department;
    }

    private Department getDepartmentByName() {
        var employee = getEmployee();
        var departments = departmentRepository.findByName(employee.getDepartment());
        if (departments.isEmpty())
            throw new HTTPRequestUserException("User department " + employee.getDepartment() + " not found");
        return departments.get(0);
    }

    public Employee getEmployee() {
        // Lazy instantiation
        if (this.employee == null) {
            this.employee = getEmployeeByName();
        }
        return this.employee;
    }

    private Employee getEmployeeByName() {
        var employees = employeeRepository.findByUsername(this.username);
        if (employees.isEmpty())
            throw new HTTPRequestUserException("Username " + this.username +" not found");
        return employees.get(0);
    }

    public static class HTTPRequestUserException extends RuntimeException {
        public HTTPRequestUserException(String message) {
            super(message);
        }
    }
}
