package com.toptal.expensetracker.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toptal.expensetracker.common.UnauthorizedAccessException;
import com.toptal.expensetracker.dto.UserDTO;

@Controller
public class UserLoginController extends BaseRestController
{
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private LogoutHandler logoutHandler;

	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	@ResponseBody
	public UserDTO login(@RequestParam final String email, @RequestParam final String password,
			final HttpServletRequest request)
	{
		final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
		token.setDetails(new WebAuthenticationDetails(request));
		final Authentication authentication;
		try
		{
			authentication = this.authenticationManager.authenticate(token);
		}
		catch (final AuthenticationException e)
		{
			throw new UnauthorizedAccessException(e);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new UserDTO(email);
	}

	@RequestMapping(value = "/api/login", method = RequestMethod.GET)
	@ResponseBody
	public UserDTO loginTEMP(@RequestParam final String email, @RequestParam final String password,
			final HttpServletRequest request)
	{
		final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
		token.setDetails(new WebAuthenticationDetails(request));
		final Authentication authentication;
		try
		{
			authentication = this.authenticationManager.authenticate(token);
		}
		catch (final AuthenticationException e)
		{
			throw new UnauthorizedAccessException(e);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new UserDTO(email);
	}

	@RequestMapping(value = "/api/logout"/* , method = RequestMethod.POST */)
	@ResponseBody
	public void logout(final HttpServletRequest request, final HttpServletResponse response)
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		this.logoutHandler.logout(request, response, authentication);
	}
}
