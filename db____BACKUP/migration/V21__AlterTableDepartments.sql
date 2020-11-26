DROP TABLE departments;
CREATE TABLE departments (
     id INTEGER NOT NULL,
     name VARCHAR(255),
     alias VARCHAR(255),
     type VARCHAR(255),
     CONSTRAINT departments_pk PRIMARY KEY (id)
);