package com.orcus.hha_report_manager.model;

import javax.persistence.*;
import java.time.Instant;
import java.time.Month;
import java.util.List;

@Entity
@Table(name = "newReports")
public class NewReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "reportName")
    private String name;

    @Column(name = "departmentName")
    private String departmentName;

    @Column(name = "departmentId")
    private Integer departmentId;

    @Column(name = "month")
    private Month month;

    @Column(name = "createdAt")
    private long createdAt;

    @Column(name = "editedAt")
    private long editedAt;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "editedBy")
    private String editedBy;

    @Column(name = "questions")
    @OneToMany(targetEntity = Question.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "newReport_id", referencedColumnName = "id")
    private List<Question> questions;

    @Column(name = "groupings")
    private String groupings;

    public NewReport(){

    }

    public NewReport(String name, String departmentName, Integer departmentId, Month month, String createdBy, String editedBy, List<Question> questions, String groupings) {
        this.createdAt = Instant.now().toEpochMilli();
        this.editedAt = Instant.now().toEpochMilli();
        this.name = name;
        this.departmentName = departmentName;
        this.departmentId = departmentId;
        this.month = month;
        this.createdBy = createdBy;
        this.editedBy = editedBy;
        this.questions = questions;
        this.groupings = groupings;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getGroupings() {
        return groupings;
    }

    public void setGroupings(String groupings) {
        this.groupings = groupings;
    }
}
