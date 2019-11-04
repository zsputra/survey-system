package com.emerio.surveysystem.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.emerio.surveysystem.model.*;
import com.google.gson.Gson;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class SurveyService {

    @Autowired
    SurveyDao surveyDao;

    @Autowired
    UserDao userDao;

    public void insert(Survey survey) throws SQLException {
        surveyDao.insert(survey);
    }

    public void inserBatch(List<Survey> surveys) {
        surveyDao.insertBatch(surveys);
    }

    public List<Survey> loadAlSurveys() throws Exception {
        return surveyDao.loadAllSurvey();
    }

    public Survey loadSurveyById(int survey_id) {
        return surveyDao.loadSurveyById(survey_id);
    }

    public void updateSurvey(Survey survey) {
        surveyDao.updateSurvey(survey);

    }

    public int getnerateId() throws SQLException {
        return surveyDao.generatedId();
    }


    public Page loadPageBySurveyIdandPageId(int survey_id, String user_id, int page_id) {
        return surveyDao.loadPageBySurveyIdandPageId(survey_id, user_id, page_id);
    }

    public Survey loadSurveyBySurveyIdandPageId(int survey_id, String user_id, int page_id) {
        return surveyDao.loadSurveyBySurveyIdandPageId(survey_id, user_id, page_id);
    }

    public int totalCountPage(int survey_id) {
        return surveyDao.totalCountPage(survey_id);
    }

    public List<Values> loadOptionAnswerbySurveyIdPageIdandQuestionId(int survey_id, int page_id, int question_id,
            String user_id) {
        return surveyDao.loadOptionAnswerbySurveyIdPageIdandQuestionId(survey_id, page_id, question_id, user_id);
    }

    public String loadAnswerbySurveyIdPageIdandQuestionId(int survey_id, int page_id, int question_id, String user_id) {
        return surveyDao.loadAnswerbySurveyIdPageIdandQuestionId(survey_id, page_id, question_id, user_id);
    }

    public Boolean checkAnswerBySurveyIdPageIdQuestionId(int idSurvey, int page_id, int question_id, String user_id) {
        return surveyDao.checkAnswerBySurveyIdPageIdQuestionId(idSurvey, page_id, question_id, user_id);
    }

    public List<SurveyView> surveyPagination(String idUser, int limit, int pageNo) throws SQLException {
        return surveyDao.surveyPagination(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationSaveStatus(String idUser, int limit, int pageNo) throws SQLException {
        return surveyDao.surveyPaginationSaveStatus(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationSaveStatusSortByASC(String idUser, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationSaveStatusOrderByASC(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationSaveStatusSortByDESC(String idUser, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationSaveStatusOrderByDESC(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationSaveStatusSortByDateASC(String idUser, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationSaveStatusOrderByDateASC(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationSaveStatusSortByDateDESC(String idUser, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationSaveStatusOrderByDateDESC(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationPublishStatus(String idUser, int limit, int pageNo) throws SQLException {
        return surveyDao.surveyPaginationPublishStatus(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationPublishStatusSortByASC(String idUser, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationPublishStatusSortByASC(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationPublishStatusSortByDESC(String idUser, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationPublishStatusSortByDESC(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationPublishStatusSortByDateASC(String idUser, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationPublishStatusSortByDateASC(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationPublishStatusSortByDateDESC(String idUser, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationPublishStatusSortByDateDESC(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationSortByDesc(String idUser, int limit, int pageNo) throws SQLException {
        return surveyDao.surveyPaginationSortDesc(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationSortByAsc(String idUser, int limit, int pageNo) throws SQLException {
        return surveyDao.surveyPaginationSortAsc(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationSortByLastModifiedDesc(String idUser, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationSortLastModifiedDesc(idUser, limit, pageNo);
    }

    public List<SurveyView> surveyPaginationSortByLastModifiedAsc(String idUser, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationSortLastModifiedAsc(idUser, limit, pageNo);
    }

    public List<SurveyViewAssign> surveyPaginationAssign(String userId, int limit, int pageNo) throws SQLException {
        return surveyDao.surveyPaginationAssign(userId, limit, pageNo);
    }

    public List<SurveyViewAssign> surveyPaginationAssignPrivateStatus(String userId, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationAssignPrivateStatus(userId, limit, pageNo);
    }

    public List<SurveyViewAssign> surveyPaginationAssignPublicStatus(String userId, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationAssignPublicStatus(userId, limit, pageNo);
    }

    public List<SurveyViewAssign> surveyPaginationAssignAnswered(String userId, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationAssignAnswered(userId, limit, pageNo);
    }

    public List<SurveyViewAssign> surveyPaginationAssignDismiss(String userId, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationAssignDismiss(userId, limit, pageNo);
    }

    public List<SurveyViewAssign> surveyPAginationAssignSortASC(String userId, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationAssignSortByASC(userId, limit, pageNo);

    }

    public List<SurveyViewAssign> surveyPaginationAssignSortDESC(String userId, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationAssignSortByDESC(userId, limit, pageNo);
    }

    public List<SurveyViewAssign> surveyPaginationAssignSortTimeASC(String userId, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationAssignSortByTimeASC(userId, limit, pageNo);
    }

    public List<SurveyViewAssign> surveyPaginationAssignSortTimeDESC(String userId, int limit, int pageNo)
            throws SQLException {
        return surveyDao.surveyPaginationAssignSortByTimeDESC(userId, limit, pageNo);
    }

    public String loadUserId(int idSurvey, String user_id) {
        return surveyDao.loadUserId(idSurvey, user_id);
    }

    public Boolean loadSubmitStatus(int idSurvey, String user_id) {
        return surveyDao.loadSubmitStatus(idSurvey, user_id);
    }

    public Boolean checkResponse(int idSurvey) {
        return surveyDao.chechResponse(idSurvey);
    }

    public void deleteSurveyById(int surveyId) {
        surveyDao.deleteSurveyById(surveyId);
    }

    public void deleteSurvey() {
        surveyDao.deleteSurveys();
    }

    // public int totalCount(String idUser) throws SQLException {
    // return surveyDao.totalCount(idUser);
    // }

    // public int totalCountAssign(String userId) throws SQLException {
    // return surveyDao.totalCountAssign(userId);
    // }

    public int totalCount(String idUser) throws SQLException {
        return surveyDao.totalCount(idUser);
    }

    public int totalCountSaveStatus(String idUser) throws SQLException {
        return surveyDao.totalCountSaveStatus(idUser);
    }

    public int totalCountPublishStatus(String idUser) throws SQLException {
        return surveyDao.totalCountPublishStatus(idUser);
    }

    public int totalCountAssign(String userId) throws SQLException {
        return surveyDao.totalCountAssign(userId);
    }

    public int totalCountAssignAnsweredStatus(String userId) throws SQLException {
        return surveyDao.totalCountAssignAnsweredStatus(userId);
    }

    public int totalCoutnAssignPrivateStatus(String userId) throws SQLException {
        return surveyDao.totalCountAssignPrivateStatus(userId);
    }

    public int totalCountAssignPublicStatus(String userId) throws SQLException {
        return surveyDao.totalCountAssignPublicStatus(userId);

    }

    public int totaCountAssignDismissStatus(String userId) throws SQLException {
        return surveyDao.totalCountAssignDismissStatus(userId);
    }

    public Boolean checkIdSurvey(int idSurvey) {
        return surveyDao.checkSurveyById(idSurvey);
    }

    public void insertResponse(Response response, List<String> user_id) {
        surveyDao.insertResponse(response, user_id);
    }

    public Survey cloneSurvey(int surveyId) throws SQLException {

        Survey survey = surveyDao.loadSurveyById(surveyId);
        int newSurveyId = surveyDao.generatedId();
        survey.setIdSurvey(newSurveyId);
        surveyDao.updateSurvey(survey);

        return survey;

    }


    public Document detailView(int index, int surveyId) throws Exception {
        Survey survey =  surveyDao.loadSurveyById(surveyId);
        String userId = surveyDao.getIdUserByIndex(surveyId, index);
        String username = getUsernameByIdUser(userId);
        for (Page page: survey.getPages()) {
            for (Question question: page.getQuestions()) {
                Response response = surveyDao.loadResponse(userId, surveyId, page.getId(), question.getId());
                question.setAnswer(response.getAnswer());
                question.setDateAnswer(response.getDateAnswer());
                question.setValues(response.getOptionAnswers());
            }
        }

        return detailResondedSurvey(survey, username);
    }

    public Document detailResondedSurvey(Survey survey, String username){
        List<Document> pages = new ArrayList<>();


        Document surveyDetailed = new Document();
        surveyDetailed.put("idSurvey", survey.getIdSurvey());
        surveyDetailed.put("respondent", username);
        surveyDetailed.put("colorHeader", survey.getColorHeader());
        surveyDetailed.put("colorBg", survey.getColorBg());
        surveyDetailed.put("icon", survey.getIcon());
        surveyDetailed.put("imgHeader", survey.getImgHeader());
        surveyDetailed.put("imgIcon", survey.getImgIcon());

        for (Page page: survey.getPages()) {
            Document page_doc = new Document();
            page_doc.put("id", page.getId());
            page_doc.put("title", page.getTitle());
            page_doc.put("desc", page.getDesc());

            List<Document> questions = new ArrayList<>();

            for (Question question: page.getQuestions()) {
                Document question_doc = new Document();
                question_doc.put("id", question.getId());
                question_doc.put("question", question.getQuestion());
                question_doc.put("description", question.getDescription());
                question_doc.put("questionType", question.getQuestionType());
                question_doc.put("imgPage", question.getImgPage());
                question_doc.put("imgQuestion", question.getImgQuestion());
                question_doc.put("answer", question.getAnswer());
                if(question.getDateAnswer() != null) {
                    String date = new SimpleDateFormat("mm-dd-yyyy").format(question.getDateAnswer());
                    question_doc.put("dateAnswer", date);
                } else{
                    question_doc.put("dateAnswer", null);
                }

                List<Document> values = new ArrayList<>();

                for (Values value: question.getValues()) {
                    Document value_doc = new Document();
                    value_doc.put("id", value.getId());
                    value_doc.put("values", value.getValues());
                    value_doc.put("checked", value.getChecked());
                    value_doc.put("imgValue", value.getImgValue());
                    values.add(value_doc);
                }
                question_doc.put("values", values);
                questions.add(question_doc);
            }
            page_doc.put("questions", questions);
            pages.add(page_doc);
        }
        surveyDetailed.put("pages", pages);
        return surveyDetailed;
    }

    public String getUsernameByIdUser(String idUser){
        return userDao.getUsenameById(idUser);
    }

    public Document getUsersResponseHigView(int idSurvey, int pageNo) throws SQLException {
        Document response = new Document();
        Survey survey = surveyDao.loadSurveyById(idSurvey);
        if(surveyDao.numberOfUserSubmitted(idSurvey) == 0){

            response.put("idSurvey", idSurvey);
            response.put("totalResponse", surveyDao.totalResponseBySurvey(idSurvey));
            response.put("title", survey.getPages().get(0).getTitle());
            response.put("colorHeader", survey.getColorHeader());
            response.put("colorBg",survey.getColorBg());
            response.put("imgHeader", survey.getImgHeader());
            response.put("icon", survey.getIcon());
            response.put("imgIcon", survey.getImgIcon());
            response.put("response", null);
            return response;
        }else {
            List<String> users = surveyDao.getOrderedSubmittedUser(idSurvey, pageNo);
            List<Document> userResponses = new ArrayList<>();
            for (String idUser : users) {
                String username = getUsernameByIdUser(idUser);
                int index = (pageNo - 1) * 12 + users.indexOf(idUser);
                Document doc = new Document();
                doc.put("respondent", username);
                doc.put("responseIndex", index);
                userResponses.add(doc);


            }

            response.put("idSurvey", idSurvey);
            int totalresponse = surveyDao.totalResponseBySurvey(idSurvey);
            int totalPage = 0;
            if (totalresponse % 12 == 0) {
                totalPage = totalresponse / 12;
            } else {
                totalPage = totalresponse / 12 + 1;
            }

            response.put("totalResponse", totalresponse);
            response.put("totalPage", totalPage);
            response.put("title", survey.getPages().get(0).getTitle());
            response.put("colorHeader", survey.getColorHeader());
            response.put("colorBg", survey.getColorBg());
            response.put("imgHeader", survey.getImgHeader());
            response.put("icon", survey.getIcon());
            response.put("imgIcon", survey.getImgIcon());
            response.put("response", userResponses);
            return response;
        }
    }

    public void assignSurvey(Document payload){
        List<String> userId = (List<String>) payload.get("userList");
        int survey_id = payload.getInteger("idSurvey");

        surveyDao.assignSurvey(survey_id, userId);
        Survey survey = loadSurveyById(survey_id);

        survey.setPrivateStatus(payload.getBoolean("privateStatus"));
        survey.setPublishStatus(true);
        survey.setActiveStatus(payload.getBoolean("activeStatus"));

        surveyDao.updateStatusSurvey(survey);

        if (checkResponse(survey_id) == false) {
            List<Response> responses = new ArrayList<>();

            for (Page page : survey.getPages()) {
                Gson gson = new Gson();
                for (Question question : page.getQuestions()) {
                    String json = gson.toJson(question);

                    Response response1 = new Response();

                    response1.setPageId(page.getId());
                    response1.setSurveyId(survey.getIdSurvey());
                    response1.setQuestionId(question.getId());
                    List<Values> listOptionAnswer = new ArrayList<>();
                    for (Values value : question.getValues()) {
                        Values optionAnswer = new Values();
                        optionAnswer.setId(value.getId());
                        optionAnswer.setValues(value.getValues());
                        optionAnswer.setChecked(value.getChecked());
                        optionAnswer.setImgValue(value.getImgValue());
                        listOptionAnswer.add(optionAnswer);
                    }
                    response1.setAnswer(question.getAnswer());
                    response1.setDateAnswer(question.getDateAnswer());
                    response1.setOptionAnswers(listOptionAnswer);
                    response1.setQuestionType(question.getQuestionType());
                    // responses.add(response1);
                    insertResponse(response1, userId);
                }
            }
        }
    }

    public void updateResponse(Document response) throws Exception {

        List<Response> responses = new ArrayList<>();
        List<Object> questions = (List<Object>) response.get("questions");
        Gson gson = new Gson();
        int idSurvey = response.getInteger("idSurvey");
        String idUser = response.getString("idUser");
        surveyDao.updateSubmitStatus(response.getInteger("idSurvey"), response.getString("idUser"),
                response.getBoolean("submitStatus"));
        for (Object question : questions) {
            String json = gson.toJson(question);

            Document doc = Document.parse(json);
            Response response1 = new Response();

            response1.setPageId(response.getInteger("id"));
            response1.setSurveyId(response.getInteger("idSurvey"));
            response1.setQuestionId(doc.getInteger("id"));
            response1.setUserId(response.getString("idUser"));

            if (doc.getString("questionType").equalsIgnoreCase("checkbox")) {
                List<Document> values = (List<Document>) doc.get("values");
                List<Values> optionAnswers = new ArrayList<>();
                for (Document value : values) {
                    Values optionAnswer = new Values();
                    optionAnswer.setId(value.getInteger("id"));
                    optionAnswer.setValues(value.getString("values"));
                    optionAnswer.setChecked(value.getBoolean("checked"));
                    optionAnswer.setImgValue(value.getString("imgValue"));
                    optionAnswers.add(optionAnswer);

                }
                response1.setQuestionType(doc.getString("questionType"));
                response1.setOptionAnswers(optionAnswers);
                response1.setAnswer(doc.getString("answer"));
                surveyDao.updateResponseCheckbox(response1);

            } else {
                Values answer = new Values();
                answer.setId(0);
                answer.setValues(doc.getString("answer"));

                response1.setAnswer(doc.getString("answer"));
                if (doc.getString("questionType").equalsIgnoreCase("date_type")) {
                    LocalDateTime localDateTime = LocalDateTime.parse(doc.getString("dateAnswer"),
                            DateTimeFormatter.ISO_DATE_TIME);

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date parsedDate = dateFormat.parse(doc.getString("dateAnswer"));
                        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                        response1.setDateAnswer(timestamp);
                    } catch(Exception e) { //this generic but you can control another types of exception
                        // look the origin of excption
                    }

                } else {
                    response1.setDateAnswer(null);
                }
                surveyDao.updateResponseBesideCheckbox(response1);

            }
        }
        if(response.getBoolean("submitStatus")){
            List<Response> ress = surveyDao.loadResponsesByIdSurveyandIdUser(idSurvey, idUser);
            surveyDao.moveToResponse(ress);
            for (Response r: ress) {
                surveyDao.deleteResponseTempByUserIdandSurveyId(r.getSurveyId(), r.getUserId());
            }

        }
    }

    public Map<String, Object> responseDashboardPagination(
            int totalRecords, List<SurveyView> surveyViews, int pageNo)
            throws SQLException {

        int p = 0;
        if (totalRecords % 8 == 0) {
            p = totalRecords / 8;
        } else {
            p = totalRecords / 8 + 1;
        }

        if (pageNo <= p) {
            Map<String, Object> response = new HashMap<>();
            response.put("surveyView", surveyViews);
            response.put("totalPages", p);

            return response;
        } else {
            return null;
        }
    }

    public Map<String, Object> responseDashboardPaginationAssign(
            int totalRecords, List<SurveyViewAssign> surveyViews, int pageNo)
            throws SQLException {

        int p = 0;
        if (totalRecords % 8 == 0) {
            p = totalRecords / 8;
        } else {
            p = totalRecords / 8 + 1;
        }

        if (pageNo <= p) {
            Map<String, Object> response = new HashMap<>();
            response.put("surveyView", surveyViews);
            response.put("totalPages", p);

            return response;
        } else {
            return null;
        }
    }



}