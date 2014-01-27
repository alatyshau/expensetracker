package com.toptal.expensetracker;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Order(1)
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException
	{
		super.onStartup(servletContext);
	}

	@Override
	protected Class<?>[] getRootConfigClasses()
	{
		return new Class[] { RootConfig.class, SecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses()
	{
		return new Class[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings()
	{
		return new String[] { "/" };
	}

	@Override
	protected Filter[] getServletFilters()
	{
		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return new Filter[] { characterEncodingFilter };
	}
}
