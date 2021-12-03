package com.orcus.hha_report_manager.model;

import javax.persistence.*;

@Entity
@Table(name = "NumericalQuestion")
public class NumericalQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "requiredByMSPP")
    private Boolean requiredByMSPP;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private Double answer;

    public NumericalQuestion(){

    }

    public NumericalQuestion(String question){
        this.question = question;
    }

    public NumericalQuestion(Boolean requiredByMSPP, String question, Double answer){
        this.requiredByMSPP = requiredByMSPP;
        this.question = question;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public Boolean isRequiredByMSPP() {
        return requiredByMSPP;
    }

    public void setRequiredByMSPP(Boolean requiredByMSPP) {
        this.requiredByMSPP = requiredByMSPP;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Double getAnswer() {
        return answer;
    }

    public void setAnswer(Double answer) {
        this.answer = answer;
    }

}
