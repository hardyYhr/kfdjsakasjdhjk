package com.orcus.hha_report_manager.model;

import javax.persistence.*;

@Entity
@Table(name = "WrittenQuestion")
public class WrittenQuestion  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "requiredByMSPP")
    private Boolean requiredByMSPP;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;

    public WrittenQuestion(){

    }

    public WrittenQuestion(String question){
        this.question = question;
    }

    public WrittenQuestion(Boolean requiredByMSPP, String question, String answer){
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
