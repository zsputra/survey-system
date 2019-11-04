package com.emerio.surveysystem;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.emerio.surveysystem.model.Values;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.jdbc.core.RowMapper;

public class ValueRowMapper implements RowMapper <List<Values>> {

    @Override
    public List<Values> mapRow(ResultSet rs, int rowNum) throws SQLException {

        String json = rs.getString("optionanswer");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Values>>() {
        }.getType();
        List<Values> valueResult = gson.fromJson(json, listType);

        return valueResult;


    }

}