CREATE SCHEMA IF NOT EXISTS hw2;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  email VARCHAR(254) NOT NULL,
  name VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  CONSTRAINT PK_USER PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS locations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  radius FLOAT,
  lat FLOAT NOT NULL,
  lon FLOAT NOT NULL,
  CONSTRAINT PK_LOC PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  title VARCHAR(120) NOT NULL,
  description VARCHAR(7000) NOT NULL,
  event_date TIMESTAMP NOT NULL,
  initiator_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
  location_id BIGINT REFERENCES locations (id) ON DELETE SET NULL,
  CONSTRAINT PK_EV PRIMARY KEY (id)
);
