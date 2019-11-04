package com.emerio.surveysystem.model;

import com.emerio.surveysystem.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SurveyDao extends JdbcDaoSupport {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    private Survey survey;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public void insert(Survey survey) {

        String sql = "INSERT INTO Survey" + "(pages) VALUES(to_jsonb(?::jsonb))";
        getJdbcTemplate().update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(survey.getPages());
                ps.setString(1, json);

                return ps;

            }
        });
    }

    public void insertBatch(List<Survey> surveys) {

        String sql = "INSERT INTO Survey" + "(pages) VALUES(to_jsonb(?::jsonb))";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Survey survey = surveys.get(i);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(survey.getPages());
                ps.setString(1, json);

            }

            @Override
            public int getBatchSize() {
                return surveys.size();
            }
        });
    }

    public List<Survey> loadAllSurvey() throws Exception {
        String sql = "SELECT * FROM Survey";
        List<Survey> surveys = jdbcTemplate.query(sql, new SurveyRowMapper());
        if (surveys.isEmpty()) {
            throw new Exception("not found");
        }
        return surveys;

    }

    public Survey loadSurveyById(int idSurvey) {
        String sql = "SELECT * FROM Survey WHERE survey_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { idSurvey }, new SurveyRowMapper());
    }

    public Boolean checkSurveyById(int idSurvey) {
        String sql = "SELECT COUNT(1) FROM Survey WHERE survey_id = ?";
        int count = getJdbcTemplate().queryForObject(sql, Integer.class, idSurvey);
        if (count == 0) {
            return false;
        } else {
            return true;
        }

    }

    public void updateSurvey(Survey survey) {

        String sql = "UPDATE Survey SET createdby = ?, lastmodified = ?, colorheader = ?, colorbg = ?, imgHeader = ?, imgLogo = ?, icon = ?, privatestatus = ?, activestatus = ?, publishstatus =?,  pages =  to_jsonb(?::jsonb) WHERE survey_id = ?";

        Gson gson = new Gson();
        String json = gson.toJson(survey.getPages());
        survey.setLastModified();
        Object[] params = { survey.getIdUser(), survey.getLastModified(), survey.getColorHeader(), survey.getColorBg(),
                survey.getImgHeader(), survey.getImgIcon(), survey.getIcon(), survey.getPrivateStatus(),
                survey.getActiveStatus(), survey.getPublishStatus(), json, survey.getIdSurvey() };

        jdbcTemplate.update(sql, params);

    }

    public void deleteSurveyById(int surveyId) {

        String sql = "DELETE FROM survey where survey_id = ?";

        Object[] params = { surveyId };

        jdbcTemplate.update(sql, params);

    }

    public void deleteSurveys() {
        String sql = "DELETE FROM survey where survey_id = ?";

        jdbcTemplate.update(sql);
    }

    public int generatedId() throws SQLException {

        Survey survey = new Survey();
        Page pageA1 = new Page();
        survey.addPages(pageA1);

        String sql = "INSERT INTO Survey" + "(pages) VALUES(to_jsonb(?::jsonb))";

        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(survey.getPages());
            ps.setString(1, json);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    survey.setIdSurvey(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }

        return survey.getIdSurvey();
    }


    public Page loadPageBySurveyIdandPageId(int idSurvey, String idUser, int idPage) {
        String sql = "SELECT pages FROM (SELECT DISTINCT jsonb_array_elements(pages) AS pages, response_temp_user_id from Survey INNER JOIN "
                + "response_temp ON response_temp.response_temp_survey_id = survey.survey_id WHERE survey_id = ? and response_temp_user_id = ?) "
                + "AS pages WHERE (pages ->> 'id')::int = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { idSurvey, idUser, idPage }, new PageRowMapper());
    }

    public Survey loadSurveyBySurveyIdandPageId(int idSurvey, String idUser, int idPage) {
        String sql = "SELECT * FROM (SELECT DISTINCT survey.survey_id, colorheader, colorbg, icon, imgHeader, imgLogo, response_temp_user_id, "
                + "jsonb_array_elements(pages) AS pages FROM Survey INNER JOIN response_temp ON response_temp.response_temp_survey_id = survey.survey_id "
                + "WHERE survey_id =? and response_temp_user_id = ?) AS new_survey WHERE (pages ->> 'id')::int = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { idSurvey, idUser, idPage }, new SurveyPageRowMapper());
    }

    public int totalCountPage(int idSurvey) {
        String sql = "SELECT COUNT (*) FROM (SELECT jsonb_array_elements(pages) AS pages from Survey WHERE survey_id = ?) as pages";

        return jdbcTemplate.queryForObject(sql, new Object[] { idSurvey }, Integer.class);
    }

    public List<Values> loadOptionAnswerbySurveyIdPageIdandQuestionId(int idSurvey, int idPage, int idQuestion,
            String idUser) {
        String sql = "SELECT optionanswer from response_temp WHERE response_temp_survey_id = ? and response_temp_page_id = ? and question_temp_id = ? "
                + "and response_temp_user_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { idSurvey, idPage, idQuestion, idUser },
                new ValueRowMapper());
    }

    public String loadAnswerbySurveyIdPageIdandQuestionId(int idSurvey, int idPage, int idQuestion, String idUser) {
        String sql = "SELECT answer from response_temp WHERE response_temp_survey_id = ? and response_temp_page_id = ? and question_temp_id = ? and "
                + "response_temp_user_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { idSurvey, idPage, idQuestion, idUser }, String.class);
    }

    public Timestamp loadDateAnswerbySurveyIdPageIdandQuestionId(int idSurvey, int idPage, int idQuestion,
                                                                 String idUser) {
        String sql = "SELECT dateanswer from response_temp WHERE response_temp_survey_id = ? and response_temp_page_id = ? and question_temp_id = ? and "
                + "response_temp_user_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { idSurvey, idPage, idQuestion, idUser },
                Timestamp.class);
    }

    public String loadUserId(int idSurvey, String idUser) {
        String sql = "SELECT DISTINCT response_temp_user_id from response_temp WHERE response_temp_survey_id = ? and response_temp_user_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { idSurvey, idUser }, String.class);
    }

    public Boolean loadSubmitStatus(int idSurvey, String idUser) {
        String sql = "SELECT submitresponse from user_survey WHERE survey_id = ? and user_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { idSurvey, idUser }, Boolean.class);
    }

    public Boolean checkAnswerBySurveyIdPageIdQuestionId(int idSurvey, int idPage, int idQuestion, String idUser) {
        String sql = "SELECT EXISTS (SELECT dateanswer , answer, optionanswer from response_temp WHERE response_temp_survey_id = ? and response_temp_page_id = ? and "
                + "question_temp_id = ? and response_temp_user_id = ?)";

        boolean exist = getJdbcTemplate().queryForObject(sql, new Object[] { idSurvey, idPage, idQuestion, idUser },
                Boolean.class);

        return exist;
    }

    public List<SurveyView> surveyPagination(String idUser, int limit, int pageNo) throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id, lastmodified, colorHeader,imgHeader, icon, imgLogo,privatestatus,activestatus, publishstatus,obj.val->> 'title' AS title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->>'id'='0' WHERE createdby = ? ORDER BY survey_id DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationSaveStatus(String idUser, int limit, int pageNo) throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id, lastmodified, colorHeader,imgHeader, icon, imgLogo,privatestatus,activestatus, publishstatus,obj.val->> 'title' AS title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val  ->>'id'='0' WHERE createdby = ? AND publishstatus = false ORDER BY survey_id ASC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);

            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationSaveStatusOrderByASC(String idUser, int limit, int pageNo)
            throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id, lastmodified, colorHeader,imgHeader, icon, imgLogo,privatestatus,activestatus, publishstatus,obj.val->> 'title' AS title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val  ->>'id'='0' WHERE createdby = ? AND publishstatus = false ORDER BY obj.val->> 'title' ASC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);

            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationSaveStatusOrderByDESC(String idUser, int limit, int pageNo)
            throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id, lastmodified, colorHeader,imgHeader, icon, imgLogo,privatestatus,activestatus, publishstatus,obj.val->> 'title' AS title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val  ->>'id'='0' WHERE createdby = ? AND publishstatus = false ORDER BY obj.val->> 'title' DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);

            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationSaveStatusOrderByDateASC(String idUser, int limit, int pageNo)
            throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id, lastmodified, colorHeader,imgHeader, icon, imgLogo,privatestatus,activestatus, publishstatus,obj.val->> 'title' AS title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val  ->>'id'='0' WHERE createdby = ? AND publishstatus = false ORDER BY lastmodified ASC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);

            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationSaveStatusOrderByDateDESC(String idUser, int limit, int pageNo)
            throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id, lastmodified, colorHeader,imgHeader, icon, imgLogo,privatestatus,activestatus, publishstatus,obj.val->> 'title' AS title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val  ->>'id'='0' WHERE createdby = ? AND publishstatus = false ORDER BY lastmodified DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);

            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationPublishStatus(String idUser, int limit, int pageNo) throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id,lastmodified, colorHeader,imgHeader, icon, imgLogo, privatestatus,activestatus, publishstatus,obj.val->> 'title' AS  title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON  obj.val ->>'id'='0' WHERE createdby = ? AND publishstatus = true ORDER BY survey_id DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationPublishStatusSortByASC(String idUser, int limit, int pageNo)
            throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id,lastmodified, colorHeader,imgHeader, icon, imgLogo, privatestatus,activestatus, publishstatus,obj.val->> 'title' AS  title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON  obj.val ->>'id'='0' WHERE createdby = ? AND publishstatus = true ORDER BY obj.val->> 'title' ASC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationPublishStatusSortByDESC(String idUser, int limit, int pageNo)
            throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id,lastmodified, colorHeader,imgHeader, icon, imgLogo, privatestatus,activestatus, publishstatus,obj.val->> 'title' AS  title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON  obj.val ->>'id'='0' WHERE createdby = ? AND publishstatus = true ORDER BY obj.val->> 'title' DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationPublishStatusSortByDateASC(String idUser, int limit, int pageNo)
            throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id,lastmodified, colorHeader,imgHeader, icon, imgLogo, privatestatus,activestatus, publishstatus,obj.val->> 'title' AS  title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON  obj.val ->>'id'='0' WHERE createdby = ? AND publishstatus = true ORDER BY lastmodified ASC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationPublishStatusSortByDateDESC(String idUser, int limit, int pageNo)
            throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id,lastmodified, colorHeader,imgHeader, icon, imgLogo, privatestatus,activestatus, publishstatus,obj.val->> 'title' AS  title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON  obj.val ->>'id'='0' WHERE createdby = ? AND publishstatus = true ORDER BY lastmodified DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationSortDesc(String idUser, int limit, int pageNo) throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id, lastmodified, colorHeader, imgHeader, icon, imgLogo, privatestatus,activestatus, publishstatus,obj.val->> 'title' AS title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->>'id'='0'  WHERE createdBy = ?  ORDER BY obj.val->> 'title' DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);

            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);
                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationSortAsc(String idUser, int limit, int pageNo) throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id, lastmodified, colorHeader, imgHeader, icon, imgLogo, privatestatus,activestatus, publishstatus,obj.val->> 'title' AS title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->>'id'='0'  WHERE createdBy = ? ORDER BY obj.val->> 'title' ASC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);
                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationSortLastModifiedDesc(String idUser, int limit, int pageNo)
            throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id, lastmodified, colorHeader, imgHeader, icon, imgLogo, privatestatus,activestatus, publishstatus,obj.val->> 'title' AS title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->>'id'='0' WHERE createdBy = ?  ORDER BY lastmodified DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyView> surveyPaginationSortLastModifiedAsc(String idUser, int limit, int pageNo)
            throws SQLException {
        List<SurveyView> surveyviews = new ArrayList<>();
        String sql = "select survey_id,lastmodified, colorHeader, imgHeader, icon, imgLogo, privatestatus,activestatus, publishstatus,obj.val->> 'title' AS title from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->>'id'='0' WHERE createdBy = ? ORDER BY lastmodified ASC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, idUser);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    LocalDateTime lastModified = rs.getObject("lastmodified", LocalDateTime.class);
                    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");

                    SurveyView surveyview = new SurveyView(idSurvey, formattedDate, colorHeader, imgHeader, icon,
                            imgIcon, privateStatus, activeStatus, publishStatus, title);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyViewAssign> surveyPaginationAssign(String userId, int limit, int pageNo) throws SQLException {
        List<SurveyViewAssign> surveyviews = new ArrayList<>();
        String sql = "select survey.survey_id, survey.colorHeader, survey.imgHeader, survey.icon, survey.imgLogo, survey.privatestatus,survey.activestatus, survey.publishstatus, obj.val->> 'title' AS title, user_survey.submitresponse, user_survey.dismissStatus, user_survey.lastmodifiedAssign from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->> 'id'='0' join user_survey ON survey.survey_id= user_survey.survey_id WHERE user_id = ? AND submitresponse = false AND dismissStatus =  false   ORDER BY survey_id DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, userId);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");
                    Boolean submitStatus = rs.getBoolean("submitresponse");
                    Boolean dismissStatus = rs.getBoolean("dismissStatus");
                    LocalDateTime lastModifiedAssign = rs.getObject("lastmodifiedAssign", LocalDateTime.class);
                    String formattedDate = lastModifiedAssign.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));

                    SurveyViewAssign surveyview = new SurveyViewAssign(idSurvey, colorHeader, imgHeader, icon, imgIcon,
                            privateStatus, activeStatus, publishStatus, title, submitStatus, dismissStatus,
                            formattedDate);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyViewAssign> surveyPaginationAssignPrivateStatus(String userId, int limit, int pageNo)
            throws SQLException {
        List<SurveyViewAssign> surveyviews = new ArrayList<>();
        String sql = "select survey.survey_id,  survey.colorHeader, survey.imgHeader, survey.icon, survey.imgLogo, survey.privatestatus,survey.activestatus, survey.publishstatus, obj.val->> 'title' AS title, user_survey.submitresponse,user_survey.dismissStatus, user_survey.lastmodifiedAssign from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->> 'id'='0' join user_survey ON survey.survey_id= user_survey.survey_id WHERE user_id = ?  AND privatestatus = ? ORDER BY survey_id DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, userId);
            // p.setInt(1, limit);

            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");
                    Boolean submitStatus = rs.getBoolean("submitresponse");
                    Boolean dismissStatus = rs.getBoolean("dismissStatus");
                    LocalDateTime lastModifiedAssign = rs.getObject("lastmodifiedAssign", LocalDateTime.class);
                    String formattedDate = lastModifiedAssign.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));

                    SurveyViewAssign surveyview = new SurveyViewAssign(idSurvey, colorHeader, imgHeader, icon, imgIcon,
                            privateStatus, activeStatus, publishStatus, title, submitStatus, dismissStatus,
                            formattedDate);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyViewAssign> surveyPaginationAssignPublicStatus(String userId, int limit, int pageNo)
            throws SQLException {
        List<SurveyViewAssign> surveyviews = new ArrayList<>();
        String sql = "select survey.survey_id, survey.colorHeader, survey.imgHeader,survey.icon, survey.imgLogo, survey.privatestatus,survey.activestatus, survey.publishstatus, obj.val->> 'title' AS title,user_survey.submitresponse,user_survey.dismissStatus,  user_survey.lastmodifiedAssign from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->> 'id'='0' join user_survey ON survey.survey_id= user_survey.survey_id WHERE user_id = ? AND privatestatus = false ORDER BY survey_id DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, userId);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");
                    Boolean submitStatus = rs.getBoolean("submitresponse");
                    Boolean dismissStatus = rs.getBoolean("dismissStatus");
                    LocalDateTime lastModifiedAssign = rs.getObject("lastmodifiedAssign", LocalDateTime.class);
                    String formattedDate = lastModifiedAssign.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));

                    SurveyViewAssign surveyview = new SurveyViewAssign(idSurvey, colorHeader, imgHeader, icon, imgIcon,
                            privateStatus, activeStatus, publishStatus, title, submitStatus, dismissStatus,
                            formattedDate);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyViewAssign> surveyPaginationAssignAnswered(String userId, int limit, int pageNo)
            throws SQLException {
        List<SurveyViewAssign> surveyviews = new ArrayList<>();
        String sql = "select survey.survey_id, survey.colorHeader, survey.imgHeader, survey.icon, survey.imgLogo, survey.privatestatus,survey.activestatus, survey.publishstatus, obj.val->> 'title' AS title, user_survey.submitresponse, user_survey.dismissStatus, user_survey.lastmodifiedAssign from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->> 'id'='0' join user_survey ON survey.survey_id= user_survey.survey_id WHERE user_id = ? AND submitresponse = true  ORDER BY survey_id DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, userId);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");
                    Boolean submitStatus = rs.getBoolean("submitresponse");
                    Boolean dismissStatus = rs.getBoolean("dismissStatus");
                    LocalDateTime lastModifiedAssign = rs.getObject("lastmodifiedAssign", LocalDateTime.class);
                    String formattedDate = lastModifiedAssign.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));

                    SurveyViewAssign surveyview = new SurveyViewAssign(idSurvey, colorHeader, imgHeader, icon, imgIcon,
                            privateStatus, activeStatus, publishStatus, title, submitStatus, dismissStatus,
                            formattedDate);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyViewAssign> surveyPaginationAssignDismiss(String userId, int limit, int pageNo)
            throws SQLException {
        List<SurveyViewAssign> surveyviews = new ArrayList<>();
        String sql = "select survey.survey_id, survey.colorHeader, survey.imgHeader, survey.icon, survey.imgLogo, survey.privatestatus,survey.activestatus, survey.publishstatus, obj.val->> 'title' AS title, user_survey.submitresponse, user_survey.dismissStatus, user_survey.lastmodifiedAssign from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->> 'id'='0' join user_survey ON survey.survey_id= user_survey.survey_id WHERE user_id = ? AND dismissStatus = true  ORDER BY survey_id DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, userId);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");
                    Boolean submitStatus = rs.getBoolean("submitresponse");
                    Boolean dismissStatus = rs.getBoolean("dismissStatus");
                    LocalDateTime lastModifiedAssign = rs.getObject("lastmodifiedAssign", LocalDateTime.class);
                    String formattedDate = lastModifiedAssign.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));


                    SurveyViewAssign surveyview = new SurveyViewAssign(idSurvey, colorHeader, imgHeader, icon, imgIcon,
                            privateStatus, activeStatus, publishStatus, title, submitStatus, dismissStatus,
                            formattedDate);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public int totalCount(String idUser) throws SQLException {
        String sql = "SELECT COUNT (*) FROM SURVEY Where createdBy = ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, idUser);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");

            }
        } catch (Exception e) {
            throw new SQLException("The Page Number is Wrong.");
        }
        return 0;

    }

    public int totalCountSaveStatus(String idUser) throws SQLException {
        String sql = "SELECT COUNT (*) FROM SURVEY Where createdBy = ? AND publishstatus = false";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, idUser);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");

            }
        } catch (Exception e) {
            throw new SQLException("The Page Number is Wrong.");
        }
        return 0;

    }

    public int totalCountPublishStatus(String idUser) throws SQLException {
        String sql = "SELECT COUNT (*) FROM SURVEY Where createdBy = ? AND publishstatus = true";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, idUser);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");

            }
        } catch (Exception e) {
            throw new SQLException("The Page Number is Wrong.");
        }
        return 0;

    }

    public int totalCountAssign(String userId) throws SQLException {
        String sql = "SELECT COUNT (*) FROM user_survey Where user_id = ? AND submitresponse = false ";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, userId);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");

            }
        } catch (Exception e) {
            throw new SQLException("The Page Number is Wrong.");
        }
        return 0;

    }

    public int totalCountAssignPrivateStatus(String userId) throws SQLException {
        String sql = "select count (*) from user_survey join survey on survey.survey_id = user_survey.survey_id where   user_id = ? AND privateStatus = true";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, userId);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");

            }
        } catch (Exception e) {
            throw new SQLException("The Page Number is Wrong.");
        }
        return 0;
    }

    public int totalCountAssignPublicStatus(String userId) throws SQLException {
        String sql = "select count (*) from user_survey join survey on survey.survey_id = user_survey.survey_id where   user_id = ? AND privateStatus = false";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, userId);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");

            }
        } catch (Exception e) {
            throw new SQLException("The Page Number is Wrong.");
        }
        return 0;
    }

    public int totalCountAssignAnsweredStatus(String userId) throws SQLException {
        String sql = "SELECT COUNT (*) FROM user_survey Where user_id = ? AND submitresponse = true ";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, userId);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");

            }
        } catch (Exception e) {
            throw new SQLException("The Page Number is Wrong.");
        }
        return 0;
    }

    public int totalCountAssignDismissStatus(String userId) throws SQLException {
        String sql = "SELECT COUNT (*) FROM user_survey Where user_id = ? AND dismissStatus = true";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, userId);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");

            }
        } catch (Exception e) {
            throw new SQLException("The Page Number is Wrong.");
        }
        return 0;
    }

    public List<SurveyViewAssign> surveyPaginationAssignSortByASC(String userId, int limit, int pageNo)
            throws SQLException {
        List<SurveyViewAssign> surveyviews = new ArrayList<>();
        String sql = "select survey.survey_id, survey.colorHeader, survey.imgHeader, survey.icon, survey.imgLogo, survey.privatestatus,survey.activestatus, survey.publishstatus, obj.val->> 'title' AS title, user_survey.submitresponse, user_survey.dismissStatus, user_survey.lastmodifiedAssign from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->> 'id'='0' join user_survey ON survey.survey_id= user_survey.survey_id WHERE user_id = ? AND submitresponse = false  ORDER BY obj.val->> 'title' DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, userId);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");
                    Boolean submitStatus = rs.getBoolean("submitresponse");
                    Boolean dismissStatus = rs.getBoolean("dismissStatus");
                    LocalDateTime lastModifiedAssign = rs.getObject("lastmodifiedAssign", LocalDateTime.class);
                    String formattedDate = lastModifiedAssign.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));


                    SurveyViewAssign surveyview = new SurveyViewAssign(idSurvey, colorHeader, imgHeader, icon, imgIcon,
                            privateStatus, activeStatus, publishStatus, title, submitStatus, dismissStatus,
                            formattedDate);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyViewAssign> surveyPaginationAssignSortByDESC(String userId, int limit, int pageNo)
            throws SQLException {
        List<SurveyViewAssign> surveyviews = new ArrayList<>();
        String sql = "select survey.survey_id, survey.colorHeader, survey.imgHeader, survey.icon, survey.imgLogo, survey.privatestatus,survey.activestatus, survey.publishstatus, obj.val->> 'title' AS title, user_survey.submitresponse, user_survey.dismissStatus, user_survey.lastmodifiedAssign from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->> 'id'='0' join user_survey ON survey.survey_id= user_survey.survey_id WHERE user_id = ? AND submitresponse = false  ORDER BY obj.val->> 'title' ASC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, userId);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");
                    Boolean submitStatus = rs.getBoolean("submitresponse");
                    Boolean dismissStatus = rs.getBoolean("dismissStatus");
                    LocalDateTime lastModifiedAssign = rs.getObject("lastmodifiedAssign", LocalDateTime.class);
                    String formattedDate = lastModifiedAssign.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));


                    SurveyViewAssign surveyview = new SurveyViewAssign(idSurvey, colorHeader, imgHeader, icon, imgIcon,
                            privateStatus, activeStatus, publishStatus, title, submitStatus, dismissStatus,
                            formattedDate);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyViewAssign> surveyPaginationAssignSortByTimeASC(String userId, int limit, int pageNo)
            throws SQLException {
        List<SurveyViewAssign> surveyviews = new ArrayList<>();
        String sql = "select survey.survey_id, survey.colorHeader, survey.imgHeader, survey.icon, survey.imgLogo, survey.privatestatus,survey.activestatus, survey.publishstatus, obj.val->> 'title' AS title, user_survey.submitresponse, user_survey.dismissStatus, user_survey.lastmodifiedAssign from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->> 'id'='0' join user_survey ON survey.survey_id= user_survey.survey_id WHERE user_id = ? AND submitresponse = false  ORDER BY user_survey.lastmodifiedAssign ASC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, userId);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");
                    Boolean submitStatus = rs.getBoolean("submitresponse");
                    Boolean dismissStatus = rs.getBoolean("dismissStatus");
                    LocalDateTime lastModifiedAssign = rs.getObject("lastmodifiedAssign", LocalDateTime.class);
                    String formattedDate = lastModifiedAssign.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));


                    SurveyViewAssign surveyview = new SurveyViewAssign(idSurvey, colorHeader, imgHeader, icon, imgIcon,
                            privateStatus, activeStatus, publishStatus, title, submitStatus, dismissStatus,
                            formattedDate);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public List<SurveyViewAssign> surveyPaginationAssignSortByTimeDESC(String userId, int limit, int pageNo)
            throws SQLException {
        List<SurveyViewAssign> surveyviews = new ArrayList<>();
        String sql = "select survey.survey_id, survey.colorHeader, survey.imgHeader, survey.icon, survey.imgLogo, survey.privatestatus,survey.activestatus, survey.publishstatus, obj.val->> 'title' AS title, user_survey.submitresponse, user_survey.dismissStatus, user_survey.lastmodifiedAssign from survey JOIN LATERAL jsonb_array_elements(pages) obj(val) ON obj.val ->> 'id'='0' join user_survey ON survey.survey_id= user_survey.survey_id WHERE user_id = ? AND submitresponse = false  ORDER BY user_survey.lastmodifiedAssign DESC LIMIT 8 OFFSET ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setString(1, userId);
            // p.setInt(1, limit);
            p.setInt(2, (pageNo - 1) * limit);
            try (ResultSet rs = p.executeQuery()) {

                while (rs.next()) {
                    int idSurvey = rs.getInt("survey_id");
                    String colorHeader = rs.getString("colorHeader");
                    String imgHeader = rs.getString("imgHeader");
                    String icon = rs.getString("icon");
                    String imgIcon = rs.getString("imgLogo");
                    Boolean privateStatus = rs.getBoolean("privatestatus");
                    Boolean activeStatus = rs.getBoolean("privatestatus");
                    Boolean publishStatus = rs.getBoolean("publishstatus");
                    String title = rs.getString("title");
                    Boolean submitStatus = rs.getBoolean("submitresponse");
                    Boolean dismissStatus = rs.getBoolean("dismissStatus");
                    LocalDateTime lastModifiedAssign = rs.getObject("lastmodifiedAssign", LocalDateTime.class);
                    String formattedDate = lastModifiedAssign.format(DateTimeFormatter.ofPattern("dd-mm-yyyy"));


                    SurveyViewAssign surveyview = new SurveyViewAssign(idSurvey, colorHeader, imgHeader, icon, imgIcon,
                            privateStatus, activeStatus, publishStatus, title, submitStatus, dismissStatus,
                            formattedDate);

                    surveyviews.add(surveyview);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return surveyviews;

    }

    public void insertResponse(Response response, List<String> user_id) {
        String sql = "INSERT INTO response_temp ( response_temp_survey_id,response_temp_page_id,question_temp_id, answer,  optionanswer, questionType, response_temp_user_id) VALUES (?,?,?,?,to_jsonb(?::jsonb),?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(7, user_id.get(i));
                ps.setInt(1, response.getSurveyId());
                ps.setInt(2, response.getPageId());
                ps.setInt(3, response.getQuestionId());
                ps.setString(4, response.getAnswer());
                // ps.setDate(5, response.getDateAnswer());

                Gson gson = new Gson();
                String optionAnswer = gson.toJson(response.getOptionAnswers());
                ps.setString(5, optionAnswer);
                ps.setString(6, response.getQuestionType());
            }

            public int getBatchSize() {
                return user_id.size();
            }

        });

    }

    public void assignSurvey(int idSurvey, List<String> user_id) {
        String sql = "INSERT INTO user_survey(user_id, survey_id, submitresponse,dismissStatus, lastmodifiedAssign) Values(?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, user_id.get(i));
                ps.setInt(2, idSurvey);
                ps.setBoolean(3, false);
                ps.setBoolean(4, false);
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            }

            public int getBatchSize() {
                return user_id.size();
            }

        });

    }

    public void updateResponseCheckbox(Response response) {

        String sql = "UPDATE Response_temp SET answer = ?, optionanswer = to_jsonb(?::jsonb) WHERE response_temp_survey_id = ? AND response_temp_page_id = ? AND question_temp_id = ? AND response_temp_user_id = ?";
        Gson gson = new Gson();
        String json = gson.toJson(response.getOptionAnswers());
        Object[] params = { response.getAnswer(), json, response.getSurveyId(), response.getPageId(),
                response.getQuestionId(), response.getUserId() };

        jdbcTemplate.update(sql, params);

    }

    public void updateResponseBesideCheckbox(Response response) {

        String sql = "UPDATE Response_temp SET answer = ?, dateanswer = ? WHERE response_temp_survey_id = ? AND response_temp_page_id = ? AND question_temp_id = ? AND response_temp_user_id = ?";
        Gson gson = new Gson();
        String json = gson.toJson(response.getAnswer());
        Object[] params = { response.getAnswer(), response.getDateAnswer(), response.getSurveyId(),
                response.getPageId(), response.getQuestionId(), response.getUserId() };

        jdbcTemplate.update(sql, params);
    }

    public Boolean chechResponse(int idSurvey) {

        String sql = "SELECT EXISTS (SELECT answer from response_temp WHERE response_temp_survey_id = ?) ";
        boolean exist = getJdbcTemplate().queryForObject(sql, new Object[] { idSurvey }, Boolean.class);

        return exist;
    }

    public void updateStatusSurvey(Survey survey) {

        String sql = "UPDATE Survey SET privatestatus = ?, activestatus = ?, publishstatus =? WHERE survey_id = ?";

        Object[] params = { survey.getPrivateStatus(), survey.getActiveStatus(), survey.getPublishStatus(),
                survey.getIdSurvey() };

        jdbcTemplate.update(sql, params);

    }

    public void updateSubmitStatus(int idSurvey, String idUser, boolean submitStatus) {

        String sql = "UPDATE user_survey SET submitresponse = ? WHERE user_id = ? AND survey_id = ?";
        Object[] params = { submitStatus, idUser, idSurvey };

        jdbcTemplate.update(sql, params);

    }

    public void dismissSurvey(String userId, int surveyId) {

        String sql = "UPDATE user_survey SET dismissStatus = ? WHERE survey_id = ? AND user_id = ?";
        Object[] params = { true, surveyId, userId };

        jdbcTemplate.update(sql, params);

    }

    public Response loadResponse(String userId, int surveyId, int pageId, int questionId) throws Exception {

        String sql = "select * from response where  response_survey_id = ? AND response_page_id = ? AND question_id = ? AND response_user_id = ?";

            return jdbcTemplate.queryForObject(sql, new Object[] { surveyId, pageId, questionId, userId }, new ResponseRowMapper());

    }
    public int checkNumberOfResponse(String userId, int surveyId, int pageId, int questionId) throws Exception {

        String sql = "select count(*) from( select * from response where  response_survey_id = ? AND response_page_id = ? AND question_id = ? AND response_user_id = ?) as result";

        return jdbcTemplate.queryForObject(sql, new Object[] { surveyId, pageId, questionId, userId }, Integer.class);

    }


    public void moveToResponse(List<Response> responses) {
        String sql = "INSERT INTO response(response_survey_id,response_page_id,question_id, answer, dateanswer,  optionanswer, questionType, response_user_id) values(?,?,?,?,?,to_jsonb(?::jsonb),?,?)"
   ;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, responses.get(i).getSurveyId());
                ps.setInt(2, responses.get(i).getPageId());
                ps.setInt(3, responses.get(i).getQuestionId());
                ps.setString(4, responses.get(i).getAnswer());
                ps.setTimestamp(5,responses.get(i).getDateAnswer());
                Gson gson = new Gson();
                String optionAnswer = gson.toJson(responses.get(i).getOptionAnswers());
                ps.setString(6, optionAnswer);
                ps.setString(7, responses.get(i).getQuestionType());
                ps.setString(8, responses.get(i).getUserId());
            }

            public int getBatchSize() {
                return responses.size();
            }

        });

    }

    public List<Response> loadResponsesByIdSurveyandIdUser(int surveyId, String userId) throws Exception {
        String sql = "SELECT * FROM response_temp"
                + " WHERE response_temp_survey_id = ? AND response_temp_user_id = ?";
        List<Response> responses = jdbcTemplate.query(sql, new Object[] { surveyId, userId }, new ResponseTempRowMapper());
        if (responses.isEmpty()) {
            throw new Exception("not found");
        }
        return responses;

    }

    public void deleteResponseTempByUserIdandSurveyId(int surveyId, String userId) {

        String sql = "DELETE FROM response_temp where response_temp_survey_id = ? AND response_temp_user_id = ?";

        Object[] params = { surveyId , userId};

        jdbcTemplate.update(sql, params);

    }

    public List<Document> getUserSurveySubmitted() throws SQLException {
        String sql = "SELECT user_id, survey_id FROM user_survey WHERE submitresponse = ?";
        List<Document> docs = new ArrayList<>();
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setBoolean(1, true);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {

                    int surveyId = rs.getInt("survey_id");
                    String userId = rs.getString("user_id");

                    Document doc = new Document();
                    doc.put("idSurvey", surveyId);
                    doc.put("idUser", userId);
                    docs.add(doc);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return docs;
    }

    public int numberOfUserSubmitted(int idSurvey) throws SQLException {
        String sql = "select count(*) from (SELECT user_id FROM user_survey WHERE submitresponse = true and survey_id = ?) as result";
        return getJdbcTemplate().queryForObject(sql, new Object[]{idSurvey}, Integer.class);
    }

    public List<String> getOrderedSubmittedUser(int idSurvey,  int pageNo) throws SQLException {
        String sql = "select user_id from user_survey where survey_id = ? AND submitresponse = ? order by lastmodifiedAssign DESC limit 12 offset ?";

        List<String> idUsers = new ArrayList<>();
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            int offset = (pageNo - 1) * 12;
            p.setInt(1, idSurvey);
            p.setBoolean(2, true);
            p.setInt(3, offset);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {

                    String userId = rs.getString("user_id");

                    idUsers.add(userId);

                }
            } catch (Exception e) {
                throw new SQLException("cannot get the result set.");
            }
        } catch (Exception e) {
            throw new SQLException("cannot connect to database.");
        }
        return idUsers;
    }

    public String getIdUserByIndex(int idSurvey, int index) throws SQLException {
        String sql = "select user_id  from user_survey where survey_id = ? AND submitresponse = ? order by lastmodifiedAssign DESC limit 12 offset ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement p = con.prepareStatement(sql);) {

            p.setInt(1, idSurvey);
            p.setBoolean(2, true);
            p.setInt(3, index);
            ResultSet rs = p.executeQuery();
            rs.next();
            return rs.getString("user_id");
        }



    }

    public int totalResponseBySurvey(int idSurvey) {
        String sql = "select COUNT(*) from (select user_id from user_survey where survey_id = ? AND submitresponse = ? order by lastmodifiedAssign DESC) as ordered";
        return getJdbcTemplate().queryForObject(sql, new Object[]{idSurvey, true}, Integer.class);


    }





}
