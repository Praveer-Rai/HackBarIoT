package com.nerdbar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class NerdBarApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(NerdBarApplication.class);

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NerdBarApplication.class);
    }


	public static void main(String[] args) {
		SpringApplication.run(NerdBarApplication.class, args);
	}
}
