package com.orcus.hha_report_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orcus.hha_report_manager.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByIsDepartmentHead(boolean isDepartmentHead);

    List<Employee> findByDepartmentContains(String title);

    List<Employee> findByUsername(String username);
}
