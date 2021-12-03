package com.orcus.hha_report_manager.controller;

import com.orcus.hha_report_manager.model.Department;
import com.orcus.hha_report_manager.model.Employee;
import com.orcus.hha_report_manager.repository.DepartmentRepository;
import com.orcus.hha_report_manager.repository.EmployeeRepository;
import com.orcus.hha_report_manager.security.SignedJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()));

            return new ResponseEntity<>(makeAuthResponse(authRequest.username), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    private AuthResponse makeAuthResponse(String username) {
        var userDetails = userDetailsService.loadUserByUsername(username);
        var employees = employeeRepository.findByUsername(userDetails.getUsername());
        if (employees.isEmpty())
            throw new UsernameNotFoundException(userDetails.getUsername());
        var employee = employees.get(0);

        final Department department;
        if (employee.isAdmin()) {
            department = new Department("admin", "");
        } else {
            var departments = departmentRepository.findByName(employees.get(0).getDepartment());
            if (departments.isEmpty())
                throw new UsernameNotFoundException(userDetails.getUsername());
            department = departments.get(0);
        }

        return new AuthResponse(SignedJwt.make(userDetails),
                department.getName(),
                department.getId());
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public static class Unauthorized extends RuntimeException {
        public Unauthorized() {
            super();
        }
    }

    public static class AuthRequest {
        private String username;
        private String password;

        public AuthRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class AuthResponse {
        private final String jwt;
        private final String department;
        private final long departmentId;

        public String getJwt() {
            return jwt;
        }

        public String getDepartment() {
            return department;
        }

        public long getDepartmentId() {
            return departmentId;
        }

        public AuthResponse(String jwt, String department, long departmentId) {
            this.jwt = jwt;
            this.department = department;
            this.departmentId = departmentId;
        }
    }
}
