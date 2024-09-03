package com.foxminded.task21.service;

import com.foxminded.task21.entities.Course;
import com.foxminded.task21.exception.ServiceException;
import com.foxminded.task21.repository.CourseRepository;
import com.foxminded.task21.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.List;

@SpringBootTest(classes = CourseServiceImpl.class)
class CourseServiceTest {

	@MockBean
	private CourseRepository сourseRepository;
	
	@Autowired
	private CourseService courseService;

	@Test
	void testGenerateAllCourses() throws ServiceException {
		List<Course> courses = List.of(
			new Course(1, "biology"),
			new Course(2, "history"),
			new Course(3, "physics")
		);

		when(сourseRepository.findAll()).thenReturn(courses);
		
		String expected = " 1  biology   \n 2  history   \n 3  physics   ";
		String result = courseService.generateAllCourses();		
		assertEquals(expected, result);
	}
}