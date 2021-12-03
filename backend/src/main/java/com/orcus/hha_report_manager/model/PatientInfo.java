package com.orcus.hha_report_manager.model;

import javax.persistence.*;

@Entity
@Table(name = "patientInfo")
public class PatientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "information")
    String information;

    public long getId() {
        return id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
