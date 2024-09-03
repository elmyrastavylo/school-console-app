package com.foxminded.task21.dto;

import java.util.Objects;
import com.foxminded.task21.entities.Group;

public class GroupWithStudentCountDTO {
	
	private Group group;
	private Long studentCount;	
	
	public GroupWithStudentCountDTO(Group group, Long studentCount) {
		this.group = group;
		this.studentCount = studentCount;
	}

	public Group getGroup() {
		return group;
	}
	
	public void setGroup(Group group) {
		this.group = group;
	}
	
	public Long getStudentCount() {
		return studentCount;
	}
	
	public void setStudentCount(Long studentCount) {
		this.studentCount = studentCount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(group, studentCount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupWithStudentCountDTO other = (GroupWithStudentCountDTO) obj;
		return Objects.equals(group, other.group) && Objects.equals(studentCount, other.studentCount);
	}	
}