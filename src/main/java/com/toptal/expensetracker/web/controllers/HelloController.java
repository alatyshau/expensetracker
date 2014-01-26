package com.toptal.expensetracker.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController
{
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String viewIndexPage()
	{
		return "welcome";
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String viewWelcomePage()
	{
		return "welcome";
	}

	@RequestMapping(value = "/rest", method = RequestMethod.GET)
	@ResponseBody
	public String viewRestResponse()
	{
		return "Hello World";
	}
}
