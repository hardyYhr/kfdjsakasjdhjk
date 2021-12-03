package com.orcus.hha_report_manager.repository;

import com.orcus.hha_report_manager.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByName(String name);

    List<Department> findByNameContains(String name);
}
