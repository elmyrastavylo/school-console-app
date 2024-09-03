INSERT INTO school.courses(id, name, description) VALUES 
  (100, 'biology', 'This course covers the basics of biology'),
  (101, 'history', 'This course covers the basics of history'),
  (102, 'psychology', 'This course covers the basics of psychology');

INSERT INTO school.groups(id, name) VALUES
  (100, 'AB-01'),
  (101, 'CD-02'),
  (102, 'EF-03');
  
INSERT INTO school.students(id, firstname, lastname, group_id) VALUES
  (100, 'Barbara', 'Taylor', 100),
  (101, 'David', 'Miller', 100),
  (102, 'Michael', 'Smith', 101),
  (103, 'Victoria', 'Davis', 101),
  (104, 'William', 'Brown', 101),
  (105, 'Anna', 'Nelson', 102);

INSERT INTO school.students_courses(student_id, course_id) VALUES
  (100, 100),
  (100, 102),
  (101, 101),
  (102, 101),
  (102, 102),
  (103, 100),
  (103, 101),
  (103, 102),
  (104, 101),
  (105, 100),
  (105, 101);