package com.orcus.hha_report_manager.repository;

import com.orcus.hha_report_manager.model.Message;
import com.orcus.hha_report_manager.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Message> findByUsername(String username);

    List<Message> findByDepartmentContains(String department);
}
