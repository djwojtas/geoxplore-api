CREATE SEQUENCE users_seq;
CREATE TABLE users (
  id BIGINT NOT NULL PRIMARY KEY,
  username VARCHAR(16) NOT NULL,
  password VARCHAR(60) NOT NULL,
  email VARCHAR(254) NOT NULL
);