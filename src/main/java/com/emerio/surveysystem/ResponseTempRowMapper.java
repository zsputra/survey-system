package com.emerio.surveysystem;


import com.emerio.surveysystem.model.Response;
import com.emerio.surveysystem.model.Values;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class ResponseTempRowMapper implements RowMapper<Response> {

    @Override
    public Response mapRow(ResultSet rs, int rowNum) throws SQLException {

        Response response = new Response();
        response.setResponseId(rs.getInt("response_temp_id"));
        response.setSurveyId(rs.getInt("response_temp_survey_id"));
        response.setPageId(rs.getInt("response_temp_page_id"));
        response.setQuestionId(rs.getInt("question_temp_id"));
        response.setQuestionType(rs.getString("questionType"));
        response.setAnswer(rs.getString("answer"));
        response.setDateAnswer(rs.getTimestamp("dateanswer"));

                String json = rs.getString("optionanswer");

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Values>>() {
                }.getType();
                List<Values> optionAnswerList = gson.fromJson(json, listType);
                response.setOptionAnswers(optionAnswerList);

                response.setUserId(rs.getString("response_temp_user_id"));
//            }

            return response;
//        }

    }

}
