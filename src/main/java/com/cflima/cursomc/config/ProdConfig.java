package com.cflima.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cflima.cursomc.serivices.DBMockService;
import com.cflima.cursomc.serivices.EmailService;
import com.cflima.cursomc.serivices.SmtpEmailService;

@Configuration
@Profile("prod")
public class ProdConfig {
	
	@Autowired
	private DBMockService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategyDataBase;
	
	@Bean
	public boolean instatiateDataBase() throws ParseException {
		if(strategyDataBase.equalsIgnoreCase("create")) {
			dbService.instantiateTestDatabase();
		}
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}

}
