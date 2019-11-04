-- DROP TABLE IF EXISTS survey;
-- DROP TABLE IF EXISTS response;
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
 
CREATE TABLE survey (
    survey_id Bigserial PRIMARY KEY NOT NULL,
    createdBy varchar(36),
    lastmodified timestamp,
    colorHeader varchar(100),
    colorBg varchar(100),
    imgHeader varchar(100),
    imgLogo varchar(100),
    icon varchar(100),
    privatestatus BOOLEAN ,
    activestatus BOOLEAN ,
    publishstatus BOOLEAN,
    pages jsonb
);

CREATE TABLE response (
    response_id Bigserial PRIMARY KEY NOT NULL,
    response_survey_id INTEGER NOT NULL,
    response_page_id INTEGER NOT NULL,
    question_id INTEGER NOT NULL,
    questionType varchar (100),
    answer varchar,
    dateanswer timestamp,
    optionanswer jsonb,
    response_user_id varchar(36)
);

CREATE TABLE response_temp (
    response_temp_id Bigserial PRIMARY KEY NOT NULL,
    response_temp_survey_id INTEGER NOT NULL,
    response_temp_page_id INTEGER NOT NULL,
    question_temp_id INTEGER NOT NULL,
    questionType varchar (100),
    answer varchar,
    dateanswer timestamp,
    optionanswer jsonb,
    response_temp_user_id varchar(36)
);

CREATE TABLE user_survey (
   user_survey_id Bigserial PRIMARY KEY NOT NULL,
   user_id varchar(36),
   survey_id INTEGER NOT NULL,
   submitresponse BOOLEAN,
   dismissStatus BOOLEAN,
 lastmodifiedAssign TIMESTAMP

);

-- INSERT INTO response(response_survey_id, response_page_id, question_id, answer)
-- VALUES(1, 0, 0, 'WIDA');
-- INSERT INTO response(response_survey_id, response_page_id, question_id, answer)
-- VALUES(1, 1, 0, 'EMERIO');
-- INSERT INTO response(response_survey_id, response_page_id, question_id, answer)
-- VALUES(1, 1, 1, 'BENHILL');
-- INSERT INTO response(response_survey_id, response_page_id, question_id, answer)
-- VALUES(2, 0, 0, 'WIDA');

--CREATE TABLE logo(
--    logo_id Bigserial PRIMARY KEY NOT NULL,
--    image BYTEA
--);

ALTER TABLE response
  ADD CONSTRAINT response_survey_fkey FOREIGN KEY (response_survey_id)
      REFERENCES survey (survey_id)
      ON UPDATE CASCADE ON DELETE CASCADE;

      ALTER TABLE response_temp
        ADD CONSTRAINT response_temp_survey_fkey FOREIGN KEY (response_temp_survey_id)
            REFERENCES survey (survey_id)
            ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE user_survey
 ADD CONSTRAINT survey_id_fkey FOREIGN KEY (survey_id)
     REFERENCES survey (survey_id)
     ON UPDATE CASCADE ON DELETE CASCADE;

