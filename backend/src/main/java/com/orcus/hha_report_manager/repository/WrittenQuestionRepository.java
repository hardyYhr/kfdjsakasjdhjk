package com.orcus.hha_report_manager.repository;

import com.orcus.hha_report_manager.model.Question;
import com.orcus.hha_report_manager.model.WrittenQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WrittenQuestionRepository extends JpaRepository<WrittenQuestion, Long> {

    List<Question> findByQuestionContains(String question);

    //List<Question> findByAnswerContains(String answer);
}
