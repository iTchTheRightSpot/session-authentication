CREATE TABLE EMPLOYEE (
                          employee_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          account_enable TINYINT(1) DEFAULT 0,
                          credentials_expired TINYINT(1) DEFAULT 1,
                          account_expired TINYINT(1) DEFAULT 1,
                          account_locked TINYINT(1) DEFAULT 1
);

CREATE TABLE ROLE (
                      role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      role VARCHAR(255) NOT NULL,
                      employee_id BIGINT NOT NULL,
                      CONSTRAINT role_employee_fk FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE
);