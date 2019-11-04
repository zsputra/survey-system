package com.emerio.surveysystem.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.emerio.surveysystem.model.SurveyView;

import org.springframework.jdbc.core.RowMapper;

public class SurveyViewRowMapper implements RowMapper<SurveyView> {

    @Override
    public SurveyView mapRow(ResultSet rs, int rowNum) throws SQLException {
        SurveyView surveyviews = new SurveyView();
        surveyviews.setIdSurvey(rs.getInt("survey_id"));
        return surveyviews;
    }

}