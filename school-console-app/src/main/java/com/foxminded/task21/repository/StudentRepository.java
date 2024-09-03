package com.foxminded.task21.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.foxminded.task21.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	Student findByFirstNameAndLastName(String firstName, String lastName);

	@Query("""
			SELECT s FROM Student s
			LEFT JOIN s.courses c
			WHERE c.name = :courseName
			ORDER BY s.id ASC
			""")
	List<Student> findAllByCoursesNameOrderByIdAsc(@Param("courseName") String courseName);

	Student findFirstByOrderByIdAsc();

	Student findFirstByOrderByIdDesc();

	boolean existsByFirstNameAndLastName(String firstName, String lastName);

	boolean existsByIdAndCourses_Id(Integer studentId, Integer courseId);
}