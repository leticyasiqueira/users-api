package com.lsiqueira.user.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/info")
public class InfoController {
	
	
	private InputStream is = getClass().getResourceAsStream("/app.properties");
	private Properties properties = new Properties();

	@PostConstruct
	private void init() throws FileNotFoundException, IOException {
		properties.load(is);
		
	}
	
	@GetMapping
	public Properties getInfo() {
		return properties;
	}


}
