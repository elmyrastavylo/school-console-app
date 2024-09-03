package com.foxminded.task21.init;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.foxminded.task21.controller.ConsoleMenu;

@Component
@Profile("!test")
public class MenuInitialization implements ApplicationListener<ApplicationReadyEvent> {

	private final ConsoleMenu consoleMenu;

	public MenuInitialization(ConsoleMenu consoleMenu) {
		this.consoleMenu = consoleMenu;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		consoleMenu.runConsole();
	}
}