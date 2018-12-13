-- CREATE SEQUENCE titles_seq START 2;
-- CREATE TABLE titles
-- (
--   id BIGINT NOT NULL PRIMARY KEY,
--   title VARCHAR(32) NOT NULL
-- );
-- INSERT INTO titles VALUES(1, '');

-- ALTER TABLE users ADD title_id BIGINT DEFAULT 1 NOT NULL REFERENCES titles(id);

-- todo

ALTER TABLE users ADD title VARCHAR(32) DEFAULT 'Początkujący' NOT NULL;
