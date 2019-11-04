package com.emerio.surveysystem;

import com.emerio.surveysystem.model.*;
import com.emerio.surveysystem.service.SurveyService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class ServiceTest {

    @MockBean
    SurveyDao surveyDao;

    @InjectMocks
    SurveyService surveyService;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void inserttoDb() throws SQLException {

        Question questionA1 = new Question();
        questionA1.setQuestion("apa jenis kelamin kamu?");
        questionA1.setQuestionType("multiple choice");

        Question questionA2 = new Question();
        questionA2.setQuestion("judul film horor kesukaan kamu?");
        questionA2.setQuestionType("multiple choice");

        Values value = new Values();
        Description description = new Description();
        value.setValues("A");
        description.setDescriptions("Perempuan");

        Values value1 = new Values();
        Description description1 = new Description();
        value1.setValues("B");
        description1.setDescriptions("Laki-laki");

        questionA1.setValues(Arrays.asList(value, value1));
        questionA1.setDescriptions(Arrays.asList(description, description1));

        Values value2 = new Values();
        Description description2 = new Description();
        value2.setValues("A");
        description2.setDescriptions("Anabelle");

        Values value3 = new Values();
        Description description3 = new Description();
        value3.setValues("B");
        description3.setDescriptions("the conjuring");

        Values value4 = new Values();
        Description description4 = new Description();
        value4.setValues("C");
        description4.setDescriptions("Danur");

        questionA2.setValues(Arrays.asList(value2, value3, value4));
        questionA2.setDescriptions(Arrays.asList(description2, description3, description4));

        Page pageA1 = new Page();
        pageA1.setId(1);
        pageA1.setTitle("survey untuk mengetahui tingkat kecerdasan anak");
        pageA1.setDesc("halaman1");
        // // pageA1.setTimer(0);
        pageA1.addQuestions(questionA1);
        pageA1.addQuestions(questionA2);

        Page pageA2 = new Page();
        pageA2.setId(2);
        pageA2.setTitle("title 2");
        pageA2.setDesc("halaman2");
        // // pageA2.setTimer(0);

        Page pageB1 = new Page();
        pageB1.setId(3);
        pageB1.setTitle("title 3");
        pageB1.setDesc("halaman3");

        Survey surveyA = new Survey();

        surveyA.addPages(pageA1);
        surveyA.addPages(pageA2);
        surveyA.addPages(pageB1);

        surveyService.insert(surveyA);
        verify(surveyDao, times(1)).insert(surveyA);

    }



}
