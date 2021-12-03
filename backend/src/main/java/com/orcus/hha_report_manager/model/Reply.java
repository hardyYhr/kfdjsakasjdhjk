package com.orcus.hha_report_manager.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "replies")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "department")
    private String department;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "content")
    private String content;


    public Reply() {

    }

    public Reply(String username, String firstname, String lastname, String department, LocalDateTime timestamp, String content) {
        this.username = username;
        this.firstName = firstname;
        this.lastName = lastname;
        this.department = department;
        this.timestamp = timestamp;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }

    public LocalDateTime getTimestamp() { return this.timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }


    @Override
    public String toString() {
        return "Message [id=" + id + ", username= " + username + ", first name=" + firstName + ", last name=" + lastName + ", department=" + department + ", timestamp=" + timestamp + ", content=" + content +"]";
    }
}
