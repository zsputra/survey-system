package com.emerio.surveysystem;


import com.emerio.surveysystem.model.*;
import com.emerio.surveysystem.service.SurveyService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@DirtiesContext
public class RestTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    SurveyService surveyService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    private Survey survey ;
    private Survey surveyA;
    private Survey surveyB;
    private Survey surveyC;
    private int idSurvey;


    @Before()
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.surveyB = new Survey();

        this.surveyC = new Survey();
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


        this.surveyA = new Survey();


        Page pageA1 = new Page();
        pageA1.setId(1);
        pageA1.setTitle("survey untuk mengetahui tingkat kecerdasan anak");
        pageA1.setDesc("halaman1");

        pageA1.addQuestions(questionA1);
        pageA1.addQuestions(questionA2);

        Page pageA2 = new Page();
        pageA2.setId(2);
        pageA2.setTitle("title 2");
        pageA2.setDesc("halaman2");


        Page pageB1 = new Page();
        pageB1.setId(3);
        pageB1.setTitle("title 3");
        pageB1.setDesc("halaman3");


        this.surveyA.addPages(pageA1);
        this.surveyA.addPages(pageA2);
        this.surveyA.addPages(pageB1);

        this.surveyB.addPages(pageA1);
        this.surveyB.addPages(pageA2);
        this.surveyB.addPages(pageB1);

        this.surveyC.addPages(pageA1);
        this.surveyC.addPages(pageA2);
        this.surveyC.addPages(pageB1);

        List<Survey> surveys = new ArrayList<>();
        surveys.add(this.surveyB);
        surveys.add(this.surveyC);

        surveyService.insert(this.surveyA);
        surveyService.inserBatch(surveys);

        this.idSurvey = surveyService.getnerateId();

        this.survey = new Survey();
        this.survey.setIdSurvey(idSurvey);
        this.survey.setIdUser("6c371d17-286f-4b2f-8be6-8108ff193c57");
        this.survey.setColorHeader("#c03232");
        this.survey.setColorBg("#ffffff");
        this.survey.setPrivateStatus(false);
        this.survey.setPublishStatus(false);

        Page page1 = new Page();
        page1.setId(0);
        page1.setTitle("Survey 1");
        page1.setDesc("tentang survey 1");

        Question question00 = new Question();
        question00.setId(0);
        question00.setQuestion("siapa nama kamu?");
        question00.setDescription(null);
        question00.setQuestionType("SHORT_ANSWER");
        question00.setRequiredValue(false);

        Values values = new Values();
        values.setId(0);
        values.setValues("Option 0");
        values.setChecked(false);
        values.setImgValue(null);

        question00.addValues(values);

        Question question01 = new Question();
        question01.setId(1);
        question01.setQuestion("deskripsikan dirimu");
        question01.setDescription(null);
        question01.setQuestionType("LONG_ANSWER");
        question01.setRequiredValue(false);

        Values values1 = new Values();
        values1.setId(0);
        values1.setValues("Option 0");
        values1.setChecked(false);
        values1.setImgValue(null);

        question01.addValues(values1);
        page1.addQuestions(question00);
        page1.addQuestions(question01);

        Page page2 = new Page();
        page2.setId(1);
        page2.setTitle("Form 2");
        page2.setDesc("tentang form 2");

        Question question10 = new Question();
        question10.setId(0);
        question10.setQuestion("2+2?");
        question10.setDescription(null);
        question10.setQuestionType("MULTIPLE");
        question10.setRequiredValue(false);

        Values values100 = new Values();
        values100.setId(0);
        values100.setValues("4");
        values100.setChecked(false);
        values100.setImgValue(null);

        Values values101 = new Values();
        values101.setId(1);
        values101.setValues("4");
        values101.setChecked(false);
        values101.setImgValue(null);

        Values values103 = new Values();
        values103.setId(2);
        values103.setValues("6");
        values103.setChecked(false);
        values103.setImgValue(null);

        question10.addValues(values100);
        question10.addValues(values101);
        question10.addValues(values103);

        Question question11 = new Question();
        question11.setId(1);
        question11.setQuestion("checklist yang sesuai dengan dirimu");
        question11.setDescription(null);
        question11.setQuestionType("CHECKBOX");
        question11.setRequiredValue(false);

        Values values110 = new Values();
        values110.setId(0);
        values110.setValues("Baik");
        values110.setChecked(false);
        values110.setImgValue(null);

        Values values111 = new Values();
        values111.setId(1);
        values111.setValues("Rajin menabung");
        values111.setChecked(false);
        values111.setImgValue(null);

        Values values113 = new Values();
        values113.setId(2);
        values113.setValues("Jahat");
        values113.setChecked(false);
        values113.setImgValue(null);

        question11.addValues(values110);
        question11.addValues(values111);
        question11.addValues(values113);

        Question question12 = new Question();
        question12.setId(1);
        question12.setQuestion("apa jenis kelaminmu?");
        question12.setDescription(null);
        question12.setQuestionType("DROPDOWN");
        question12.setRequiredValue(false);

        Values values120 = new Values();
        values120.setId(0);
        values120.setValues("Male");
        values120.setChecked(false);
        values120.setImgValue(null);

        Values values121 = new Values();
        values121.setId(1);
        values121.setValues("Female");
        values121.setChecked(false);
        values121.setImgValue(null);

        Values values123 = new Values();
        values123.setId(2);
        values123.setValues("Half");
        values123.setChecked(false);
        values123.setImgValue(null);

        question12.addValues(values120);
        question12.addValues(values121);
        question12.addValues(values123);


        page2.addQuestions(question10);
        page2.addQuestions(question11);
        page2.addQuestions(question12);


        this.survey.addPages(page1);
        this.survey.addPages(page2);

        surveyService.updateSurvey(this.survey);

    }

    @Test
    public void generateIdSurvey() throws Exception {
        ResultActions ra = mockMvc.perform(get("/generate-id-survey"));
        ra.andExpect(content().contentType(contentType));
        ra.andExpect(status().isOk());
    }

    @Test
    public void  createSurvey() throws Exception {

        int id = surveyService.getnerateId();
        Survey survey = new Survey();
        survey.setIdSurvey(id);

        Page pageA1 = new Page();
        pageA1.setId(1);
        pageA1.setTitle("survey badfarewfaw");
        pageA1.setDesc("halaman1");

        Page pageA2 = new Page();
        pageA2.setId(2);
        pageA2.setTitle("title 2");
        pageA2.setDesc("halaman2");


        Page pageB1 = new Page();
        pageB1.setId(3);
        pageB1.setTitle("title 3");
        pageB1.setDesc("halaman3");


        survey.addPages(pageA1);
        survey.addPages(pageA2);
        survey.addPages(pageB1);
        mockMvc.perform(post("/add-survey").content(this.json(survey)).contentType(contentType)).andExpect(status().isCreated());
    }

    @Test
    public void createSurveyWithUnknownField() throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("idSurvey", 41232);
        json.addProperty("tileSurvey", "survey salah");

        mockMvc.perform(post("/add-survey").content(json.toString()).contentType(contentType)).andExpect(status().isBadRequest());

    }

    @Test
    public void loadAllSurvey() throws Exception {
        ResultActions ra = mockMvc.perform(get("/load-all-survey"));
        ra.andExpect(content().contentType(contentType));
        ra.andExpect(status().isOk());

    }

//    @Test
//    public void loadAllSurveyNotFound() throws Exception {
//        surveyService.deleteSurvey();
//        ResultActions ra = mockMvc.perform(get("/load-all-survey"));
//        ra.andExpect(status().isNotFound());
//
//    }
//
//    @Test
//    public  void getSurveyById() throws Exception {
//        int id = surveyService.getnerateId();
//        Survey survey = new Survey();
//        List<Page> pages = new ArrayList<>();
//        Page page = new Page();
//        pages.add(page);
//        survey.setPages(pages);
//        survey.setIdSurvey(id);
//        ResultActions ra = mockMvc.perform(get("/load-survey-by-id" + "/" + id));
//        ra.andExpect(content().contentType(contentType));
//        ra.andExpect(content().json(this.json(survey)));
//        ra.andExpect(status().isOk());
//    }

    @Test
    public  void getSurveyByIdBadRequest() throws Exception {
        int id = 342421;
        ResultActions ra = mockMvc.perform(get("/load-survey-by-id" + "/" + id));
        ra.andExpect(status().isBadRequest());
    }

//    @Test
//    public void getSurveyPageOneById() throws Exception {
//        int id = surveyService.getnerateId();
//        Survey survey4 = new Survey();
//        survey4.setIdSurvey(id);
//
//        Page pageA1 = new Page();
//        pageA1.setId(1);
//        pageA1.setTitle("survey test page one");
//        pageA1.setDesc("halaman1");
//
//        survey4.addPages(pageA1);
//        surveyService.updateSurvey(survey4);
//        ResultActions ra = mockMvc.perform(get("/load-survey-by-id-page-one/" + id));
//        ra.andExpect(content().contentType(contentType));
//        ra.andExpect(content().json(this.json(survey4)));
//        ra.andExpect(status().isOk());
//
//
//    }
//
//    @Test
//    public void getSurveyPageOneByIdBadRequest() throws Exception {
//        surveyService.deleteSurvey();
//        ResultActions ra = mockMvc.perform(get("/load-survey-by-id-page-one/" + 1));
//        ra.andExpect(status().isBadRequest());
//
//
//    }
//
//    @Test
//    public void getSurveyByidPerPage() throws Exception {
//        int id = surveyService.getnerateId();
//
//        Survey survey = new Survey();
//        survey.setIdSurvey(id);
//
//        Page pageA1 = new Page();
//        pageA1.setId(1);
//        pageA1.setTitle("survey badfarewfaw");
//        pageA1.setDesc("halaman1");
//
//        Page pageA2 = new Page();
//        pageA2.setId(2);
//        pageA2.setTitle("title 2");
//        pageA2.setDesc("halaman2");
//
//
//        Page pageB1 = new Page();
//        pageB1.setId(3);
//        pageB1.setTitle("title 3");
//        pageB1.setDesc("halaman3");
//
//
//        survey.addPages(pageA1);
//        survey.addPages(pageA2);
//        survey.addPages(pageB1);
//
//        surveyService.updateSurvey(survey);
//
//        ResultActions ra = mockMvc.perform(get("/load-page-by-survey-id-and-page-id/" + id +"/"+pageA1.getId()));
//        ra.andExpect(content().contentType(contentType));
//        ra.andExpect(content().json(this.json(pageA2)));
//        ra.andExpect(status().isOk());
//    }
//
//    @Test
//    public void getSurveyByidPerPageBadRequest() throws Exception {
//        surveyService.deleteSurvey();
//        ResultActions ra = mockMvc.perform(get("/load-page-by-survey-id-and-page-id/" + 1 +"/"+"0"));
//        ra.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void surveyPagination() throws Exception {
//
//        ResultActions ra = mockMvc.perform(get("/find-all-survey-pagination/" + 1));
//        ra.andExpect(status().isOk());
//    }
//
//    @Test
//    public void surveyPaginationPageNumber0() throws Exception {
//        ResultActions ra = mockMvc.perform(get("/find-all-survey-pagination/" + 0));
//        ra.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void surveyPaginationPageNumberGreatherThanTotalPage() throws Exception {
//        ResultActions ra = mockMvc.perform(get("/find-all-survey-pagination/" + 100099090));
//        ra.andExpect(status().isBadRequest());
//    }

    @Test
    public void assignSurvey() throws Exception {

        List<String> userList = Arrays.asList("8c3aa91c-faa0-4df1-b207-d74d8aa88952","26226a3c-9111-4be0-9f2e-180eb0fa6d19","09236a22-897d-41f0-b4d5-f3c083e1a7be","b6e996b2-039b-453f-97f7-e60e6967ceae");

        Document payload = new Document();
        payload.append("idSurvey", this.idSurvey);
        payload.append("userList", userList);
        payload.append("saveStatus", true);
        payload.append("privateStatus", false);
        payload.append("activeStatus", true);
        payload.append("publishStatus", true);

        Gson gson = new Gson();
        String json = gson.toJson(payload);

        mockMvc.perform(post("/assign-survey").content(json.toString()).contentType(contentType)).andExpect(status().isOk());
    }





    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }








}
