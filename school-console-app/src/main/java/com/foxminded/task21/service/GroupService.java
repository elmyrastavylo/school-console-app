package com.foxminded.task21.service;

import java.util.List;
import com.foxminded.task21.exception.ServiceException;

public interface GroupService {

	List<String> generateGroupsByStudents(int maxCount) throws ServiceException;
}