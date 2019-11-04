package com.emerio.surveysystem.model;

import org.bson.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class Survey {

    private int idSurvey;

    private String idUser;

    private LocalDateTime lastModified;

    private String colorHeader;

    private String colorBg;

    private String icon;

    private String imgHeader;

    private String imgIcon;

    private Boolean privateStatus;

    private Boolean activeStatus;

    private Boolean publishStatus;

    private List<Page> pages = new ArrayList<>();

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

    public String getColorBg() {
        return this.colorBg;
    }

    public void setColorBg(String colorBg) {
        this.colorBg = colorBg;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImgHeader() {
        return imgHeader;
    }

    public void setImgHeader(String imgHeader) {
        this.imgHeader = imgHeader;
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

    public List<Page> getPages() {
        return pages;
    }

    public void addPages(Page page) {
        this.pages.add(page);
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;

    }

    public String getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(String imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified() {
        this.lastModified = LocalDateTime.now();
    }

}