package com.orcus.hha_report_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id = -1;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name="password")
    private String password;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    //Todo: should be foreign key.
    @Column(name = "department")
    private String department;

    @Column(name = "isDepartmentHead")
    private boolean isDepartmentHead;

    @Column(name = "isAdmin")
    private boolean isAdmin;

    @Column(name = "score")
    private Integer score;

    public Employee() {

    }

    public Employee(String username,String password ,String firstname, String lastname, String department, boolean isDepartmentHead) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.username = username;
        this.password = password;
        this.department = department;
        this.isDepartmentHead = isDepartmentHead;
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

    public boolean isDepartmentHead() {
        return isDepartmentHead;
    }

    public void setDepartmentHead(boolean isDepartmentHead) {
        this.isDepartmentHead = isDepartmentHead;
    }

    /**
     * This method replaces the getter to "trick" Jackson from writing the password field into Json
     * */
    public String accessPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(Integer score) { this.score = score; }

    public Integer getScore() { return this.score; }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", username=" + username + " , first name=" + firstName + ", last name=" + lastName + ", department=" + department + ", department head=" + isDepartmentHead+ "]";
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}