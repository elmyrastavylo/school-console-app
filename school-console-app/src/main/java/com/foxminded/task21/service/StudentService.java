package com.foxminded.task21.service;

import java.util.List;
import java.util.Optional;
import com.foxminded.task21.entities.Student;
import com.foxminded.task21.exception.ServiceException;

public interface StudentService {

	String generateStudent(Optional<Student> student);

	String generateAllStudentsInColumns(int columns) throws ServiceException;

	String generateDeletedStudentById(Integer studentId) throws ServiceException;

	String generateAddedStudentByName(String firstName, String lastName) throws ServiceException;

	List<String> generateStudentsByCourseName(String courseName) throws ServiceException;

	String generateAddedStudentToCourse(Integer studentId, Integer courseId) throws ServiceException;
	
	String generateDeletedStudentFromCourse(Integer studentId, Integer courseId) throws ServiceException;
	
	String generateIdRangeMessage() throws ServiceException;
}