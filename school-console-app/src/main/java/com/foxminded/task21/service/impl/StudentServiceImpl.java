package com.foxminded.task21.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.foxminded.task21.entities.Course;
import com.foxminded.task21.entities.Student;
import com.foxminded.task21.exception.ServiceException;
import com.foxminded.task21.repository.CourseRepository;
import com.foxminded.task21.repository.StudentRepository;
import com.foxminded.task21.service.StudentService;

@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;

	public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public String generateStudent(Optional<Student> student) {
		return student.map(s -> String.format("%3d  %-12s%-20s", student.get().getId(), student.get().getFirstName(),
				student.get().getLastName())).orElse("");
	}

	@Override
	public String generateAllStudentsInColumns(int columns) throws ServiceException {
		try {
			List<Student> students = studentRepository.findAll();
			if (students.isEmpty()) {
				return "";
			}
			int rows = (int) Math.ceil((double) students.size() / columns);
			StringBuilder result = new StringBuilder();

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					int index = i + j * rows;
					if (index < students.size()) {
						Optional<Student> student = Optional.ofNullable(students.get(index));
						result.append(generateStudent(student));
					}
				}
				result.append(System.lineSeparator());
			}
			return result.toString();
		} catch (DataAccessException e) {
			throw new ServiceException("Failed to generate all students in " + columns + " columns", e);
		}
	}

	@Override
	@Transactional
	public String generateDeletedStudentById(Integer studentId) throws ServiceException {
		try {
			return studentRepository.findById(studentId).map(student -> {
				studentRepository.delete(student);
				return generateStudent(Optional.of(student));
			}).orElse("");
		} catch (DataAccessException e) {
			throw new ServiceException("Failed to generate deleted student by ID= " + studentId, e);
		}
	}

	@Override
	@Transactional
	public String generateAddedStudentByName(String firstName, String lastName) throws ServiceException {
		try {
			if (studentRepository.existsByFirstNameAndLastName(firstName, lastName)) {
				return "Student with name:  " + firstName + " " + lastName + "  already exists in database.";
			}

			Student newStudent = new Student();
			newStudent.setFirstName(firstName);
			newStudent.setLastName(lastName);
			newStudent = studentRepository.save(newStudent);

			Optional<Student> student = studentRepository.findById(newStudent.getId());
			return generateStudent(student);
		} catch (DataAccessException e) {
			throw new ServiceException("Failed to generate new student by name= " + firstName + " " + lastName, e);
		}
	}

	@Override
	public List<String> generateStudentsByCourseName(String courseName) throws ServiceException {
		try {
			List<Student> students = studentRepository.findAllByCoursesNameOrderByIdAsc(courseName);

			return students.stream().sorted(Comparator.comparing(Student::getId))
					.map(student -> String.format("%3d  %-15s%-13s%s", student.getId(), student.getFirstName(),
							student.getLastName(), courseName))
					.collect(Collectors.toList());
		} catch (DataAccessException e) {
			throw new ServiceException("Failed to generate students by course name= " + courseName, e);
		}
	}

	@Override
	@Transactional
	public String generateAddedStudentToCourse(Integer studentId, Integer courseId) throws ServiceException {
		try {
			Optional<Student> studentOptional = studentRepository.findById(studentId);
			if (studentOptional.isEmpty()) {
				return "Student with ID=" + studentId + " not found in database";
			}

			Optional<Course> courseOptional = courseRepository.findById(courseId);
			if (courseOptional.isEmpty()) {
				return "Course with ID=" + courseId + " not found in database";
			}

			if (studentRepository.existsByIdAndCourses_Id(studentId, courseId)) {
				return "Student with ID=" + studentId + " is already enrolled in the course with ID=" + courseId;
			}

			Student student = studentOptional.get();
			Course course = courseOptional.get();
			student.getCourses().add(course);
			studentRepository.save(student);

			return String.format("Student:  %d %s %s  has been added to the course:  %d %s", student.getId(),
					student.getFirstName(), student.getLastName(), course.getId(), course.getName());
		} catch (DataAccessException e) {
			throw new ServiceException(
					"Failed to generate student with ID= " + studentId + " and courseID= " + courseId, e);
		}
	}

	@Override
	@Transactional
	public String generateDeletedStudentFromCourse(Integer studentId, Integer courseId) throws ServiceException {
		try {
			if (!studentRepository.existsById(studentId)) {
				return "Student with ID=" + studentId + " not found in database";
			}

			if (!courseRepository.existsById(courseId)) {
				return "Course with ID=" + courseId + " not found in database";
			}

			if (!studentRepository.existsByIdAndCourses_Id(studentId, courseId)) {
				return "Student with ID=" + studentId + " didn't register for the course with ID=" + courseId;
			}

			Optional<Student> studentOptional = studentRepository.findById(studentId);
			if (studentOptional.isPresent()) {
				Student student = studentOptional.get();
				student.getCourses().removeIf(course -> course.getId().equals(courseId));
				studentRepository.save(student);

				Optional<Course> courseOptional = courseRepository.findById(courseId);
				if (courseOptional.isPresent()) {
					Course course = courseOptional.get();
					return String.format("Student:  %d %s %s  has been deleted from the course with ID:  %d %s",
							student.getId(), student.getFirstName(), student.getLastName(), course.getId(),
							course.getName());
				} else {
					return "Failed to find the course with ID=" + courseId;
				}
			} else {
				return "Failed to find the student with ID=" + studentId;
			}
		} catch (DataAccessException e) {
			throw new ServiceException(
					"Failed to generate student with ID= " + studentId + " and courseID= " + courseId, e);
		}
	}

	@Override
	public String generateIdRangeMessage() throws ServiceException {
		try {
			Integer minStudentId = studentRepository.findFirstByOrderByIdAsc().getId();
			Integer maxStudentId = studentRepository.findFirstByOrderByIdDesc().getId();
			String message = "\n\nEnter the student's id between " + minStudentId + " and " + maxStudentId + ": ";
			return message;
		} catch (DataAccessException e) {
			throw new ServiceException("Failed to generate the minimum and maximum values of STUDENT_ID ", e);
		}
	}
}