CREATE SCHEMA IF NOT EXISTS school;

CREATE TABLE IF NOT EXISTS school.courses(
  id SERIAL PRIMARY KEY,
  name VARCHAR(25) NOT NULL,
  description TEXT
);

CREATE TABLE IF NOT EXISTS school.groups(
  id SERIAL PRIMARY KEY,
  name VARCHAR(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS school.students(
  id SERIAL PRIMARY KEY,
  firstname VARCHAR(20) NOT NULL,
  lastname VARCHAR(20) NOT NULL,
  group_id INTEGER REFERENCES school.groups(id),
  UNIQUE (firstname, lastname)
);

CREATE TABLE IF NOT EXISTS school.students_courses(
  student_id INTEGER REFERENCES school.students(id) ON DELETE CASCADE,
  course_id INTEGER REFERENCES school.courses(id) ON DELETE CASCADE,
  PRIMARY KEY (student_id, course_id)
);