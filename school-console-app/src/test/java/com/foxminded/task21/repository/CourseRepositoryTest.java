package com.foxminded.task21.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import com.foxminded.task21.entities.Course;
import java.util.List;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CourseRepository.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = 
		{ "/sql/clear_tables.sql", "/sql/sample_data.sql" }, 
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class CourseRepositoryTest {

	@Autowired
	private CourseRepository courseRepository;

	@Test
	void testCountByStudentsIsNotNull() {
		long expected = 11;
		long result = courseRepository.countByStudentsIsNotNull();
		assertEquals(expected, result);
	}
	
	@Test
	void testFindByStudentsId() {		
		List<Course> courses = courseRepository.findByStudentsId(100);
		assertNotNull(courses);
		assertEquals(2, courses.size());
		assertEquals("biology", courses.get(0).getName());
		assertEquals("psychology", courses.get(1).getName());
	}
}