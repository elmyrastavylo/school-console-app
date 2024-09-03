package com.foxminded.task21.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.foxminded.task21.service.DataGenerationService;

@Component
public class DataInitialization implements ApplicationRunner {

	private final DataGenerationService dataGenerationService;

	public DataInitialization(DataGenerationService dataGenerationService) {
		this.dataGenerationService = dataGenerationService;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		dataGenerationService.generateData();
	}
}