package com.emerio.surveysystem;

import com.emerio.surveysystem.model.*;
import com.emerio.surveysystem.service.SurveyService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class DemoApplicationTests {

   @MockBean
    SurveyDao surveyDao;

    @InjectMocks
    SurveyService surveyService;

    private int id_survey;

    private Survey survey;



    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        this.id_survey = surveyDao.generatedId();

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
        pageA1.setId(0);
        pageA1.setTitle("survey untuk mengetahui tingkat kecerdasan anak");
        pageA1.setDesc("halaman1");
        // // pageA1.setTimer(0);
        pageA1.addQuestions(questionA1);
        pageA1.addQuestions(questionA2);

        Page pageA2 = new Page();
        pageA2.setId(1);
        pageA2.setTitle("title 2");
        pageA2.setDesc("halaman2");
        // // pageA2.setTimer(0);

        Page pageB1 = new Page();
        pageB1.setId(2);
        pageB1.setTitle("title 3");
        pageB1.setDesc("halaman3");

        Survey surveyA = new Survey();

        surveyA.addPages(pageA1);
        surveyA.addPages(pageA2);
        surveyA.addPages(pageB1);

        surveyA.setIdUser("abcd");

        surveyA.setIdSurvey(id_survey);

        surveyDao.updateSurvey(surveyA);


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

        surveyDao.insert(surveyA);

        verify(surveyDao, times(1)).insert(surveyA);

    }

    @Test
    public void insertBatchtodb() {
        Survey surveyB = new Survey();

        Survey surveyC = new Survey();

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

        Survey surveyA = new Survey();

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
        // pageB1.setTimer(10);

        surveyA.addPages(pageA1);
        surveyA.addPages(pageA2);
        surveyA.addPages(pageB1);

        surveyB.addPages(pageA1);
        surveyB.addPages(pageA2);
        surveyB.addPages(pageB1);

        surveyC.addPages(pageA1);
        surveyC.addPages(pageA2);
        surveyC.addPages(pageB1);

        List<Survey> surveys = new ArrayList<>();
        surveys.add(surveyB);
        surveys.add(surveyC);

        surveyDao.insertBatch(surveys);
        verify(surveyDao, times(1)).insertBatch(surveys);

    }

    @Test
    public void getAllSurveysFromDb() throws Exception {

        Survey surveyB = new Survey();

        Survey surveyC = new Survey();

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

        Survey surveyA = new Survey();

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
        // pageB1.setTimer(10);

        surveyA.addPages(pageA1);
        surveyA.addPages(pageA2);
        surveyA.addPages(pageB1);

        surveyB.addPages(pageA1);
        surveyB.addPages(pageA2);
        surveyB.addPages(pageB1);

        surveyC.addPages(pageA1);
        surveyC.addPages(pageA2);
        surveyC.addPages(pageB1);

        List<Survey> surveys = new ArrayList<>();
        surveys.add(surveyB);
        surveys.add(surveyC);
        surveys.add(surveyA);
        when(surveyDao.loadAllSurvey()).thenReturn(surveys);

        List<Survey> list = surveyDao.loadAllSurvey();
        assertEquals(3, list.size());
        verify(surveyDao, times(1)).loadAllSurvey();
    }

    @Test
    public void getSurveyByIdFromDb() throws SQLException {

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

        Survey surveyA = new Survey();
        surveyA.setIdSurvey(1);

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
        // pageB1.setTimer(10);

        int id = surveyDao.generatedId();
        surveyA.setIdSurvey(id);
        surveyA.addPages(pageA1);
        surveyA.addPages(pageA2);
        surveyA.addPages(pageB1);

        surveyDao.updateSurvey(surveyA);
//        when(surveyDao.loadSurveyById(1)).thenReturn(surveyA);
        Survey survey = surveyDao.loadSurveyById(id);
        assertEquals(1, survey.getPages().get(0).getId());
        assertEquals("survey untuk mengetahui tingkat kecerdasan anak", survey.getPages().get(0).getTitle());
        assertEquals("halaman1", survey.getPages().get(0).getDesc());
        assertEquals("apa jenis kelamin kamu?", survey.getPages().get(0).getQuestions().get(0).getQuestion());

    }

    @Test
    public void updateSurvey() {

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
        pageA1.setTitle("Survey 1 updated");
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
        surveyA.setIdSurvey(1);

        surveyA.addPages(pageA1);
        surveyA.addPages(pageA2);
        surveyA.addPages(pageB1);

        surveyDao.updateSurvey(surveyA);
        verify(surveyDao, times(1)).updateSurvey(surveyA);

    }

    @Test
    public void generateId() {

        int id = 4;
        try {
            when(surveyDao.generatedId()).thenReturn(id);
            int idSurvey = surveyDao.generatedId();
            verify(surveyDao, times(1)).generatedId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void loadSurveyPageOne() throws SQLException {
//        int id = surveyDao.generatedId();
//        Question questionA1 = new Question();
//        questionA1.setQuestion("apa jenis kelamin kamu?");
//        questionA1.setQuestionType("multiple choice");
//
//        Question questionA2 = new Question();
//        questionA2.setQuestion("judul film horor kesukaan kamu?");
//        questionA2.setQuestionType("multiple choice");
//
//        Values value = new Values();
//        Description description = new Description();
//        value.setValues("A");
//        value.setId(1);
//        description.setDescriptions("Perempuan");
//        description.setId(1);
//
//        Values value1 = new Values();
//        Description description1 = new Description();
//        value1.setValues("B");
//        description1.setDescriptions("Laki-laki");
//
//        questionA1.setId(0);
//        // questionA1.setValues(Arrays.asList(value, value1));
//        questionA1.addValues(value);
//        questionA1.addValues(value1);
//        questionA1.addDescriptions(description);
//        questionA1.addDescriptions(description1);
//        // questionA1.setDescriptions(Arrays.asList(description, description1));
//
//        Values value2 = new Values();
//        value2.setId(1);
//        Description description2 = new Description();
//        value2.setValues("A");
//        description2.setDescriptions("Anabelle");
//
//        Values value3 = new Values();
//        Description description3 = new Description();
//        value3.setValues("B");
//        description3.setDescriptions("the conjuring");
//
//        Values value4 = new Values();
//        Description description4 = new Description();
//        value4.setValues("C");
//        description4.setDescriptions("Danur");
//
//        questionA2.setValues(Arrays.asList(value2, value3, value4));
//        questionA2.setDescriptions(Arrays.asList(description2, description3, description4));
//
//        Page pageA1 = new Page();
//        pageA1.setId(1);
//        pageA1.setTitle("Survey 1 updated");
//        pageA1.setDesc("halaman1");
//        // // pageA1.setTimer(0);
//        pageA1.addQuestions(questionA1);
//        pageA1.addQuestions(questionA2);
//
//        Survey surveyA = new Survey();
//        surveyA.setIdSurvey(id);
//
//        surveyA.addPages(pageA1);
//
//        surveyDao.updateSurvey(surveyA);
//
//        Survey survey = surveyDao.loadSurveyByIdPageOne(id);
//        verify(surveyDao, times(1)).loadSurveyByIdPageOne(id);
//        assertEquals(id, survey.getIdSurvey());
////        assertEquals("multiple choice", survey.getPages().get(0).getQuestions().get(0).getQuestionType());
////        assertEquals(0, survey.getPages().get(0).getQuestions().get(0).getId());
////        assertEquals("judul film horor kesukaan kamu?", survey.getPages().get(0).getQuestions().get(1).getQuestion());
////        assertEquals(1, survey.getPages().get(0).getQuestions().get(0).getValues().get(0).getId());
////        assertEquals("A", survey.getPages().get(0).getQuestions().get(0).getValues().get(0).getValues());
////        assertEquals(1, survey.getPages().get(0).getQuestions().get(0).getDescriptions().get(0).getId());
////        assertEquals("Perempuan",
////                survey.getPages().get(0).getQuestions().get(0).getDescriptions().get(0).getDescriptions());
//
//    }

     @Test
     public void loadPageBySurveyIdandPageId() throws SQLException {

        int id = surveyDao.generatedId();
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
         pageA1.setId(0);
         pageA1.setTitle("survey untuk mengetahui tingkat kecerdasan anak");
         pageA1.setDesc("halaman1");
         // // pageA1.setTimer(0);
         pageA1.addQuestions(questionA1);
         pageA1.addQuestions(questionA2);

         Page pageA2 = new Page();
         pageA2.setId(1);
         pageA2.setTitle("title 2");
         pageA2.setDesc("halaman2");
         // // pageA2.setTimer(0);

         Page pageB1 = new Page();
         pageB1.setId(2);
         pageB1.setTitle("title 3");
         pageB1.setDesc("halaman3");

         Survey surveyA = new Survey();

         surveyA.addPages(pageA1);
         surveyA.addPages(pageA2);
         surveyA.addPages(pageB1);

         surveyA.setIdUser("abcd");

         surveyA.setIdSurvey(id);

         surveyDao.updateSurvey(surveyA);

     Page page1 = surveyDao.loadPageBySurveyIdandPageId(id_survey,"abcd", 0);
     verify(surveyDao, times(1)).loadPageBySurveyIdandPageId(this.id_survey, "abcd",0);
     Survey survey = surveyDao.loadSurveyById(id);
     assertEquals(id,survey.getIdSurvey());

//     assertEquals("survey untuk mengetahui tingkat kecerdasan anak", page1.getTitle());
//     assertEquals("halaman1", page1.getDesc());
//     assertEquals(new ArrayList<>(), page1.getQuestions());
     }

     @Test
     public void loadSurveyBySurveyIdandPageId(){
         Survey survey = new Survey();
         survey.setIdSurvey(1);
         survey.setColorHeader("#ff");
         survey.setColorBg("#ff");
         survey.setIcon("icon");
         survey.setImgHeader("imgHeader");
         survey.setImgIcon("imgIcon");

         Page pageResult = new Page();
         pageResult.setTitle("page1");
         pageResult.setDesc("tentang page 1");
         pageResult.setId(1);
         pageResult.setPagesPicture("picture");

         survey.addPages(pageResult);

         when(surveyDao.loadSurveyBySurveyIdandPageId(1, "abcd", 1)).thenReturn(survey);
         Survey surveyResult = surveyDao.loadSurveyBySurveyIdandPageId(1, "abcd", 1);
         verify(surveyDao, times(1)).loadSurveyBySurveyIdandPageId(1, "abcd",1);
         assertEquals(1, surveyResult.getIdSurvey());
         assertEquals("icon", surveyResult.getIcon());
         assertEquals("tentang page 1", surveyResult.getPages().get(0).getDesc());
         assertEquals(1, surveyResult.getPages().get(0).getId());
     }

     @Test
     public void totalCountPage(){
        when(surveyDao.totalCountPage(1)).thenReturn(3);
        int totalPages = surveyDao.totalCountPage(1);
         verify(surveyDao, times(1)).totalCountPage(1);
         assertEquals(3, totalPages);
     }

     @Test
     public void loadOptionAnswerbySurveyIdPageIdandQuestionId(){
        List<Values> values = new ArrayList<>();
         Values value2 = new Values();
         value2.setChecked(false);
         value2.setValues("A");


         Values value3 = new Values();
         value3.setValues("B");
         value3.setChecked(true);

         values.add(value2);
         values.add(value3);

         when(surveyDao.loadOptionAnswerbySurveyIdPageIdandQuestionId(1, 0, 1, "abcd")).thenReturn(values);
         List<Values> results = surveyDao.loadOptionAnswerbySurveyIdPageIdandQuestionId(1, 0, 1, "abcd");
         verify(surveyDao, times(1)).loadOptionAnswerbySurveyIdPageIdandQuestionId(1, 0, 1, "abcd");
         assertEquals("A",results.get(0).getValues());
         assertEquals("B",results.get(1).getValues());
         assertEquals(true, results.get(1).getChecked());

     }

     @Test
     public void loadAnswerbySurveyIdPageIdandQuestionId(){
        when(surveyDao.loadAnswerbySurveyIdPageIdandQuestionId(1,0,0,"abcd")).thenReturn("Putra");
        String answer = surveyDao.loadAnswerbySurveyIdPageIdandQuestionId(1,0,0,"abcd");
        verify(surveyDao,times(1)).loadAnswerbySurveyIdPageIdandQuestionId(1,0,0,"abcd");
        assertEquals("Putra", answer);
     }

     @Test
     public void loadDateAnswerbySurveyIdPageIdandQuestionId(){
         String input = "2007-11-11 12:13:14" ;
         Timestamp ts = Timestamp.valueOf( input ) ;
         when(surveyDao.loadDateAnswerbySurveyIdPageIdandQuestionId(1,0,0,"abcd")).thenReturn(ts);
         Timestamp result = surveyDao.loadDateAnswerbySurveyIdPageIdandQuestionId(1,0,0,"abcd");
         verify(surveyDao,times(1)).loadDateAnswerbySurveyIdPageIdandQuestionId(1,0,0,"abcd");
         assertEquals(ts, result);
     }

     @Test
     public void loadUserId(){
        String id = "abcd";
        when(surveyDao.loadUserId(1, "abcd")).thenReturn(id);
        String result = surveyDao.loadUserId(1, "abcd");
        verify(surveyDao, times(1)).loadUserId(1, "abcd");
        assertEquals("abcd", result);

     }

     @Test
     public void loadSubmitStatus(){
        when(surveyDao.loadSubmitStatus(1,"abcd")).thenReturn(false);
        Boolean result = surveyDao.loadSubmitStatus(1, "abcd");
        verify(surveyDao, times(1)).loadSubmitStatus(1, "abcd");
        assertEquals(false, result);
     }

    @Test
    public void checkAnswerBySurveyIdPageIdQuestionId(){
        when(surveyDao.checkAnswerBySurveyIdPageIdQuestionId(1,0, 0, "abcd")).thenReturn(false);
        Boolean result = surveyDao.checkAnswerBySurveyIdPageIdQuestionId(1,0, 0, "abcd");
        verify(surveyDao, times(1)).checkAnswerBySurveyIdPageIdQuestionId(1,0, 0, "abcd");
        assertEquals(false, result);
    }


     @Test
     public void surveyPagination() throws SQLException {

     List<SurveyView> surveyViewList = new ArrayList<>();
     SurveyView surveyView = new SurveyView(4, "29-05-1994", "#ffff", "#ffff", "icon", "imgIcon", true, true, true,
     "form 1");
     surveyViewList.add(surveyView);

     when(surveyDao.surveyPagination("abcd",12, 1)).thenReturn(surveyViewList);
     List<SurveyView> newsurveyViewList = surveyDao.surveyPagination("abcd",12, 1);
     verify(surveyDao, times(1)).surveyPagination("abcd", 12, 1);

     assertEquals(4, newsurveyViewList.get(0).getIdSurvey());
     assertEquals("#ffff", newsurveyViewList.get(0).getColorHeader());
     assertEquals("icon", newsurveyViewList.get(0).getIcon());
     assertEquals(true, newsurveyViewList.get(0).getPrivateStatus());
     assertEquals(true, newsurveyViewList.get(0).getActiveStatus());
     assertEquals(true, newsurveyViewList.get(0).getPublishStatus());
     assertEquals("form 1", newsurveyViewList.get(0).getTitle());
     }

    @Test
    public void surveyPaginationSaveStatus() throws SQLException {

        List<SurveyView> surveyViewList = new ArrayList<>();
        SurveyView surveyView = new SurveyView(4, "29-05-1994", "#ffff", "#ffff", "icon", "imgIcon", true, true, false,
                "form 1");
        surveyViewList.add(surveyView);

        when(surveyDao.surveyPaginationSaveStatus("abcd",12, 1)).thenReturn(surveyViewList);
        List<SurveyView> newsurveyViewList = surveyDao.surveyPaginationSaveStatus("abcd",12, 1);
        verify(surveyDao, times(1)).surveyPaginationSaveStatus("abcd", 12, 1);

        assertEquals(4, newsurveyViewList.get(0).getIdSurvey());
        assertEquals("#ffff", newsurveyViewList.get(0).getColorHeader());
        assertEquals("icon", newsurveyViewList.get(0).getIcon());
        assertEquals(true, newsurveyViewList.get(0).getPrivateStatus());
        assertEquals(true, newsurveyViewList.get(0).getActiveStatus());
        assertEquals(false, newsurveyViewList.get(0).getPublishStatus());
        assertEquals("form 1", newsurveyViewList.get(0).getTitle());
    }

    @Test
    public void surveyPaginationSaveStatusOrderByASC() throws SQLException {

        List<SurveyView> surveyViewList = new ArrayList<>();
        SurveyView surveyView = new SurveyView(4, "29-05-1994", "#ffff", "#ffff", "icon", "imgIcon", true, true, false,
                "form 1");
        surveyViewList.add(surveyView);

        when(surveyDao.surveyPaginationSaveStatusOrderByASC("abcd",12, 1)).thenReturn(surveyViewList);
        List<SurveyView> newsurveyViewList = surveyDao.surveyPaginationSaveStatusOrderByASC("abcd",12, 1);
        verify(surveyDao, times(1)).surveyPaginationSaveStatusOrderByASC("abcd", 12, 1);

        assertEquals(4, newsurveyViewList.get(0).getIdSurvey());
        assertEquals("#ffff", newsurveyViewList.get(0).getColorHeader());
        assertEquals("icon", newsurveyViewList.get(0).getIcon());
        assertEquals(true, newsurveyViewList.get(0).getPrivateStatus());
        assertEquals(true, newsurveyViewList.get(0).getActiveStatus());
        assertEquals(false, newsurveyViewList.get(0).getPublishStatus());
        assertEquals("form 1", newsurveyViewList.get(0).getTitle());
    }




     @Test
     public void totalCount() throws SQLException {
     when(surveyDao.totalCount("abcd")).thenReturn(1);
     int totalCount = surveyDao.totalCount("abcd");
     verify(surveyDao, times(1)).totalCount("abcd");
     assertEquals(1, totalCount);
     }

    @Test
    public void main() throws SQLException {
        DemoApplication.main(new String[] {});
    }
}
