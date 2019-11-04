package com.emerio.surveysystem.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserDao {

    @Value("${datasource.url.keycloak}")
    private String keycloakDbUrl;

    @Value("${datasource.username.keycloak}")
    private String keycloakDbUsername;

    @Value("${datasource.password.keycloak}")
    private String keycloakDbPassword;

    // @Autowired
    // JdbcTemplate jdbcTemplate;

    public List<User> selectAllUser() {

        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setUrl(keycloakDbUrl);
        ds.setUsername(keycloakDbUsername);
        ds.setPassword(keycloakDbPassword);

        //try(Connection connection = DriverManager.getConnection("jdbc:postgresql://192.168.25.11:8300:8311/keycloak", "keycloak", "password")) {

            JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

            String sql = "Select id, username, email From user_entity";

            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) -> {
                        User user = new User();
                        user.setId(rs.getString("id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        return user;
                    }

            );
//        } catch (SQLException e) {
//            e.printStackTrace();
        }

    public String getUsenameById(String idUser) {

        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setUrl(keycloakDbUrl);
        ds.setUsername(keycloakDbUsername);
        ds.setPassword(keycloakDbPassword);

        //try(Connection connection = DriverManager.getConnection("jdbc:postgresql://192.168.25.11:8300:8311/keycloak", "keycloak", "password")) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        String sql = "Select username From user_entity where id = ?";

        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{idUser},
                String.class);
    }

}
