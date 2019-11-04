package com.emerio.surveysystem.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Page {

    private int id;
    private String title;
    private String desc;
    private String titlepic;
    private  String pagesPicture;
    private List<Pictures> pictures;

    private List<Question> questions = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int page_id) {
        this.id = page_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String page_desc) {
        this.desc = page_desc;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestions(Question question) {
        this.questions.add(question);

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String page_title) {
        this.title = page_title;
    }

    public List<Pictures> getPictures() {
        return pictures;
    }

    public String getTitlepic() {
        return titlepic;
    }

    public void setTitlepic(String titlepic) {
        this.titlepic = titlepic;
    }

    public String getPagesPicture() {
        return pagesPicture;
    }

    public void setPagesPicture(String pagesPicture) {
        this.pagesPicture = pagesPicture;
    }
}