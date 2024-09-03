package com.foxminded.task21.service;

import com.foxminded.task21.service.impl.StudentServiceImpl;
import com.foxminded.task21.entities.Course;
import com.foxminded.task21.entities.Student;
import com.foxminded.task21.exception.ServiceException;
import com.foxminded.task21.repository.CourseRepository;
import com.foxminded.task21.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest(classes = StudentServiceImpl.class)
class StudentServiceTest {

	@MockBean
	private StudentRepository studentRepository;

	@MockBean
	private CourseRepository courseRepository;

	@Autowired
	private StudentService studentService;

	@Test
	void testGenerateAllStudentsInColumns() throws ServiceException {
		List<Student> students = List.of(
				new Student(1, "Amanda", "Anderson"), 
				new Student(2, "David", "Johnson"),
				new Student(3, "Robert", "Nelson")
		);
		when(studentRepository.findAll()).thenReturn(students);

		String expected = "  1  Amanda      Anderson            \r\n" + "  2  David       Johnson             \r\n"
				+ "  3  Robert      Nelson              \r\n";
		String result = studentService.generateAllStudentsInColumns(1);
		assertEquals(expected, result);
	}

	@Test
	void testGenerateDeletedStudentById() throws ServiceException {
		Student student = new Student(10, "John", "Doe");
		Optional<Student> optionalStudent = Optional.of(student);

		when(studentRepository.findById(10)).thenReturn(optionalStudent);

		String result = studentService.generateDeletedStudentById(10);
		String expected = String.format("%3d  %-12s%-20s", student.getId(), student.getFirstName(),
				student.getLastName());
		assertEquals(expected, result);
	}

	@Test
	void testGenerateAddedStudentByName_WhenStudentDoesNotExist() throws ServiceException {
		String firstName = "John";
		String lastName = "Doe";

		when(studentRepository.existsByFirstNameAndLastName(firstName, lastName)).thenReturn(false);

		Student student = new Student(10, firstName, lastName);

		when(studentRepository.save(any(Student.class))).thenReturn(student);
		when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

		String result = studentService.generateAddedStudentByName(firstName, lastName);
		String expected = String.format("%3d  %-12s%-20s", student.getId(), student.getFirstName(),
				student.getLastName());
		assertEquals(expected, result);
	}

	@Test
	void testGenerateAddedStudentByName_WhenStudentExists() throws ServiceException {
		String firstName = "John";
		String lastName = "Doe";

		when(studentRepository.existsByFirstNameAndLastName(firstName, lastName)).thenReturn(true);

		String result = studentService.generateAddedStudentByName(firstName, lastName);
		String expected = "Student with name:  John Doe  already exists in database.";
		assertEquals(expected, result);
	}

	@Test
	void testGenerateStudentsByCourseName() throws ServiceException {
		List<Student> students = List.of(new Student(11, "John", "Doe"), new Student(12, "Jane", "Smith"));
		String courseName = "history";

		when(studentRepository.findAllByCoursesNameOrderByIdAsc("history")).thenReturn(students);

		List<String> result = studentService.generateStudentsByCourseName("history");
		List<String> expected = students
				.stream().sorted(Comparator.comparing(Student::getId)).map(student -> String.format("%3d  %-15s%-13s%s",
						student.getId(), student.getFirstName(), student.getLastName(), courseName))
				.collect(Collectors.toList());
		assertEquals(expected, result);
	}

	@Test
	void testGenerateAddedStudentToCourse() throws ServiceException {
		Integer studentId = 100;
		Integer courseId = 10;

		Student student = new Student(studentId, "John", "Doe", courseId, "biology");
		Course course = new Course(courseId, "biology");

		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		when(studentRepository.existsByIdAndCourses_Id(studentId, courseId)).thenReturn(false);

		String result = studentService.generateAddedStudentToCourse(studentId, courseId);
		String expected = String.format("Student:  %d %s %s  has been added to the course:  %d %s", student.getId(),
				student.getFirstName(), student.getLastName(), course.getId(), course.getName());
		assertEquals(expected, result);
	}

	@Test
	void testGenerateAddedStudentToCourse_WhenStudentIdNotExists() throws ServiceException {
		Integer studentId = 100;
		Integer courseId = 10;

		Course course = new Course(courseId, "biology");

		when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

		String result = studentService.generateAddedStudentToCourse(studentId, courseId);
		String expected = "Student with ID=" + studentId + " not found in database";
		assertEquals(expected, result);
	}

	@Test
	void testGenerateAddedStudentToCourse_WhenCourseIdNotExists() throws ServiceException {
		Integer studentId = 100;
		Integer courseId = 10;

		Student student = new Student(studentId, "John", "Doe", courseId, "biology");

		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

		String result = studentService.generateAddedStudentToCourse(studentId, courseId);
		String expected = "Course with ID=" + courseId + " not found in database";
		assertEquals(expected, result);
	}

	@Test
	void testGenerateDeletedStudentFromCourse() throws ServiceException {
		Integer studentId = 100;
		Integer courseId = 10;

		Student student = new Student(studentId, "John", "Doe");
		Course course = new Course(courseId, "biology");

		when(studentRepository.existsById(studentId)).thenReturn(true);
		when(courseRepository.existsById(courseId)).thenReturn(true);
		when(studentRepository.existsByIdAndCourses_Id(studentId, courseId)).thenReturn(true);
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

		String result = studentService.generateDeletedStudentFromCourse(studentId, courseId);
		String expected = String.format("Student:  %d %s %s  has been deleted from the course with ID:  %d %s",
				student.getId(), student.getFirstName(), student.getLastName(), course.getId(), course.getName());
		assertEquals(expected, result);
	}

	@Test
	void testGenerateDeletedStudentFromCourse_WhenStudentIdNotExists() throws ServiceException {
		Integer studentId = 100;
		Integer courseId = 10;

		when(studentRepository.existsById(studentId)).thenReturn(false);
		when(courseRepository.existsById(courseId)).thenReturn(true);

		String result = studentService.generateDeletedStudentFromCourse(studentId, courseId);
		String expected = "Student with ID=" + studentId + " not found in database";
		assertEquals(expected, result);
	}

	@Test
	void testGenerateDeletedStudentFromCourse_WhenCourseIdNotExists() throws ServiceException {
		Integer studentId = 100;
		Integer courseId = 10;

		when(studentRepository.existsById(studentId)).thenReturn(true);
		when(courseRepository.existsById(courseId)).thenReturn(false);

		String result = studentService.generateDeletedStudentFromCourse(studentId, courseId);
		String expected = "Course with ID=" + courseId + " not found in database";
		assertEquals(expected, result);
	}
}