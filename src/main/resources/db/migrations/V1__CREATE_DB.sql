CREATE TABLE USER (
                        ID BIGINT NOT NULL AUTO_INCREMENT,
                        FIRST_NAME VARCHAR(255) NOT NULL,
                        LAST_NAME VARCHAR(255) NOT NULL,
                        EMAIL VARCHAR(255) NOT NULL UNIQUE,
                        PASSWORD VARCHAR(255) NOT NULL,
                        ROLE VARCHAR(255) NOT NULL,
                        TITLE VARCHAR(255),
                        FACULTY VARCHAR(255),
                        FACULTY_NUMBER VARCHAR(255),
                        SPECIALIZATION VARCHAR(255),
                        STUDENT_GROUP VARCHAR(255),
                        STUDENT_STREAM VARCHAR(255),
                        ENABLED BOOLEAN,
                        PRIMARY KEY (ID)
);

CREATE TABLE ASSIGNMENT (
                            ID BIGINT NOT NULL AUTO_INCREMENT,
                            TITLE VARCHAR(255) NOT NULL,
                            DESCRIPTION TEXT NOT NULL,
                            CREATE_AT DATETIME NOT NULL,
                            MODIFIED_AT DATETIME NOT NULL,
                            USER_ID BIGINT,
                            PRIMARY KEY (ID),
                            FOREIGN KEY (USER_ID) REFERENCES USER(ID)
);

CREATE TABLE SPECIAL_FILES (
                               ASSIGNMENT_ID BIGINT NOT NULL,
                               SPECIAL_FILE VARCHAR(255) NOT NULL,
                               PRIMARY KEY (ASSIGNMENT_ID, SPECIAL_FILE),
                               FOREIGN KEY (ASSIGNMENT_ID) REFERENCES ASSIGNMENT(ID)
);

CREATE TABLE STUDENT_SUBMISSIONS (
                                     ID BIGINT NOT NULL AUTO_INCREMENT,
                                     FILE_NAME VARCHAR(255) NOT NULL,
                                     CONTENT LONGBLOB,
                                     FILES_PRESENT BOOLEAN NOT NULL,
                                     BUILD_PASSING BOOLEAN NOT NULL,
                                     PROBLEMS LONGBLOB,
                                     ASSIGNMENT_ID BIGINT NOT NULL,
                                     USER_ID BIGINT NOT NULL,
                                     PRIMARY KEY (ID),
                                     FOREIGN KEY (ASSIGNMENT_ID) REFERENCES ASSIGNMENT(ID),
                                     FOREIGN KEY (USER_ID) REFERENCES USER(ID)
);

CREATE TABLE FEEDBACK (
                          ID BIGINT NOT NULL AUTO_INCREMENT,
                          STUDENT_SUBMISSION_ID BIGINT NOT NULL,
                          COMMENT TEXT NOT NULL,
                          COMMENTER VARCHAR(255) NOT NULL,
                          CREATE_AT DATETIME NOT NULL,
                          PRIMARY KEY (ID),
                          FOREIGN KEY (STUDENT_SUBMISSION_ID) REFERENCES STUDENT_SUBMISSIONS(ID)
);

CREATE TABLE TOKEN (
                       ID BIGINT NOT NULL AUTO_INCREMENT,
                       TOKEN VARCHAR(255) NOT NULL UNIQUE,
                       TOKEN_TYPE VARCHAR(255) NOT NULL,
                       REVOKED BOOLEAN NOT NULL,
                       EXPIRED BOOLEAN NOT NULL,
                       USER_ID BIGINT,
                       PRIMARY KEY (ID),
                       FOREIGN KEY (USER_ID) REFERENCES USER(ID)
);

-- Foreign key for StudentSubmission
ALTER TABLE "USER" ADD CONSTRAINT FK_SUBMISSION_USER FOREIGN KEY (ID) REFERENCES STUDENT_SUBMISSIONS(USER_ID);

-- Foreign key for Assignment
ALTER TABLE "USER" ADD CONSTRAINT FK_ASSIGNMENT_USER FOREIGN KEY (ID) REFERENCES ASSIGNMENT(USER_ID);

-- Foreign key for Token
ALTER TABLE "USER" ADD CONSTRAINT FK_TOKEN_USER FOREIGN KEY (ID) REFERENCES TOKEN(USER_ID);