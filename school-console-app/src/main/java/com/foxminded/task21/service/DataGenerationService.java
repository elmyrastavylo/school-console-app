package com.foxminded.task21.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.foxminded.task21.entities.Course;
import com.foxminded.task21.entities.Group;
import com.foxminded.task21.entities.Student;
import com.foxminded.task21.repository.CourseRepository;
import com.foxminded.task21.repository.GroupRepository;
import com.foxminded.task21.repository.StudentRepository;

@Service
public class DataGenerationService {

	private final CourseRepository courseRepository;
	private final GroupRepository groupRepository;
	private final StudentRepository studentRepository;

	static final int minCourses = 1;
	static final int maxCourses = 3;
	static final int totalStudents = 200;
	static final int totalGroups = 10;
	static final int minStudentsInGroup = 10;
	static final int maxStudentsInGroup = 30;
	Random random = new Random();
	
	public DataGenerationService(CourseRepository courseRepository, GroupRepository groupRepository, StudentRepository studentRepository) {
		this.courseRepository = courseRepository;
		this.groupRepository = groupRepository;
		this.studentRepository = studentRepository;
	}

	@Transactional
	public void generateData() {
		try {
			long coursesCount = courseRepository.count();
			if (coursesCount == 0) {
				Map<String, String> courses = generateCourses();
				
				courses.forEach((name, description) -> {
					Course course = new Course();
					course.setName(name);
					course.setDescription(description);
					courseRepository.save(course);
				});
			}

			long groupsCount = groupRepository.count();
			if (groupsCount == 0) {
				List<String> groups = generateGroups();
				
				for (String name : groups) {
					Group group = new Group(name);
					groupRepository.save(group);
				}
			}

			long studentsCount = studentRepository.count();
			if (studentsCount == 0) {
				List<Student> students = generateStudents();				
				studentRepository.saveAll(students);
			}

			long studentsToCoursesCount = courseRepository.countByStudentsIsNotNull();
			if (studentsToCoursesCount == 0) {
				Map<Student, Set<Course>> studentsCourses = generateStudentsToCourses();
				for (Map.Entry<Student, Set<Course>> studentCourses : studentsCourses.entrySet()) {
					
					Student student = studentCourses.getKey();
					Set<Course> courses = studentCourses.getValue();					
					for (Course course : courses) {
						student.addCourse(course);
					}
					studentRepository.save(student);
				}
			}
		} catch (DataAccessException e) {
		    throw new DataAccessResourceFailureException ("Failed to save data in the database", e);
		}
	}

	public Map<String, String> generateCourses() {
		Map<String, String> courses = new HashMap<>();

		courses.put("astronomy", "This course covers the basics of astronomy");
		courses.put("biology", "This course covers the basics of biology");
		courses.put("chemistry", "This course covers the basics of chemistry");
		courses.put("geography", "This course covers the basics of geography");
		courses.put("history", "This course covers the basics of history");
		courses.put("informatics", "This course covers the basics of informatics");
		courses.put("linguistics", "This course covers the basics of linguistics");
		courses.put("mathematics", "This course covers the basics of mathematics");
		courses.put("physics", "This course covers the basics of physics");
		courses.put("psychology", "This course covers the basics of psychology");
		return courses;
	}

	public List<String> generateGroups() {
		List<String> groupNames = IntStream.range(0, 10)
				.mapToObj(i -> String.format("%c%c-%d%d", getRandomLetter(), getRandomLetter(), 
						getRandomDigit(), getRandomDigit()))
				.collect(Collectors.toList());
		return groupNames;
	}

	private char getRandomLetter() {
		return (char) ('A' + random.nextInt(26));
	}

	private int getRandomDigit() {
		return random.nextInt(10);
	}

	@Transactional(readOnly = true)
	public Map<Student, Set<Course>> generateStudentsToCourses() {
		List<Student> students = studentRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		Map<Student, Set<Course>> result = new HashMap<>();

		for (Student student : students) {
			int range = random.nextInt(maxCourses - minCourses + 1) + minCourses;
			Set<Course> selectedCourses = getRandomCourses(courses, range);
			result.put(student, selectedCourses);
		}
		return result;
	}

	private Set<Course> getRandomCourses(List<Course> courses, int range) {
		Set<Course> selectedCourses = new HashSet<>();

		while (selectedCourses.size() < range) {
			Course course = courses.get(random.nextInt(courses.size()));
			selectedCourses.add(course);
		}
		return selectedCourses;
	}

	public List<Student> generateStudents() {
		Set<String> uniqueNames = generateUniqueNames();
		List<Student> students = addGroupIdsToStudents(uniqueNames);
		return students;
	}

	private Set<String> generateUniqueNames() {
		Set<String> uniqueNames = new HashSet<>();
		List<String> firstNames = readFileAndConvertToList("firstname.txt");
		List<String> lastNames = readFileAndConvertToList("lastname.txt");

		while (uniqueNames.size() < totalStudents) {
			String firstName = firstNames.get(random.nextInt(firstNames.size()));
			String lastName = lastNames.get(random.nextInt(lastNames.size()));
			uniqueNames.add(firstName + "," + lastName);
		}
		return uniqueNames;
	}

	@Transactional(readOnly = true)
	private List<Student> addGroupIdsToStudents(Set<String> uniqueNames) {
		List<Student> students = new ArrayList<>();
		List<Group> groups = groupRepository.findAll();

		int groupIndex = 0;
		int studentIndex = 0;

		while (studentIndex < uniqueNames.size()) {
			int range = random.nextInt(maxStudentsInGroup - minStudentsInGroup + 1) + minStudentsInGroup;
			if (studentIndex + range > uniqueNames.size()) {
				range = uniqueNames.size() - studentIndex;
			}

			for (int i = studentIndex; i < studentIndex + range; i++) {
				String name = (String) uniqueNames.toArray()[i];
				String firstName = name.split(",")[0];
				String lastName = name.split(",")[1];
				Group group = (range >= minStudentsInGroup && groupIndex < totalGroups) ? groups.get(groupIndex) : null;
				Student student = new Student(firstName, lastName, group);
				students.add(student);
			}
			studentIndex += range;
			groupIndex++;
		}
		return students;
	}

	private List<String> readFileAndConvertToList(String fileName) {
		try {
			String result = readFile(fileName);
			List<String> names = Arrays.asList(result.split("\n"));
			return names;
		} catch (IOException e) {
			System.err.println("Error reading file " + fileName + ": " + e.getMessage());
			throw new RuntimeException();
		}
	}

	public String readFile(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		try (InputStream inputStream = classLoader.getResourceAsStream(fileName);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			return reader.lines().collect(Collectors.joining("\n"));
		}
	}
}