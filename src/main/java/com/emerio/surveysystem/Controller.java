package com.emerio.surveysystem;

import com.emerio.surveysystem.model.*;
import com.emerio.surveysystem.service.FileService;
import com.emerio.surveysystem.service.SurveyService;
import com.google.gson.Gson;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// @RequestMapping("api/survey")
@RestController()

public class Controller {

    @Value("${app.kong.host}")
    private String kongHost;

    @Value("${app.kong.port}")
    private int kongPort;

    @Autowired
    FileService minioService;

    @Autowired
    UserDao userDao;

    @Autowired
    SurveyService surveyService;

    @Autowired
    SurveyDao surveyDao;

    @GetMapping("/generate-id-survey")
    public ResponseEntity<Integer> generateIdSurvey() throws SQLException {
        return new ResponseEntity<Integer>(surveyService.getnerateId(), HttpStatus.OK);
    }

    @PostMapping("/add-survey")
    public ResponseEntity addSurvey(@RequestBody Survey survey) {

        boolean exists = surveyService.checkIdSurvey(survey.getIdSurvey());
        if (exists == true) {
            surveyService.updateSurvey(survey);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/load-all-survey")
    public ResponseEntity<List<Survey>> getAllSurvey() {
        try {
            return new ResponseEntity<List<Survey>>(surveyService.loadAlSurveys(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/load-survey-by-id/{survey_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Survey> getSurveyById(@PathVariable(name = "survey_id") int idSurvey) {

        boolean exists = surveyService.checkIdSurvey(idSurvey);
        if (exists == true) {
            return new ResponseEntity<Survey>(surveyService.loadSurveyById(idSurvey), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/load-survey-by-survey-id-and-page-id/{user_id}/{survey_id}/{page_id}")
    public ResponseEntity<Document> getSurveyByUserIdSurveyIdandPageId(@PathVariable(name = "user_id") String idUser,
            @PathVariable(name = "survey_id") int idSurvey, @PathVariable(name = "page_id") int page_id) {

        Survey newSurvey = surveyService.loadSurveyBySurveyIdandPageId(idSurvey, idUser, page_id);
        Page newPage = surveyService.loadPageBySurveyIdandPageId(idSurvey, idUser, page_id);
        int totalpage = surveyService.totalCountPage(idSurvey) - 1;

        String user_id = surveyService.loadUserId(idSurvey, idUser);
        Boolean submitStatus = surveyService.loadSubmitStatus(idSurvey, idUser);

        for (Question question : newPage.getQuestions()) {

            boolean answerExist = surveyService.checkAnswerBySurveyIdPageIdQuestionId(idSurvey, page_id,
                    question.getId(), idUser);

            if (answerExist == false) {
                question.setAnswer(null);
                question.setDateAnswer(null);
                // question.setValues(null);
            } else {

                if (question.getQuestionType().equalsIgnoreCase("CHECKBOX")) {
                    List<Values> values = surveyService.loadOptionAnswerbySurveyIdPageIdandQuestionId(idSurvey, page_id,
                            question.getId(), idUser);
                    question.setValues(values);
                    question.setAnswer(null);
                    question.setDateAnswer(null);
                } else {
                    // question.setValues(null);
                    String answer = surveyService.loadAnswerbySurveyIdPageIdandQuestionId(idSurvey, page_id,
                            question.getId(), idUser);

                    Timestamp dateAnswer = surveyDao.loadDateAnswerbySurveyIdPageIdandQuestionId(idSurvey, page_id,
                            question.getId(), idUser);

                    question.setAnswer(answer);
                    question.setDateAnswer(dateAnswer);
                }
            }
//            question.setValidateType("LENGTH");
//            question.setValidateSetting("MIN");
//            question.setValidateValue(5);

        }

        Document doc = new Document();

        doc.put("idSurvey", newSurvey.getIdSurvey());
        doc.put("idUser", user_id);
        doc.put("submitStatus", submitStatus);
        doc.put("colorHeader", newSurvey.getColorHeader());
        doc.put("colorBg", newSurvey.getColorBg());
        doc.put("icon", newSurvey.getIcon());
        doc.put("imgHeader", newSurvey.getImgHeader());
        doc.put("imgIcon", newSurvey.getImgIcon());
        doc.put("totalpages", totalpage);
        doc.put("id", newPage.getId());
        doc.put("title", newPage.getTitle());
        doc.put("desc", newPage.getDesc());
        doc.put("questions", newPage.getQuestions());

        return new ResponseEntity<Document>(doc, HttpStatus.OK);

        // catch (Exception e){
        // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // }
    }

    @RequestMapping(value = "/find-all-survey-pagination/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPagination(@PathVariable(name = "idUser") String idUser,
            @PathVariable(name = "pageNo") int pageNo) throws SQLException {
        try {
            int totalRecords = surveyService.totalCount(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPagination(idUser, 8, pageNo);
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-save-status/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSaveStatus(@PathVariable(name = "idUser") String idUser,
            @PathVariable(name = "pageNo") int pageNo) throws SQLException {

        try {
            int totalRecords = surveyService.totalCountSaveStatus(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationSaveStatus(idUser, 8, pageNo);;
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-save-status-sortby-asc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSaveStatusSortByASC(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {
            int totalRecords = surveyService.totalCountSaveStatus(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationSaveStatusSortByASC(idUser, 8, pageNo);;
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-save-status-sortby-desc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSaveStatusSortByDESC(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountSaveStatus(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationSaveStatusSortByDESC(idUser, 8, pageNo);;
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-save-status-sortby-date-asc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSaveStatusSortByDateASC(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountSaveStatus(idUser);

            List<SurveyView> surveyViews = surveyService.surveyPaginationSaveStatusSortByDateASC(idUser, 8,
                        pageNo);
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-save-status-sortby-date-desc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSaveStatusSortByDateDESC(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountSaveStatus(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationSaveStatusSortByDateDESC(idUser, 8,
                        pageNo);
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-publish-status/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSubmitStatus(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountPublishStatus(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationPublishStatus(idUser, 8, pageNo);;
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-publish-status-sortby-asc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationPublishsortByASC(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountPublishStatus(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationPublishStatusSortByASC(idUser, 8, pageNo);;
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-publish-status-sortby-desc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSubmitStatusSortByDESC(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountPublishStatus(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationPublishStatusSortByDESC(idUser, 8,
                        pageNo);
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-publish-status-sortby-date-asc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSubmitStatusSortByDateASC(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountPublishStatus(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationPublishStatusSortByDateASC(idUser, 8,
                        pageNo);
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-publish-statussortby-date-desc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSubmitStatusSortByDateDESC(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountPublishStatus(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationPublishStatusSortByDateDESC(idUser, 8,
                        pageNo);
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-sort-bydesc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSortBydesc(@PathVariable(name = "idUser") String idUser,
            @PathVariable(name = "pageNo") int pageNo) throws SQLException {

        try {

            int totalRecords = surveyService.totalCount(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationSortByDesc(idUser, 8, pageNo);;
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-sort-byasc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSortByasc(@PathVariable(name = "idUser") String idUser,
            @PathVariable(name = "pageNo") int pageNo) throws SQLException {

        try {

            int totalRecords = surveyService.totalCount(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationSortByAsc(idUser, 8, pageNo);;
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-sort-by-last-modified-desc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSortByLastModifiedDesc(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCount(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationSortByLastModifiedDesc(idUser, 8, pageNo);
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-sort-by-last-modified-asc/{idUser}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationSortByLastModifiedAsc(
            @PathVariable(name = "idUser") String idUser, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCount(idUser);
            List<SurveyView> surveyViews = surveyService.surveyPaginationSortByLastModifiedAsc(idUser, 8, pageNo);;
            Map<String, Object> response = surveyService.responseDashboardPagination(totalRecords, surveyViews, pageNo);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-assign/{user_id}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationAssign(@PathVariable(name = "user_id") String userId,
            @PathVariable(name = "pageNo") int pageNo) throws SQLException {

        try {

            int totalRecords = surveyService.totalCountAssign(userId);
            List<SurveyViewAssign> surveyViews = surveyService.surveyPaginationAssign(userId, 8, pageNo);
            Map<String, Object> response = surveyService.responseDashboardPaginationAssign(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-assign-private-status/{user_id}/{privatestatus}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationAssignActiveStatus(
            @PathVariable(name = "user_id") String userId, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountAssign(userId);
            List<SurveyViewAssign> surveyViews = surveyService.surveyPaginationAssignPrivateStatus(userId, 8,
                        pageNo);
            Map<String, Object> response = surveyService.responseDashboardPaginationAssign(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-assign-public-status/{user_id}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationAssignInactiveStatus(
            @PathVariable(name = "user_id") String userId, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {
            int totalRecords = surveyService.totalCountAssignPublicStatus(userId);
            List<SurveyViewAssign> surveyViews = surveyService.surveyPaginationAssignPublicStatus(userId, 8,
                        pageNo);
            Map<String, Object> response = surveyService.responseDashboardPaginationAssign(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-assign-answered-status/{user_id}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationAssignAnswered(
            @PathVariable(name = "user_id") String userId, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {
            int totalRecords = surveyService.totalCountAssignAnsweredStatus(userId);
            List<SurveyViewAssign> surveyViews = surveyService.surveyPaginationAssignAnswered(userId, 8, pageNo);
            Map<String, Object> response = surveyService.responseDashboardPaginationAssign(totalRecords, surveyViews, pageNo);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-assign-dismiss-status/{user_id}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationAssignDismiss(
            @PathVariable(name = "user_id") String userId, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {
            int totalRecords = surveyService.totaCountAssignDismissStatus(userId);
            List<SurveyViewAssign> surveyViews = surveyService.surveyPaginationAssignDismiss(userId, 8, pageNo);
            Map<String, Object> response = surveyService.responseDashboardPaginationAssign(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-assign-sortAsc/{user_id}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationAssignSortASc(
            @PathVariable(name = "user_id") String userId, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountAssign(userId);
            List<SurveyViewAssign> surveyViews = surveyService.surveyPAginationAssignSortASC(userId, 8, pageNo);
            Map<String, Object> response = surveyService.responseDashboardPaginationAssign(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-assign-sortDesc/{user_id}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationAssignsortDesc(
            @PathVariable(name = "user_id") String userId, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountAssign(userId);
            List<SurveyViewAssign> surveyViews = surveyService.surveyPaginationAssignSortDESC(userId, 8, pageNo);
            Map<String, Object> response = surveyService.responseDashboardPaginationAssign(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-assign-lastmodifiedASC/{user_id}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationAssignLastModifiedASC(
            @PathVariable(name = "user_id") String userId, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountAssign(userId);
            List<SurveyViewAssign> surveyViews = surveyService.surveyPaginationAssignSortTimeASC(userId, 8,
                        pageNo);
            Map<String, Object> response = surveyService.responseDashboardPaginationAssign(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/find-all-survey-pagination-assign-lastmodifiedDESC/{user_id}/{pageNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> surveyPaginationAssignLastModifiedDESC(
            @PathVariable(name = "user_id") String userId, @PathVariable(name = "pageNo") int pageNo)
            throws SQLException {

        try {

            int totalRecords = surveyService.totalCountAssign(userId);
            List<SurveyViewAssign> surveyViews = surveyService.surveyPaginationAssignSortTimeDESC(userId, 8,
                        pageNo);
            Map<String, Object> response = surveyService.responseDashboardPaginationAssign(totalRecords, surveyViews, pageNo);

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/update-response")
    public void updateResponse(@RequestBody Document response) throws Exception {

        surveyService.updateResponse(response);
    }



    @DeleteMapping("/delete-survey/{surveyId}")
    public ResponseEntity<Void> deleteSurveyById(@PathVariable int surveyId) {

        surveyService.deleteSurveyById(surveyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/find-all-user")
    public ResponseEntity<List<User>> selectAllUser() {

        try {
            return new ResponseEntity<>(userDao.selectAllUser(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/assign-survey")
    public void assignSurvey(@RequestBody Document payload) {

        surveyService.assignSurvey(payload);

    }

    @GetMapping("/coba23")
    public Survey check() throws SQLException {

        Survey survey = new Survey();
        survey.setIdSurvey(2);
        survey.setIdUser("6c371d17-286f-4b2f-8be6-8108ff193c57");
        survey.setColorHeader("#c03232");
        survey.setColorBg("#ffffff");
        survey.setPrivateStatus(false);
        survey.setPublishStatus(false);

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

        survey.addPages(page1);
        survey.addPages(page2);

        String a = "adfadf.tafdafds.adfdas.jpeg";
        List<String> list = Arrays.asList(a.split("\\."));
        // return list.get(list.size() - 1);
        return survey;
    }

    @DeleteMapping("/dismiss-survey/{user_id}/{survey_id}")
    public void dismissSurvey(@PathVariable(name = "user_id") String userId, @PathVariable(name = "survey_id") int surveyId){

        surveyDao.dismissSurvey(userId, surveyId);

    }

    @PostMapping("/clone-survey/{survey_id}")
    public Survey cloneSurvey( @PathVariable(name = "survey_id") int surveyId) throws SQLException {
        return surveyService.cloneSurvey(surveyId);
    }


    @GetMapping("/detail-view/{idSurvey}/{index}")
    public Document getDetail(@PathVariable(name = "index") int index, @PathVariable(name = "idSurvey") int idSurvey) throws Exception {
        if(surveyDao.numberOfUserSubmitted(idSurvey) == 0){
            return null;
        } else {
            return surveyService.detailView(index, idSurvey);
        }
    }

    @GetMapping("/high-view/{idSurvey}/{pageNo}")
    public Document paginationUsersHighView(@PathVariable(name = "idSurvey") int idSurvey, @PathVariable(name = "pageNo") int pageNo) throws SQLException {
            return surveyService.getUsersResponseHigView(idSurvey, pageNo);

    }

    @GetMapping("/coba")
    public List<Values> coba() throws SQLException {

        return surveyService.loadOptionAnswerbySurveyIdPageIdandQuestionId(321, 0, 3, "f080ff70-36dd-45b8-a37a-349046f5af34");
    }


}
