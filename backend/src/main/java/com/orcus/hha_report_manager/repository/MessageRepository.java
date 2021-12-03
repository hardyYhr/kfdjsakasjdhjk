package com.orcus.hha_report_manager.repository;

import com.orcus.hha_report_manager.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByUsername(String username);

    List<Message> findByDepartmentContains(String department);
}
