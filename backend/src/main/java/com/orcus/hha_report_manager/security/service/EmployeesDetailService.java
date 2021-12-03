package com.orcus.hha_report_manager.security.service;

import com.orcus.hha_report_manager.model.Employee;
import com.orcus.hha_report_manager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeesDetailService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var found = employeeRepository.findByUsername(username);
        if (found.isEmpty())
            throw new UsernameNotFoundException(username + " not found");
        var employeeFound = found.get(0);
        var name = employeeFound.getUsername();
        var pw = employeeFound.accessPassword();
        var roles = getUserAuthorities(employeeFound);
        return new User(name, pw, true, true, true, true, roles);
    }

    private List<SimpleGrantedAuthority> getUserAuthorities(Employee employee) {
        var auths = new ArrayList<SimpleGrantedAuthority>();
        if (employee.isAdmin())
            auths.add(new SimpleGrantedAuthority("ADMIN"));
        if (employee.isDepartmentHead())
            auths.add(new SimpleGrantedAuthority("DEPARTMENT_HEAD"));
        if (auths.isEmpty())
            auths.add(new SimpleGrantedAuthority("N/A"));
        return auths;
    }
}
