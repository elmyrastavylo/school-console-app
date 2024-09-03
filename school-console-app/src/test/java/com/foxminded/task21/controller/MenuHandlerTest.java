package com.foxminded.task21.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.foxminded.task21.entities.Course;
import com.foxminded.task21.entities.Group;
import com.foxminded.task21.entities.Student;
import com.foxminded.task21.exception.ServiceException;
import com.foxminded.task21.service.CourseService;
import com.foxminded.task21.service.GroupService;
import com.foxminded.task21.service.StudentService;

@SpringBootTest(classes = MenuHandler.class)
class MenuHandlerTest {

	@MockBean
	private SchoolConsole schoolConsole;

	@MockBean
	private StudentService studentService;

	@MockBean
	private GroupService groupService;

	@MockBean
	private CourseService courseService;

	@Autowired
	private MenuHandler menuHandler;

	@Captor
	private ArgumentCaptor<String> consoleCaptor;

	@Test
	void testShowGroupsUnderStudentCount() throws ServiceException {
		int maxCount = 15;
		Map<Group, Long> groups = Map.of(
				new Group(1, "AB-12"), 10L, 
				new Group(2, "CD-34"), 20L, 
				new Group(3, "FE-56"), 8L
		);
		List<String> groupsList = groups.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
				.map(group -> String.format(" %-9s%7d", group.getKey().getName(), group.getValue()))
				.collect(Collectors.toList());

		when(groupService.generateGroupsByStudents(maxCount)).thenReturn(groupsList);
		menuHandler.showGroupsUnderStudentCount(maxCount);

		verify(schoolConsole, times(6)).println(consoleCaptor.capture());

		List<String> capturedOutput = consoleCaptor.getAllValues();
		assertEquals("\n Groups     Students", capturedOutput.get(0));
		assertEquals(" FE-56          8", capturedOutput.get(2));
		assertEquals(" AB-12         10", capturedOutput.get(3));
	}

	@Test
	void testShowCourses() throws ServiceException {
		List<Course> courses = List.of(
				new Course(10, "biology"), 
				new Course(11, "history"), 
				new Course(12, "physics")
		);
		String expectedOutput = courses.stream()
				.map(course -> String.format("%2d  %-10s", course.getId(), course.getName()))
				.collect(Collectors.joining("\n"));

		when(courseService.generateAllCourses()).thenReturn(expectedOutput);
		menuHandler.showCourses();

		verify(schoolConsole, times(2)).println(consoleCaptor.capture());

		List<String> capturedOutput = consoleCaptor.getAllValues();
		assertEquals("\n\nCatalog of school courses:\n", capturedOutput.get(0));
		assertEquals("10  biology   \n11  history   \n12  physics   ", capturedOutput.get(1));
	}

	@Test
	void testShowAddedStudentToCourse() throws ServiceException {
		Integer studentId = 100;
		Integer courseId = 10;

		Student student = new Student(studentId, "John", "Doe", courseId, "biology");
		Course course = new Course(courseId, "biology");

		String expectedOutput = String.format("Student:  %d %s %s  has been added to the course:  %d %s",
				student.getId(), student.getFirstName(), student.getLastName(), course.getId(), course.getName());

		when(studentService.generateAddedStudentToCourse(studentId, courseId)).thenReturn(expectedOutput);
		menuHandler.showAddedStudentToCourse(studentId, courseId);

		verify(schoolConsole, times(1)).println(consoleCaptor.capture());
		assertEquals("Student:  100 John Doe  has been added to the course:  10 biology", consoleCaptor.getValue());
	}

	@Test
	void testShowDeletedStudentFromCourse() throws ServiceException {
		Integer studentId = 100;
		Integer courseId = 10;

		Student student = new Student(studentId, "John", "Doe");
		Course course = new Course(courseId, "biology");

		String expectedOutput = String.format("Student:  %d %s %s  has been deleted from the course with ID:  %d %s",
				student.getId(), student.getFirstName(), student.getLastName(), course.getId(), course.getName());

		when(studentService.generateDeletedStudentFromCourse(studentId, courseId)).thenReturn(expectedOutput);
		menuHandler.showDeletedStudentFromCourse(studentId, courseId);

		verify(schoolConsole, times(1)).println(consoleCaptor.capture());
		assertEquals("Student:  100 John Doe  has been deleted from the course with ID:  10 biology",
				consoleCaptor.getValue());
	}

	@Test
	void testShowStudentsByCourseName() throws ServiceException {
		String courseName = "history";
		Map<Student, String> students = Map.of(
				new Student(100, "John", "Doe"), "history",
				new Student(101, "Jane", "Smith"), "history");

		List<String> studentsList = students.entrySet().stream()
				.sorted(Map.Entry.comparingByKey((s1, s2) -> Integer.compare(s1.getId(), s2.getId())))
				.map(student -> String.format("%3d  %-15s%-13s%s", student.getKey().getId(),
						student.getKey().getFirstName(), student.getKey().getLastName(), student.getValue()))
				.collect(Collectors.toList());

		when(studentService.generateStudentsByCourseName(courseName)).thenReturn(studentsList);
		menuHandler.showStudentsByCourseName(courseName);

		verify(schoolConsole, times(6)).println(consoleCaptor.capture());

		List<String> capturedOutput = consoleCaptor.getAllValues();
		assertEquals("\n\t\tStudents\t\tCourse", capturedOutput.get(0));
		assertEquals("     id\t  firstname\tlastname\t name", capturedOutput.get(1));
		assertEquals("100  John           Doe          history", capturedOutput.get(3));
		assertEquals("101  Jane           Smith        history", capturedOutput.get(4));
	}

	@Test
	void testShowAllStudents() throws ServiceException {
		int columns = 2;
		List<Student> students = List.of(
				new Student(100, "Amanda", "Anderson"), 
				new Student(101, "David", "Johnson"),
				new Student(102, "Robert", "Nelson"), 
				new Student(103, "Frank", "Baker")
		);

		int rows = (int) Math.ceil((double) students.size() / columns);
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				int index = i + j * rows;
				if (index < students.size()) {
					Optional<Student> student = Optional.ofNullable(students.get(index));
					result.append(String.format("%3d  %-12s%-20s", student.get().getId(), student.get().getFirstName(),
							student.get().getLastName()));
				}
			}
			result.append(System.lineSeparator());
		}
		String expectedOutput = result.toString();

		when(studentService.generateAllStudentsInColumns(columns)).thenReturn(expectedOutput);
		menuHandler.showAllStudents(columns);

		verify(schoolConsole, times(2)).println(consoleCaptor.capture());
		List<String> capturedOutput = consoleCaptor.getAllValues();
		assertEquals("\n\nList of school students:\n", capturedOutput.get(0));
		assertEquals(
				"100  Amanda      Anderson            102  Robert      Nelson              \r\n"
						+ "101  David       Johnson             103  Frank       Baker               \r\n",
				capturedOutput.get(1));
	}

	@Test
	void testShowDeletedStudentById_WhenStudentFound() throws ServiceException {
		Integer studentId = 100;
		String studentName = "John Doe";

		when(studentService.generateDeletedStudentById(studentId)).thenReturn(studentName);
		menuHandler.showDeletedStudentById(studentId);

		verify(schoolConsole, times(1)).println(consoleCaptor.capture());
		assertEquals("\nStudent " + studentName.trim() + " deleted successfully", consoleCaptor.getValue());
	}

	@Test
	void testShowDeletedStudentById_WhenStudentNotFound() throws ServiceException {
		Integer studentId = 100;
		String studentName = "";

		when(studentService.generateDeletedStudentById(studentId)).thenReturn(studentName);
		menuHandler.showDeletedStudentById(studentId);

		verify(schoolConsole, times(1)).println(consoleCaptor.capture());
		assertEquals("\nStudent with ID=" + studentId + " was not found", consoleCaptor.getValue());
	}

	@Test
	void testShowAddedStudentByName() throws ServiceException {
		String firstName = "John";
		String lastName = "Doe";
		Student student = new Student(300, firstName, lastName);

		String addedStudent = String.format("%3d  %-12s%-20s", student.getId(), student.getFirstName(),
				student.getLastName());

		when(studentService.generateAddedStudentByName(firstName, lastName)).thenReturn(addedStudent);
		menuHandler.showAddedStudentByName(firstName, lastName);

		verify(schoolConsole, times(1)).println(consoleCaptor.capture());
		assertEquals("\nStudent added successfully:\t" + addedStudent, consoleCaptor.getValue());
	}
}