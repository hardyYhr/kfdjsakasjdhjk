package com.orcus.hha_report_manager.model;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "MultipleChoiceQuestions")
public class MultipleChoiceQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "requiredByMSPP")
    private Boolean requiredByMSPP;

    @Column(name = "question")
    private String question;

    @ElementCollection (fetch = FetchType.EAGER)
    @CollectionTable(name = "choices", joinColumns = @JoinColumn(name = "question_id", referencedColumnName = "id"))
    //@OneToMany(targetEntity = Map.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(name = "character")
    @Column(name = "answer")
    private Map<Character, String> choices;

    @Column(name = "choice")
    private Character choice;

    public MultipleChoiceQuestion(){

    }

    public MultipleChoiceQuestion(String question, Map<Character, String> choices){
        this.question = question;
        this.choices = choices;
    }

    public MultipleChoiceQuestion(Boolean requiredByMSPP, String question, Map<Character, String> choices, Character choice){
        this.requiredByMSPP = requiredByMSPP;
        this.question = question;
        this.choices = choices;
        this.choice = choice;
    }

    public long getId() {
        return id;
    }

    public Boolean getRequiredByMSPP() {
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

    public Map<Character, String> getChoices(){
        return this.choices;
    }

    public void setChoices(Map<Character, String> choices){
        this.choices = choices;
    }

    public Character getChoice() {
        return choice;
    }

    public void setChoice(Character choice) {
        this.choice = choice;
    }
}
