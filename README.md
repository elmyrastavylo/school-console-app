# school-console-app

This project is a simple console-based application for managing students, groups and courses that was developed as part of a learning process.

**Service Layer**
- Added a generator service that will be called if database is empty:
     - Create 10 groups with randomly generated names. The name should contain 2 characters, hyphen, 2 numbers.
     - Create 10 courses (math, biology, etc.).
     - Create 200 students. Take 20 first names and 20 last names and randomly combine them to generate students.
     - Randomly assign students to the groups. Each group can contain from 10 to 30 students. It is possible that some groups are without students or students without groups.
     - Create the relation MANY-TO-MANY between the tables STUDENTS and COURSES. Randomly assign from 1 to 3 courses for each student.
     

- Implemented a console menu for easier interaction with the application:
     - Find all the groups with less or equal student count.
     - Find all the students related to the course with the given name.
     - Add a new student.
     - Delete student by STUDENT_ID.
     - Add a student to the course (from the list).
     - Remove the student from one of their courses.

## Technologies Used

- **Java 17 (Core, Collection API, Stream API)**
- **Spring Boot**
- **Hibernate**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway Migration**
- **Docker**
- **JUnit & Testcontainers**