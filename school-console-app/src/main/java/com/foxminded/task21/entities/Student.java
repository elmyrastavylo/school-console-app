package com.foxminded.task21.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "firstname")
	private String firstName;
	
	@Column(name = "lastname")
	private String lastName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "students_courses",
			joinColumns = @JoinColumn(name = "student_id"),
			inverseJoinColumns = @JoinColumn(name = "course_id")
	)
	private Set<Course> courses = new HashSet<>();

	public Student() {
	}

	public Student(Integer id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.courses = new HashSet<>();
	}

	public Student(String firstName, String lastName, Group group) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.group = group;
	}

	public Student(Integer id, String firstName, String lastName, Group group) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.group = group;
	}

	public Student(Integer id, String firstName, String lastName, Integer courseId, String courseName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.courses = new HashSet<>();
		addCourse(courseId, courseName);
	}

	public void addCourse(Integer courseId, String courseName) {
		Course course = new Course(courseId, courseName);
		addCourse(course);
	}

	public void addCourse(Course course) {
		if (courses == null) {
			courses = new HashSet<>();
		}
		if (!courses.contains(course)) {
			courses.add(course);
			course.getStudents().add(this);
		}
	}

	public Course getCourseById(Integer courseId) {
		return courses
				.stream()
				.filter(course -> course.getId() == courseId)
				.findFirst()
				.orElse(null);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	@Override
	public int hashCode() {
		return Objects.hash(courses, firstName, group, id, lastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		return Objects.equals(courses, other.courses) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(group, other.group) && id == other.id && Objects.equals(lastName, other.lastName);
	}
}