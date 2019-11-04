package com.emerio.surveysystem;

import com.emerio.surveysystem.model.Page;
import com.emerio.surveysystem.model.Survey;
import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


public class SurveyPageRowMapper implements RowMapper<Survey> {

    @Override
    public Survey mapRow(ResultSet rs, int rowNum) throws SQLException {

        String pages = "pages";

        Survey survey = new Survey();
            survey.setIdSurvey(rs.getInt("survey_id"));
            survey.setColorHeader(rs.getString("colorheader"));
            survey.setColorBg(rs.getString("colorbg"));
            survey.setIcon(rs.getString("icon"));
            survey.setImgHeader(rs.getString("imgHeader"));
            survey.setImgIcon(rs.getString("imgLogo"));

                String json = rs.getString(pages);
                Gson gson = new Gson();
                Page pageResult = gson.fromJson(json, Page.class);

                survey.addPages(pageResult);

            return survey;

    }

}
