ALTER TABLE WEIGHINGS
    ADD COLUMN tare FLOAT;

UPDATE WEIGHINGS w
SET w.tare = (SELECT t.weight FROM TARES t WHERE t.id = w.tare_id);

ALTER TABLE WEIGHINGS
    DROP COLUMN tare_id;