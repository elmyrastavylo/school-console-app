package com.foxminded.task21.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ConsoleMenu {

	private final MenuHandler menuHandler;
	private final SchoolConsole console;

	public ConsoleMenu(MenuHandler menuHandler, SchoolConsole console) {
		this.menuHandler = menuHandler;
		this.console = console;
	}

	public void runConsole() {
		Map<Integer, Runnable> menu = createMenu();
		try (console) {
			while (true) {
				console.println("\n\n\tWelcome to the SQL-JDBC-School application:");
				console.println(String.join("", Collections.nCopies(65, "-")));
				printMenu();
				
				int choice = console.nextInt();
				if (menu.containsKey(choice)) {
					menu.get(choice).run();
				} else {
					console.println("Invalid selection. Please try again.");
				}
			}
		} catch (InputMismatchException e) {
			console.println("\nERROR: Invalid data entered!");
		} catch (Exception e) {
			console.println("\nError while performing the operation: " + e.getMessage());
		}
	}

	private Map<Integer, Runnable> createMenu() {
		Map<Integer, Runnable> menu = new HashMap<>();

		menu.put(1, () -> {
			int maxCount = getInputInt("\n\nEnter the maximum number of students: ");
			menuHandler.showGroupsUnderStudentCount(maxCount);
		});

		menu.put(2, () -> {
			menuHandler.showCourses();
			console.nextLine();
			String courseName = getInputString("\n\nEnter the course name: ");
			menuHandler.showStudentsByCourseName(courseName);
		});

		menu.put(3, () -> {
			console.nextLine();
			String firstName = getInputString("\n\nEnter the student's firstname: ");
			String lastname = getInputString("Enter the student's lastname:  ");
			menuHandler.showAddedStudentByName(firstName, lastname);
		});

		menu.put(4, () -> {
			menuHandler.showIdRangeMessage();
			int idStudent = getInputInt("Enter the student's ID: ");
			menuHandler.showDeletedStudentById(idStudent);
		});

		menu.put(5, () -> {
			menuHandler.showCourses();
			int idStudent = getInputInt("\n\nEnter the student's ID: ");
			int idCourse = getInputInt("Enter the course's ID: ");
			menuHandler.showAddedStudentToCourse(idStudent, idCourse);
		});

		menu.put(6, () -> {
			menuHandler.showCourses();
			int idStudent = getInputInt("\n\nEnter the student's ID: ");
			int idCourse = getInputInt("Enter the ID of the course you want to delete: ");
			menuHandler.showDeletedStudentFromCourse(idStudent, idCourse);
		});

		menu.put(7, () -> {
			int columns = 5;
			menuHandler.showAllStudents(columns);
		});

		menu.put(8, () -> {
			console.println("\nBye-bye!");
			System.exit(0);
		});
		return menu;
	}

	private void printMenu() {
		List<String> menuItems = Arrays.asList("Find all groups with less or equal students number",
				"Find all students related to the course with the given name", "Add a new student",
				"Delete a student by the STUDENT_ID", "Add a student to the course (from a list)",
				"Remove the student from one of their courses", "List of all students of the school", "Exit");

		for (int i = 0; i < menuItems.size(); i++) {
			console.println(i + 1 + ". " + menuItems.get(i));
		}
		console.println(String.join("", Collections.nCopies(65, "-")));
		console.print("Your choice: ");
	}

	private String getInputString(String message) {
		console.print(message);
		return console.nextLine().trim();
	}

	private int getInputInt(String message) {
		console.print(message);
		int input = console.nextInt();
		console.nextLine();
		return input;
	}	
}