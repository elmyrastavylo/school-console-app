package com.foxminded.task21.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;
import com.foxminded.task21.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

	@Meta(comment = "Counts the number of records in the 'students_courses' table at startup")
	long countByStudentsIsNotNull();

	List<Course> findByStudentsId(Integer studentId);
}