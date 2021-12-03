package com.orcus.hha_report_manager.model;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "messages")
public class Message {

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

    @Column(name = "replies")
    @OneToMany(targetEntity = Reply.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private List<Reply> replies;

    public Message() {

    }

    public Message(String username, String firstname, String lastname, String department, LocalDateTime timestamp, String content) {
        this.username = username;
        this.firstName = firstname;
        this.lastName = lastname;
        this.department = department;
        this.timestamp = timestamp;
        this.content = content;
        this.replies = new ArrayList<Reply>();
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

    //public void addReply(Reply reply) { replies.add(reply); }

    public List<Reply> getReplies() { return this.replies; }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", username= " + username + ", first name=" + firstName + ", last name=" + lastName + ", department=" + department + ", timestamp=" + timestamp + ", content=" + content +"]";
    }
}