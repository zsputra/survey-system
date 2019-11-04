package com.emerio.surveysystem.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Response {
    private int responseId;
    private int surveyId;
    private String userId;
    private int pageId;
    private int questionId;
    private String questionType;
    private String answer;
    //private LocalDateTime dateAnswer;
    private Timestamp dateAnswer;
    private List<Values> optionAnswers = new ArrayList<>();

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

//    public List<OptionAnswer> getOptionAnswers() {
//        return optionAnswers;
//    }
//
//    public void setOptionAnswers(List<OptionAnswer> answer) {
//        this.optionAnswers = answer;
//    }
//
//    public void addOptionAnswers(OptionAnswer answer) {
//        this.optionAnswers.add(answer);
//    }


    public List<Values> getOptionAnswers() {
        return optionAnswers;
    }

    public void setOptionAnswers(List<Values> optionAnswers) {
        this.optionAnswers = optionAnswers;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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
}