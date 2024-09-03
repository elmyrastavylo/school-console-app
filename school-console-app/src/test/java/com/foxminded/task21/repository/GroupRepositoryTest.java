package com.foxminded.task21.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import com.foxminded.task21.dto.GroupWithStudentCountDTO;
import java.util.Comparator;
import java.util.List;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GroupRepository.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = 
		{ "/sql/clear_tables.sql", "/sql/sample_data.sql" },
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class GroupRepositoryTest {

	@Autowired
	private GroupRepository groupRepository;

	@Test
	void testGetGroupsMaxStudents() {
		List<GroupWithStudentCountDTO> groups = groupRepository.getGroupsMaxStudents(3);
		assertFalse(groups.isEmpty());

		GroupWithStudentCountDTO groupWithMaxStudents = groups.stream()
				.max(Comparator.comparingLong(GroupWithStudentCountDTO::getStudentCount))
				.orElseThrow(() -> new AssertionError("No group found"));

		assertEquals("CD-02", groupWithMaxStudents.getGroup().getName());
		assertEquals(3L, groupWithMaxStudents.getStudentCount());
		assertEquals(3, groups.size());
	}
}