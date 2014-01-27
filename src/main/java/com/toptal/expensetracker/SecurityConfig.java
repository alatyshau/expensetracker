package com.toptal.expensetracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	// @Autowired
	// private DataSource dataSource;

	private UserDetailsManager userDetailsManager;

	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception
	{
		final InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer = auth
				.inMemoryAuthentication();
		configurer.withUser("user@gmail.com").password("password").roles("USER");
		configurer.withUser("admin@gmail.com").password("password").roles("USER", "ADMIN");

		this.userDetailsManager = configurer.getUserDetailsService();

		// auth.jdbcAuthentication()//
		// .dataSource(this.dataSource)//
		// .withDefaultSchema()//
		// .withUser("user").password("password").roles("USER").and()//
		// .withUser("admin").password("password").roles("USER", "ADMIN");
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
				//
				.antMatchers("/css/**", "/images/**", "/js/**", "/api/login", "/api/user/**", "/welcome", "/",
						"/com.toptal.expensetracker.gwt.ExpenseTracker/**").permitAll() //
				.antMatchers("/admin/**").hasRole("ADMIN") //
				.antMatchers("/api/**").authenticated() //
				.anyRequest().denyAll();

		http.headers().frameOptions().disable();
	}

	@Bean
	public LogoutHandler logoutHandler()
	{
		final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
		securityContextLogoutHandler.setClearAuthentication(true);
		securityContextLogoutHandler.setInvalidateHttpSession(true);
		return securityContextLogoutHandler;
	}

	@Bean
	public UserDetailsManager userDetailsManagerBean()
	{
		return this.userDetailsManager;
	}

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception
	{
		return super.userDetailsServiceBean();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}

}
