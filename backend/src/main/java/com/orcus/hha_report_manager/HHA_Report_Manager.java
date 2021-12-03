package com.orcus.hha_report_manager;

import com.orcus.hha_report_manager.controller.DepartmentController;
import com.orcus.hha_report_manager.controller.EmployeeController;
import com.orcus.hha_report_manager.model.Department;
import com.orcus.hha_report_manager.model.Employee;
import com.orcus.hha_report_manager.repository.DepartmentRepository;
import com.orcus.hha_report_manager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@SpringBootApplication
public class HHA_Report_Manager {
	public static void main(String[] args) {
		SpringApplication.run(HHA_Report_Manager.class, args);
	}
}
