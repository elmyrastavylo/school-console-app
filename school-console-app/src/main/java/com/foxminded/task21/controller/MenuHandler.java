package com.foxminded.task21.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Controller;
import com.foxminded.task21.exception.ServiceException;
import com.foxminded.task21.service.CourseService;
import com.foxminded.task21.service.GroupService;
import com.foxminded.task21.service.StudentService;

@Controller
public class MenuHandler {

	private final StudentService studentService;
	private final GroupService groupService;
	private final CourseService courseService;
	private final SchoolConsole console;

	public MenuHandler(StudentService studentService, GroupService groupService, CourseService courseService,
			SchoolConsole console) {
		this.studentService = studentService;
		this.groupService = groupService;
		this.courseService = courseService;
		this.console = console;
	}
		
	public void showGroupsUnderStudentCount(int maxCount) {
		try {
			List<String> groups = groupService.generateGroupsByStudents(maxCount);
			console.println("\n Groups     Students");
			console.println(String.join("", Collections.nCopies(21, "-")));
			for (String group : groups) {
				console.println(group);
			}
			console.println(String.join("", Collections.nCopies(21, "-")) + "\n");
		} catch (ServiceException e) {
			console.printlnErr("Error preparing groups with the maximum specified number of students for console output: "
							+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void showCourses() {
		try {
			String courses = courseService.generateAllCourses();
			console.println("\n\nCatalog of school courses:\n");
			console.println(courses);
		} catch (ServiceException e) {
			console.printlnErr("Error preparing all courses: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void showAddedStudentToCourse(Integer studentId, Integer courseId) {
		try {
			String student = studentService.generateAddedStudentToCourse(studentId, courseId);
			console.println(student);
		} catch (ServiceException e) {
			console.printlnErr("Error preparing student with ID= " + studentId + " and courseID= " + courseId
					+ " for console output: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void showDeletedStudentFromCourse(Integer studentId, Integer courseId) {
		try {
			String student = studentService.generateDeletedStudentFromCourse(studentId, courseId);
			console.println(student);
		} catch (ServiceException e) {
			console.printlnErr("Error preparing student with ID= " + studentId + " and courseID= " + courseId
					+ " for console output: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void showStudentsByCourseName(String courseName) {
		try {
			List<String> students = studentService.generateStudentsByCourseName(courseName);
			console.println("\n\t\tStudents\t\tCourse");
			console.println("     id\t  firstname\tlastname\t name");
			console.println(String.join("", Collections.nCopies(51, "-")));
			int i = 1;
			for (String student : students) {
				console.printf("%2d)  ", i);
				console.println(student);
				i++;
			}
			console.println(String.join("", Collections.nCopies(51, "-")) + "\n");
		} catch (ServiceException e) {
			console.printlnErr("Error preparing students associated with the course= " + courseName
					+ " for console output: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void showAllStudents(int columns) {
		try {
			String student = studentService.generateAllStudentsInColumns(columns);
			console.println("\n\nList of school students:\n");
			console.println(student);
		} catch (ServiceException e) {
			console.printlnErr(
					"Error preparing all students in " + columns + " columns for console output: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void showDeletedStudentById(Integer studentId) {
		try {
			String deletedStudent = studentService.generateDeletedStudentById(studentId);

			if (deletedStudent.isEmpty()) {
				console.println("\nStudent with ID=" + studentId + " was not found");				
			} else {
				console.println("\nStudent " + deletedStudent.trim() + " deleted successfully");
			}
		} catch (ServiceException e) {
			console.printlnErr(
					"Error preparing student with ID= " + studentId + " for console output: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void showAddedStudentByName(String firstName, String lastName) {
		try {
			String addedStudent = studentService.generateAddedStudentByName(firstName, lastName);
			if (addedStudent.startsWith("Student with name:")) {
				console.println("\n" + addedStudent);
			} else {
				console.println("\nStudent added successfully:\t" + addedStudent);
			}
		} catch (ServiceException e) {
			console.printlnErr("Error preparing student with name= " + firstName + " " + lastName
					+ " for console output: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void showIdRangeMessage() {
		try {
			String rangeMessage = studentService.generateIdRangeMessage();
			console.println(rangeMessage);
		} catch (ServiceException e) {
			console.printlnErr("Error preparing the minimum and maximum values of STUDENT_ID for console output: "
					+ e.getMessage());
			e.printStackTrace();
		}
	}
}