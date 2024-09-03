package com.foxminded.task21.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import com.foxminded.task21.entities.Student;
import java.util.List;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = StudentRepository.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = 
		{ "/sql/clear_tables.sql", "/sql/sample_data.sql" }, 
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class StudentRepositoryTest {

	@Autowired
	private StudentRepository studentRepository;

	@Test
	void testFindByFirstNameAndLastName() {
		Student student = studentRepository.findByFirstNameAndLastName("Michael", "Smith");
		Integer studentId = student.getId();
		assertEquals(102, studentId);
	}

	@Test
	void testFindAllByCourses_NameOrderBy_IdAsc() {
		String courseName = "history";
		List<Student> students = studentRepository.findAllByCoursesNameOrderByIdAsc(courseName);
		assertTrue(!students.isEmpty());
		assertEquals(5, students.size());
	}

	@Test
	void testFindFirstByOrderByIdAsc() {
		Integer expectedMinId = 100;
		Integer actualMinId = studentRepository.findFirstByOrderByIdAsc().getId();
		assertEquals(expectedMinId, actualMinId);
	}

	@Test
	void testFindFirstByOrderByIdDesc() {
		Integer expectedMaxId = 105;
		Integer actualMaxId = studentRepository.findFirstByOrderByIdDesc().getId();
		assertEquals(expectedMaxId, actualMaxId);
	}

	@Test
	void testExistsByFirstNameAndLastName() {
		String firstName = "David";
		String lastName = "Miller";

		boolean result = studentRepository.existsByFirstNameAndLastName(firstName, lastName);
		assertTrue(result);
	}

	@Test
	void testExistsByFirstNameAndLastNameNotExists() {
		String firstName = "John";
		String lastName = "Doe";

		boolean result = studentRepository.existsByFirstNameAndLastName(firstName, lastName);
		assertFalse(result);
	}

	@Test
	void testExistsByIdAndCourses_Id() {
		Integer studentId = 100;
		Integer courseId = 102;

		boolean result = studentRepository.existsByIdAndCourses_Id(studentId, courseId);
		assertTrue(result);
	}

	@Test
	void testExistsByIdAndCourses_IdNotExists() {
		Integer studentId = 100;
		Integer courseId = 101;

		boolean result = studentRepository.existsByIdAndCourses_Id(studentId, courseId);
		assertFalse(result);
	}
}