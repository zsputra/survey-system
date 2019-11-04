package com.emerio.surveysystem.model;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class SurveyView {
    private int idSurvey;
    private String lastModified;
    private String colorHeader;
    private String imgHeader;
    private String icon;
    private String imgIcon;
    private Boolean privateStatus;
    private Boolean activeStatus;
    private Boolean publishStatus;
    private String title;

    public SurveyView() {
    }

    public SurveyView(int idSurvey, String lastModified, String colorHeader, String imgHeader, String icon,
            String imgIcon, Boolean privateStatus, Boolean activeStatus, Boolean publishStatus, String title) {
        this.idSurvey = idSurvey;
        this.lastModified = lastModified;
        this.colorHeader = colorHeader;
        this.imgHeader = imgHeader;
        this.icon = icon;
        this.imgIcon = imgIcon;
        this.privateStatus = privateStatus;
        this.activeStatus = activeStatus;
        this.publishStatus = publishStatus;
        this.title = title;
    }

    public int getIdSurvey() {
        return this.idSurvey;
    }

    public void setIdSurvey(int idSurvey) {
        this.idSurvey = idSurvey;
    }

//    public LocalDateTime getLastModified() {
//        return this.lastModified;
//    }
//
//    public void setLastModified(LocalDateTime lastModified) {
//        this.lastModified = lastModified;
//    }


    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getColorHeader() {
        return this.colorHeader;
    }

    public void setColorHeader(String colorHeader) {
        this.colorHeader = colorHeader;
    }

    public String getImgHeader() {
        return this.imgHeader;
    }

    public void setImgHeader(String imgHeader) {
        this.imgHeader = imgHeader;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImgIcon() {
        return this.imgIcon;
    }

    public void setImgIcon(String imgIcon) {
        this.imgIcon = imgIcon;
    }

    public Boolean isPrivateStatus() {
        return this.privateStatus;
    }

    public Boolean getPrivateStatus() {
        return this.privateStatus;
    }

    public void setPrivateStatus(Boolean privateStatus) {
        this.privateStatus = privateStatus;
    }

    public Boolean isActiveStatus() {
        return this.activeStatus;
    }

    public Boolean getActiveStatus() {
        return this.activeStatus;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Boolean isPublishStatus() {
        return this.publishStatus;
    }

    public Boolean getPublishStatus() {
        return this.publishStatus;
    }

    public void setPublishStatus(Boolean publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
