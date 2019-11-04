package com.emerio.surveysystem.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.stereotype.Component;


@Component
public class Question {

    private int id;
    private String question;
    private String questionType;
    private boolean requiredValue;

    private List<Values> values = new ArrayList<>();

    private List<Description> descriptions = new ArrayList<>();

    private String description;
    private String titlepic;
    private String imgPage;
    private String imgQuestion;
    private String answer;
    //private LocalDateTime dateAnswer;
    private Timestamp dateAnswer;
    private boolean validateStatus;
    private String validateType;
    private String validateSetting;
    private int validateValue;
    private Validate validate;

    public String getValidateType() {
        return validateType;
    }

    public void setValidateType(String validateType) {
        this.validateType = validateType;
    }

    public String getValidateSetting() {
        return validateSetting;
    }

    public void setValidateSetting(String validateSetting) {
        this.validateSetting = validateSetting;
    }

    public int getValidateValue() {
        return validateValue;
    }

    public void setValidateValue(int validateValue) {
        this.validateValue = validateValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int question_id) {
        this.id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public boolean isRequiredValue() {
        return this.requiredValue;
    }

    public boolean getRequiredValue() {
        return this.requiredValue;
    }

    public void setRequiredValue(boolean requiredValue) {
        this.requiredValue = requiredValue;
    }


    public List<Values> getValues() {
        return values;
    }

    public List<Description> getDescriptions() {
        return descriptions;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }

    public void addValues(Values values) {
        this.values.add(values);
    }

    public void setDescriptions(List<Description> descriptions) {
        this.descriptions = descriptions;
    }

    public void addDescriptions(Description descriptions) {
        this.descriptions.add(descriptions);
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitlepic() {
        return this.titlepic;
    }

    public void setTitlepic(String titlepic) {
        this.titlepic = titlepic;
    }

    public String getImgPage() {
        return this.imgPage;
    }

    public void setImgPage(String imgPage) {
        this.imgPage = imgPage;
    }

    public String getImgQuestion() {
        return this.imgQuestion;
    }

    public void setImgQuestion(String imgQuestion) {
        this.imgQuestion = imgQuestion;
    }

    public boolean isValidateStatus() {
        return validateStatus;
    }

    public void setValidateStatus(boolean validateStatus) {
        this.validateStatus = validateStatus;
    }


//    public LocalDateTime getDateAnswer() {
//        return dateAnswer;
//    }
//
//    public void setDateAnswer(LocalDateTime dateAnswer) {
//        this.dateAnswer = dateAnswer;
//    }


    public Timestamp getDateAnswer() {
        return dateAnswer;
    }

    public void setDateAnswer(Timestamp dateAnswer) {
        this.dateAnswer = dateAnswer;
    }

    public Validate getValidate() {
        return validate;
    }

    public void setValidate(Validate validate) {
        this.validate = validate;
    }

}