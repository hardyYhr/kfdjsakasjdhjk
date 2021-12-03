package com.orcus.hha_report_manager.model;

import javax.persistence.*;
import java.time.Instant;


@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "createdAt")
    private long createdAt;

    @Column(name = "editedAt")
    private long editedAt;

    @Column(name = "departmentId")
    private String departmentId;

    @Column(name = "questionGroup")
    private String group;

    @Column(name = "questionOrder")
    private int order;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "questionType")
    private String type;

    @Column(name = "choices")
    private String choices;

    public Question(){

    }

    public Question(String question){
        this.question = question;
    }

    public Question(String departmentId, String group, int order, String question, String answer, String type, String choices) {
        this.createdAt = Instant.now().toEpochMilli();
        this.editedAt = Instant.now().toEpochMilli();
        this.departmentId = departmentId;
        this.group = group;
        this.order = order;
        this.question = question;
        this.answer = answer;
        this.type = type;
        this.choices = choices;
    }

    public long getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(long editedAt) {
        this.editedAt = editedAt;
    }

    public String getQuestion(){
        return this.question;
    }

    public void setQuestion(String question){
        this.question = question;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }
}
