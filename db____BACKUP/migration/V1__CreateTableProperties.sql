CREATE TABLE PROPERTIES (
id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
name VARCHAR(250),
value VARCHAR (250))