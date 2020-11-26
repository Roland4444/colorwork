CREATE TABLE TEMP_TARES (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    name VARCHAR(255),
    weight float,
    waybill_id INTEGER,
    CONSTRAINT temp_tares_pk PRIMARY KEY (id),
    CONSTRAINT waybill_id_tares_ref FOREIGN KEY (waybill_id) REFERENCES WAYBILLS(id)
);
