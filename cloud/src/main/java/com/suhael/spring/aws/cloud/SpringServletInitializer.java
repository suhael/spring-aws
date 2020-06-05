package com.suhael.spring.aws.cloud;


import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * App Spring Boot Servlet Initializer. Required to generate and load a war instead of a jar.
 */
public class SpringServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(CloudApplication.class);
	}

}
