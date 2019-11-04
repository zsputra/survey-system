package com.emerio.surveysystem.model;

import org.springframework.stereotype.Component;

@Component
public class Values {

    private int id;
    private String values;
    private boolean checked;
    private String imgValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getImgValue() {
        return this.imgValue;
    }

    public void setImgValue(String imgValue) {
        this.imgValue = imgValue;
    }

}