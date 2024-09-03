package com.foxminded.task21.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.foxminded.task21.dto.GroupWithStudentCountDTO;
import com.foxminded.task21.entities.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {

	@Query("""
			SELECT NEW com.foxminded.task21.dto.GroupWithStudentCountDTO(g, COUNT(s))
			FROM Group g LEFT JOIN g.students s 
			GROUP BY g 
			HAVING COUNT(s) <= :number
			""")
	List<GroupWithStudentCountDTO> getGroupsMaxStudents(@Param("number") int maxCount);
}