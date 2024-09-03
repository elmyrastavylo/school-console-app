package com.foxminded.task21.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ConsoleMenu.class)
@ActiveProfiles("test")
class ConsoleMenuTest {

	@MockBean
	private SchoolConsole schoolConsole;

	@MockBean
	private MenuHandler menuHandler;

	@Captor
	private ArgumentCaptor<String> consoleCaptor;

	@Autowired
	private ConsoleMenu consoleMenu;

	@Test
	void testConsoleMenuOutput() {
		when(schoolConsole.nextInt()).thenReturn(1, 2, 3);
		consoleMenu.runConsole();

		verify(schoolConsole, atLeastOnce()).println(consoleCaptor.capture());

		List<String> capturedOutput = consoleCaptor.getAllValues();
		assertEquals("\n\n\tWelcome to the SQL-JDBC-School application:", capturedOutput.get(0));
		assertEquals(String.join("", Collections.nCopies(65, "-")), capturedOutput.get(1));
		assertEquals("1. Find all groups with less or equal students number", capturedOutput.get(2));
		assertEquals("2. Find all students related to the course with the given name", capturedOutput.get(3));
	}
}