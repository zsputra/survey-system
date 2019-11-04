package com.emerio.surveysystem.model;

import org.springframework.stereotype.Component;

@Component
public class Validate {

    private String validateType;
    private String validateSetting;
    private String validateValue;

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

    public String getValidateValue() {
        return validateValue;
    }

    public void setValidateValue(String validateValue) {
        this.validateValue = validateValue;
    }
}
