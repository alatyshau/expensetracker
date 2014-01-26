package com.toptal.expensetracker.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toptal.expensetracker.web.dto.UserDTO;

@Controller
public class UserLoginController
{
	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	@ResponseBody
	public UserDTO login(@RequestParam final String email, @RequestParam final String password,
			final HttpServletRequest request)
	{
		final UserDTO user = UserDTO.sample(email);
		request.getSession().setAttribute("user", user);
		return user;
	}

	@RequestMapping(value = "/api/login", method = RequestMethod.GET)
	@ResponseBody
	public UserDTO loginGET_TEMP(@RequestParam final String email, @RequestParam final String password,
			final HttpServletRequest request)
	{
		final UserDTO user = UserDTO.sample(email);
		request.getSession().setAttribute("user", user);
		return user;
	}

}
