package com.foxminded.task21.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.foxminded.task21.dto.GroupWithStudentCountDTO;
import com.foxminded.task21.exception.ServiceException;
import com.foxminded.task21.repository.GroupRepository;
import com.foxminded.task21.service.GroupService;

@Service
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;

	public GroupServiceImpl(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	@Override
	public List<String> generateGroupsByStudents(int maxCount) throws ServiceException {
		try {
			List<GroupWithStudentCountDTO> groups = groupRepository.getGroupsMaxStudents(maxCount);

			return groups.stream()
					.sorted((g1, g2) -> Long.compare(g1.getStudentCount(), g2.getStudentCount()))
					.map(group -> String.format(" %-9s%7d", group.getGroup().getName(), group.getStudentCount()))
					.collect(Collectors.toList());
		} catch (DataAccessException e) {
			throw new ServiceException("Failed to generate groups by students", e);
		}
	}
}