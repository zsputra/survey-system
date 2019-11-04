package com.emerio.surveysystem;

import com.emerio.surveysystem.model.Description;
import com.emerio.surveysystem.model.Page;
import com.emerio.surveysystem.model.Question;
import com.emerio.surveysystem.model.Survey;
import com.emerio.surveysystem.model.Values;
import com.emerio.surveysystem.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DemoApplication{


	@Autowired
	SurveyService surveyService;

	public static void main(String[] args) throws  SQLException {
		SpringApplication.run(DemoApplication.class, args);
	}


}