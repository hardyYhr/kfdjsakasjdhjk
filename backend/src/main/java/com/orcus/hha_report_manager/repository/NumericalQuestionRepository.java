package com.orcus.hha_report_manager.repository;

import com.orcus.hha_report_manager.model.NumericalQuestion;
import com.orcus.hha_report_manager.model.Question;
import com.orcus.hha_report_manager.model.WrittenQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NumericalQuestionRepository extends JpaRepository<NumericalQuestion, Long> {

    List<Question> findByQuestionContains(String question);

    //List<Question> findByAnswerContains(String answer);
}
