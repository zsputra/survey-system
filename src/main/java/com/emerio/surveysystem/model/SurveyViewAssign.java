package com.emerio.surveysystem.model;

import java.time.LocalDateTime;

public class SurveyViewAssign {
    private int idSurvey;
    private String colorHeader;
    private String imgHeader;
    private String icon;
    private String imgIcon;
    private Boolean privateStatus;
    private Boolean activeStatus;
    private Boolean publishStatus;
    private String title;
    private Boolean submitStatus;
    private Boolean dismissStatus;
    private String lastModifiedAssign;

    public SurveyViewAssign() {
    }

    public SurveyViewAssign(int idSurvey, String colorHeader, String imgHeader, String icon, String imgIcon,
            Boolean privateStatus, Boolean activeStatus, Boolean publishStatus, String title, Boolean submitStatus,
            Boolean dismissStatus, String lastModifiedAssign) {
        this.idSurvey = idSurvey;
        this.colorHeader = colorHeader;
        this.imgHeader = imgHeader;
        this.icon = icon;
        this.imgIcon = imgIcon;
        this.privateStatus = privateStatus;
        this.activeStatus = activeStatus;
        this.publishStatus = publishStatus;
        this.title = title;
        this.submitStatus = submitStatus;
        this.dismissStatus = dismissStatus;
        this.lastModifiedAssign = lastModifiedAssign;
    }

    public int getIdSurvey() {
        return this.idSurvey;
    }

    public void setIdSurvey(int idSurvey) {
        this.idSurvey = idSurvey;
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

    public Boolean isSubmitStatus() {
        return this.submitStatus;
    }

    public Boolean getSubmitStatus() {
        return this.submitStatus;
    }

    public void setSubmitStatus(Boolean submitStatus) {
        this.submitStatus = submitStatus;
    }

    public Boolean isDismissStatus() {
        return this.dismissStatus;
    }

    public Boolean getDismissStatus() {
        return this.dismissStatus;
    }

    public void setDismissStatus(Boolean dismissStatus) {
        this.dismissStatus = dismissStatus;
    }

//    public LocalDateTime getLastModifiedAssign() {
//        return this.lastModifiedAssign;
//    }
//
//    public void setLastModifiedAssign(LocalDateTime lastModifiedAssign) {
//        this.lastModifiedAssign = lastModifiedAssign;
//    }


    public String getLastModifiedAssign() {
        return lastModifiedAssign;
    }

    public void setLastModifiedAssign(String lastModifiedAssign) {
        this.lastModifiedAssign = lastModifiedAssign;
    }
}