package com.orcus.hha_report_manager.controller;

import com.orcus.hha_report_manager.ReportManagerUtilities;
import com.orcus.hha_report_manager.model.MultipleChoiceQuestion;
import com.orcus.hha_report_manager.model.NewReport;
import com.orcus.hha_report_manager.model.Question;
import com.orcus.hha_report_manager.model.Report;
import com.orcus.hha_report_manager.repository.NewReportRepository;
import com.orcus.hha_report_manager.repository.QuestionRepository;
import com.orcus.hha_report_manager.security.beans.HTTPRequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class QuestionController {

    ReportManagerUtilities reportManagerUtilities = new ReportManagerUtilities();

    @Autowired
    NewReportRepository newReportRepository;

    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("/newreports/{id}/questions")
    public ResponseEntity<List<Question>> getAllQuestionsFromNewReportById(@PathVariable("id") long id){
        List<NewReport> reports = new ArrayList<NewReport>();
        Optional<NewReport> reportData = newReportRepository.findById(id);
        if(reportData.isPresent()){
            List<Question> questions = reportData.get().getQuestions();
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/newreports/{id}/questions")
    public ResponseEntity<Question> addQuestionToNewReport(@PathVariable("id") long id, @RequestBody Question question) {
        Optional<NewReport> reportData = newReportRepository.findById(id);
        if (reportData.isPresent()) {
            NewReport report = reportData.get();
            report.getQuestions().add(question);
            Question newQuestion = questionRepository.save(question);
            newReportRepository.save(report);
            return new ResponseEntity<>(question, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/newreports/questions/{id}")
    public ResponseEntity<Question> editQuestion(@PathVariable("id") long id, @RequestBody Question question) {
        Optional<Question> questionData = questionRepository.findById(id);
        if (questionData.isPresent()) {
            Question questionToUpdate = reportManagerUtilities.replaceNonNullQuestionFields(question, questionData.get());
            return new ResponseEntity<>(questionRepository.save(questionToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/newreports/questions/{id}/answer")
    public ResponseEntity<Question> answerQuestion(@PathVariable("id") long id, @RequestBody String answer) {
        Optional<Question> questionData = questionRepository.findById(id);
        if (questionData.isPresent()) {
            Question questionToUpdate = questionData.get();
            if(Objects.nonNull(answer)){
                if(questionToUpdate.getType().equals("choices")){
                    List<String> choices = Arrays.asList(questionToUpdate.getChoices().split(","));
                    if(choices.contains(answer)){
                        questionToUpdate.setAnswer(answer);
                    } else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }
                questionToUpdate.setAnswer(answer);
            }
            return new ResponseEntity<>(questionRepository.save(questionToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/newreports/questions/{id}")
    public ResponseEntity<HttpStatus> deleteQuestion(@PathVariable("id") long id) {
        try {
            questionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
