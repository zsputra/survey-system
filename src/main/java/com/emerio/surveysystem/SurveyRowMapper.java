package com.emerio.surveysystem;

import com.emerio.surveysystem.model.Page;
import com.emerio.surveysystem.model.Survey;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;



public class SurveyRowMapper implements RowMapper<Survey> {

    @Override
    public Survey mapRow(ResultSet rs, int rowNum) throws SQLException {



        String pages = "pages";


        Survey survey = new Survey();

            survey.setIdSurvey(rs.getInt("survey_id"));
            survey.setIdUser(rs.getString("createdBy"));
            survey.setColorHeader(rs.getString("colorheader"));
            survey.setColorBg(rs.getString("colorbg"));
            survey.setIcon(rs.getString("icon"));
            survey.setImgIcon(rs.getString("imglogo"));
            survey.setPrivateStatus(rs.getBoolean("privatestatus"));
            survey.setActiveStatus(rs.getBoolean("activestatus"));
            survey.setPublishStatus(rs.getBoolean("publishstatus"));
            survey.setImgHeader(rs.getString("imgheader"));
//            if (rs.getString(pages) == null || rs.getString(pages).isEmpty()) {
//                survey.setPages(null);
//            } else {

                String json = rs.getString(pages);

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Page>>() {
                }.getType();
                List<Page> pagesResult = gson.fromJson(json, listType);
                survey.setPages(pagesResult);

//            }

            return survey;
//        }

    }

}
