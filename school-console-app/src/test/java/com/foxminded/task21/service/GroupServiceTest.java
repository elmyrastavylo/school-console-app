package com.foxminded.task21.service;

import com.foxminded.task21.dto.GroupWithStudentCountDTO;
import com.foxminded.task21.entities.Group;
import com.foxminded.task21.exception.ServiceException;
import com.foxminded.task21.repository.GroupRepository;
import com.foxminded.task21.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.List;

@SpringBootTest(classes = GroupServiceImpl.class)
class GroupServiceTest {

	@MockBean
	private GroupRepository groupRepository;

	@Autowired
	private GroupService groupService;

	@Test
	void testGenerateGroupsByStudents() throws ServiceException {
		List<GroupWithStudentCountDTO> groups = List.of(
				new GroupWithStudentCountDTO(new Group(1, "AB-12"), 10L),
	            new GroupWithStudentCountDTO(new Group(2, "CD-34"), 15L),
	            new GroupWithStudentCountDTO(new Group(3, "FE-56"), 8L)
	    );

	    when(groupRepository.getGroupsMaxStudents(20)).thenReturn(groups);

	    List<String> expected = List.of(" FE-56          8", " AB-12         10", " CD-34         15");
	    List<String> result = groupService.generateGroupsByStudents(20);
	    assertEquals(expected, result);
	}
}