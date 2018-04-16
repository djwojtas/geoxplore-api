CREATE SEQUENCE home_locations_seq;
CREATE TABLE home_locations (
  id BIGINT NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id),
  longitude BIGINT NOT NULL,
  latitude BIGINT NOT NULL,
  date_added TIMESTAMP NOT NULL
);