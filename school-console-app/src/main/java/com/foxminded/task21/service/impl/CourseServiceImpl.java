package com.foxminded.task21.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.foxminded.task21.entities.Course;
import com.foxminded.task21.exception.ServiceException;
import com.foxminded.task21.repository.CourseRepository;
import com.foxminded.task21.service.CourseService;

@Service
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

	private final CourseRepository courseRepository;

	public CourseServiceImpl(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@Override
	public String generateAllCourses() throws ServiceException {
		try {
			List<Course> courses = courseRepository.findAll();
			return courses.stream()
					.map(course -> String.format("%2d  %-10s", course.getId(), course.getName()))
					.collect(Collectors.joining("\n"));
		} catch (DataAccessException e) {
			throw new ServiceException("Failed to generate all courses ", e);
		}
	}
}