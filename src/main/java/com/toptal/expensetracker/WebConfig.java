package com.toptal.expensetracker;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@ComponentScan("com.toptal.expensetracker.web")
@EnableWebMvc
@PropertySource("classpath:/web.properties")
public class WebConfig extends WebMvcConfigurerAdapter
{
	// @Override
	// public void addViewControllers(ViewControllerRegistry registry) {
	// super.addViewControllers(registry);
	// registry.addViewController("/login").setViewName("login");
	// registry.addViewController("/errors/404").setViewName("errors/404");
	// registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	// }

	@Autowired
	private Environment env;

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
		registry.addResourceHandler("/images/**").addResourceLocations("/images/");
		registry.addResourceHandler("/expenseTracker/**").addResourceLocations("/expenseTracker/");
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry)
	{
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Override
	public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer)
	{
		configurer.enable();
	}

	@Bean
	public MessageSource messageSource()
	{
		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("/WEB-INF/messages/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(this.env.getProperty("web.resourceBundle.cacheSeconds", Integer.class));

		return messageSource;
	}

	@Bean
	public LocaleResolver localeResolver()
	{
		final CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor()
	{
		final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean
	public ViewResolver viewResolver()
	{
		final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setCache(false);
		viewResolver.setRedirectHttp10Compatible(true);
		return viewResolver;
	}

	@Bean
	public MultipartResolver multipartResolver()
	{
		final CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(5 * 1024 * 1024);
		return multipartResolver;
	}

}
