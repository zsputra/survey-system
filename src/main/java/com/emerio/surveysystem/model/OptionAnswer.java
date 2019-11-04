package com.emerio.surveysystem.model;

public class OptionAnswer {
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

    public boolean isChecked() {
        return this.checked;
    }

    public boolean getChecked() {
        return this.checked;
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
