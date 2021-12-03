package com.orcus.hha_report_manager.controller;

import com.orcus.hha_report_manager.model.*;
import com.orcus.hha_report_manager.repository.*;
import com.orcus.hha_report_manager.security.beans.HTTPRequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private NumericalQuestionRepository numericalQuestionRepository;

    @Autowired
    private WrittenQuestionRepository writtenQuestionRepository;

    @Autowired
    private MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;

    @Autowired
    private PatientInfoRepository patientInfoRepository;

    @Autowired
    private HTTPRequestUser httpRequestUser;

    //Reports; collections of some Metadata, some Written Questions, some Multiple Choice Questions, and some PatientInfo.

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllReports(@RequestParam(required = false) String department, String username) {
        try {
            List<Report> reports = new ArrayList<Report>();

            if (department == null && username == null)
                reportRepository.findAll().forEach(reports::add);
            else if (department != null)
                reportRepository.findByDepartmentContains(department).forEach(reports::add);
            else if (username != null)
                reportRepository.findBySubmitterUsername(username).forEach(reports::add);

            if (reports.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable("id") long id) {
        Optional<Report> reportData = reportRepository.findById(id);

        if (reportData.isPresent()) {
            return new ResponseEntity<>(reportData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/reports")
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        checkReportSubmitterUsername(report);
        try {
            Report newReport;
            if (Objects.nonNull(report.getMonth())) {
                newReport = reportRepository
                        .save(new Report(report.getDepartment(), report.getMonth(), report.getSubmitterUsername(), report.getSubmitterFirstName(), report.getSubmitterLastName(), report.isComplete(), report.isSaved(), report.isSubmitted(), report.isTemplate(), report.getNumericalQuestions(), report.getWrittenQuestions(), report.getMultipleChoiceQuestions(), report.getPatientInfo()));
            } else {
                newReport = reportRepository
                        .save(new Report(report.getDepartment(), report.getSubmitterUsername(), report.getSubmitterFirstName(), report.getSubmitterLastName(), report.isComplete(), report.isSaved(), report.isSubmitted(), report.isTemplate(), report.getNumericalQuestions(), report.getWrittenQuestions(), report.getMultipleChoiceQuestions(), report.getPatientInfo()));
            }
            return new ResponseEntity<>(newReport, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void checkReportSubmitterUsername(Report report) {
        if(report.getSubmitterUsername() == null){
            var employee = httpRequestUser.getEmployee();
            report.setSubmitterUsername(employee.getUsername());
            report.setSubmitterFirstName(employee.getFirstName());
            report.setSubmitterLastName(employee.getLastName());
        }
    }

    @PutMapping("/reports/{id}")
    public ResponseEntity<Report> updateReportDetails(@PathVariable("id") long id, @RequestBody Report report) {
        Optional<Report> reportData = reportRepository.findById(id);

        if (reportData.isPresent()) {
            Report reportToChange = reportData.get();
            if (Objects.nonNull(report.getDepartment())) {
                reportToChange.setDepartment(report.getDepartment());
            }
            if (Objects.nonNull(report.getMonth())) {
                reportToChange.setMonth(report.getMonth());
            }
            if (Objects.nonNull(report.isSaved())) {
                reportToChange.setSaved(report.isSaved());
            }
            if (Objects.nonNull(report.isComplete())) {
                reportToChange.setComplete(report.isComplete());
            }
            if (Objects.nonNull(report.isSubmitted())) {
                reportToChange.setSubmitted(report.isSubmitted());
            }
            if (Objects.nonNull(report.isTemplate())) {
                reportToChange.setTemplate(report.isTemplate());
            }
            if (Objects.nonNull(report.getNumericalQuestions())) {
                reportToChange.setNumericalQuestions(report.getNumericalQuestions());
            }
            if (Objects.nonNull(report.getWrittenQuestions())) {
                reportToChange.setWrittenQuestions(report.getWrittenQuestions());
            }
            if (Objects.nonNull(report.getPatientInfo())) {
                reportToChange.setPatientInfo(report.getPatientInfo());
            }
            return new ResponseEntity<>(reportRepository.save(reportToChange), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<HttpStatus> deleteReport(@PathVariable("id") long id) {
        try {
            reportRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/reports")
    public ResponseEntity<HttpStatus> deleteAllReports() {
        try {
            reportRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //Numeric questions, with a String question and a Double answer

    @PostMapping("/reports/{id}/questions")
    public ResponseEntity<NumericalQuestion> addQuestionToReport(@PathVariable("id") long id, @RequestBody NumericalQuestion numericalQuestion) {
        Optional<Report> reportData = reportRepository.findById(id);
        if (reportData.isPresent()) {
            Report report = reportData.get();
            NumericalQuestion question = numericalQuestionRepository.save(numericalQuestion);
            report.addNumericalQuestion(question);
            reportRepository.save(report);
            return new ResponseEntity<>(question, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reports/questions/{id}")
    public ResponseEntity<NumericalQuestion> editQuestion(@PathVariable("id") long id, @RequestBody NumericalQuestion numericalQuestion) {
        Optional<NumericalQuestion> questionData = numericalQuestionRepository.findById(id);
        if (questionData.isPresent()) {
            NumericalQuestion questionToUpdate = questionData.get();
            if (Objects.nonNull(numericalQuestion.isRequiredByMSPP())) {
                questionToUpdate.setRequiredByMSPP(numericalQuestion.isRequiredByMSPP());
            }
            if (Objects.nonNull(numericalQuestion.getQuestion())) {
                questionToUpdate.setQuestion(numericalQuestion.getQuestion());
            }
            return new ResponseEntity<>(numericalQuestionRepository.save(questionToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reports/questions/{id}/answer")
    public ResponseEntity<NumericalQuestion> answerQuestion(@PathVariable("id") long id, @RequestBody Double answer) {
        Optional<NumericalQuestion> questionData = numericalQuestionRepository.findById(id);
        if (questionData.isPresent()) {
            NumericalQuestion questionToUpdate = questionData.get();
            if (Objects.nonNull(answer)) {
                questionToUpdate.setAnswer(answer);
            }
            return new ResponseEntity<>(numericalQuestionRepository.save(questionToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/reports/questions/{id}")
    public ResponseEntity<HttpStatus> deleteQuestion(@PathVariable("id") long id) {
        try {
            numericalQuestionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Written Questions, with a String Question and a String Answer.

    @PostMapping("/reports/{id}/questions/written")
    public ResponseEntity<WrittenQuestion> addWrittenQuestionToReport(@PathVariable("id") long id, @RequestBody WrittenQuestion writtenQuestion) {
        Optional<Report> reportData = reportRepository.findById(id);
        if (reportData.isPresent()) {
            Report report = reportData.get();
            WrittenQuestion question = writtenQuestionRepository.save(writtenQuestion);
            report.addWrittenQuestion(question);
            reportRepository.save(report);
            return new ResponseEntity<>(question, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reports/questions/written/{id}")
    public ResponseEntity<WrittenQuestion> editWrittenQuestion(@PathVariable("id") long id, @RequestBody WrittenQuestion writtenQuestion) {
        Optional<WrittenQuestion> questionData = writtenQuestionRepository.findById(id);
        if (questionData.isPresent()) {
            WrittenQuestion questionToUpdate = questionData.get();
            if (Objects.nonNull(writtenQuestion.isRequiredByMSPP())) {
                questionToUpdate.setRequiredByMSPP(writtenQuestion.isRequiredByMSPP());
            }
            if (Objects.nonNull(writtenQuestion.getQuestion())) {
                questionToUpdate.setQuestion(writtenQuestion.getQuestion());
            }
            return new ResponseEntity<>(writtenQuestionRepository.save(questionToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reports/questions/written/{id}/answer")
    public ResponseEntity<WrittenQuestion> answerWrittenQuestion(@PathVariable("id") long id, @RequestBody String answer) {
        Optional<WrittenQuestion> questionData = writtenQuestionRepository.findById(id);
        if (questionData.isPresent()) {
            WrittenQuestion questionToUpdate = questionData.get();
            if (Objects.nonNull(answer)) {
                questionToUpdate.setAnswer(answer);
            }
            return new ResponseEntity<>(writtenQuestionRepository.save(questionToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/reports/questions/written/{id}")
    public ResponseEntity<HttpStatus> deleteWrittenQuestion(@PathVariable("id") long id) {
        try {
            writtenQuestionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Multiple choice questions, with a String Question, a Map<String, Character> set of Choices, and a Character Choice.

    @PostMapping("/reports/{id}/questions/mcq")
    public ResponseEntity<MultipleChoiceQuestion> addQuestionToReport(@PathVariable("id") long id, @RequestBody MultipleChoiceQuestion multipleChoiceQuestion) {
        Optional<Report> reportData = reportRepository.findById(id);
        if (reportData.isPresent()) {
            Report report = reportData.get();
            report.addMultipleChoiceQuestion(multipleChoiceQuestion);
            MultipleChoiceQuestion question = multipleChoiceQuestionRepository.save(multipleChoiceQuestion);
            reportRepository.save(report);
            return new ResponseEntity<>(question, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reports/questions/mcq/{id}")
    public ResponseEntity<MultipleChoiceQuestion> editMCQuestion(@PathVariable("id") long id, @RequestBody MultipleChoiceQuestion multipleChoiceQuestion) {
        Optional<MultipleChoiceQuestion> questionData = multipleChoiceQuestionRepository.findById(id);
        if (questionData.isPresent()) {
            MultipleChoiceQuestion questionToUpdate = questionData.get();
            if (Objects.nonNull(multipleChoiceQuestion.getRequiredByMSPP())) {
                questionToUpdate.setRequiredByMSPP(multipleChoiceQuestion.getRequiredByMSPP());
            }
            if (Objects.nonNull(multipleChoiceQuestion.getQuestion())) {
                questionToUpdate.setQuestion(multipleChoiceQuestion.getQuestion());
            }
            if (Objects.nonNull(multipleChoiceQuestion.getChoices())) {
                questionToUpdate.setChoices(multipleChoiceQuestion.getChoices());
            }
            return new ResponseEntity<>(multipleChoiceQuestionRepository.save(questionToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reports/questions/mcq/{id}/answer")
    public ResponseEntity<MultipleChoiceQuestion> answerMCQuestion(@PathVariable("id") long id, @RequestBody Character choice) {
        Optional<MultipleChoiceQuestion> questionData = multipleChoiceQuestionRepository.findById(id);
        if (questionData.isPresent()) {
            MultipleChoiceQuestion questionToUpdate = questionData.get();
            if (Objects.nonNull(choice)) {
                if (questionToUpdate.getChoices().containsKey(choice)) {
                    questionToUpdate.setChoice(choice);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(multipleChoiceQuestionRepository.save(questionToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/reports/questions/mcq/{id}")
    public ResponseEntity<HttpStatus> deleteMultipleChoiceQuestion(@PathVariable("id") long id) {
        try {
            multipleChoiceQuestionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //PatientInfo; Individual patient incidents that are included anonymously on Reports.

    @PostMapping("/reports/{id}/patients")
    public ResponseEntity<PatientInfo> addPatientInfoToReport(@PathVariable("id") long id, @RequestBody PatientInfo patientInfo) {
        Optional<Report> reportData = reportRepository.findById(id);
        if (reportData.isPresent()) {
            Report report = reportData.get();
            report.addPatient(patientInfo);
            PatientInfo info = patientInfoRepository.save(patientInfo);
            reportRepository.save(report);
            return new ResponseEntity<>(info, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reports/patients/{id}")
    public ResponseEntity<PatientInfo> updatePatientInfo(@PathVariable("id") long id, @RequestBody PatientInfo patientInfo) {
        Optional<PatientInfo> patientData = patientInfoRepository.findById(id);
        if (patientData.isPresent()) {
            PatientInfo patientInfoToUpdate = patientData.get();
            if (Objects.nonNull(patientInfo.getInformation())) {
                patientInfoToUpdate.setInformation(patientInfo.getInformation());
            }
            return new ResponseEntity<>(patientInfoRepository.save(patientInfoToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/reports/patients/{id}")
    public ResponseEntity<HttpStatus> deletePatientInfo(@PathVariable("id") long id) {
        try {
            patientInfoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}