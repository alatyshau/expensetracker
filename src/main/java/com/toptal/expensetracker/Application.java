package com.toptal.expensetracker;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/** Bootstraps the application by launching embedded web server. */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc
public class Application extends SpringBootServletInitializer
{
	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application)
	{
		return application.sources(Application.class);
	}

	public static void main(final String[] args)
	{
		final ApplicationContext ctx = SpringApplication.run(Application.class, args);

		System.out.println("Beans provided by Spring Boot:");

		final String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (final String beanName : beanNames)
		{
			System.out.println(" - " + beanName);
		}
	}

}
