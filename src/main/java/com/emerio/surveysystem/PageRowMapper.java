package com.emerio.surveysystem;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.emerio.surveysystem.model.Page;
import com.google.gson.Gson;

import org.springframework.jdbc.core.RowMapper;

public class PageRowMapper implements RowMapper<Page> {

    @Override
    public Page mapRow(ResultSet rs, int rowNum) throws SQLException {

        String json = rs.getString("pages");

        Gson gson = new Gson();

        return gson.fromJson(json, Page.class);



    }

    
}