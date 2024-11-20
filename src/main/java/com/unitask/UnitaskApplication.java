package com.unitask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication(scanBasePackages = "com.unitask")
public class UnitaskApplication {
	public static void main(String[] args) {
		// Configure the application to run in non-headless mode
		SpringApplicationBuilder builder = new SpringApplicationBuilder(UnitaskApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);

		// Launch the GUI on the EDT
		SwingUtilities.invokeLater(() -> {
			TradeAnalysisFrame frame = context.getBean(TradeAnalysisFrame.class);
			frame.setVisible(true);
		});
	}
}