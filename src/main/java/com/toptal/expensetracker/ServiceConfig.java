package com.toptal.expensetracker;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.toptal.expensetracker.services")
@EnableTransactionManagement
public class ServiceConfig
{
	@Bean
	public DataSource dataSource()
	{
		final BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		final String url = "jdbc:mysql://localhost:3307/expensetracker?useConfigs=maxPerformance&characterEncoding=utf8";
		dataSource.setUrl(url);
		dataSource.setUsername("root");
		dataSource.setPassword("admin");
		dataSource.setMaxActive(20);
		dataSource.setMaxIdle(10);
		dataSource.setMinIdle(0);
		dataSource.setInitialSize(20);
		dataSource.setRemoveAbandoned(true);
		dataSource.setLogAbandoned(true);
		dataSource.setValidationQuery("select 1");
		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate()
	{
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public PlatformTransactionManager txManager()
	{
		return new DataSourceTransactionManager(dataSource());
	}

}
