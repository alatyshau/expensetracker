package com.toptal.expensetracker.web.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController
{
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String viewExpenseTracker()
	{
		return "expenseTracker";
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String viewWelcomePage()
	{
		return "welcome";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String viewAdminPage(final Model model, final HttpServletRequest request)
	{
		final Cookie[] cookies = request.getCookies();
		final StringBuilder cookiesString = new StringBuilder();
		if (cookies != null)
		{
			if (cookies.length == 0)
			{
				cookiesString.append("coockies.length = 0");
			}
			else
			{
				for (final Cookie cookie : cookies)
				{
					cookiesString.append(cookie.getDomain()).append('[').append(cookie.getName()).append("] = ")
							.append(cookie.getValue()).append("<br>");
				}
			}
		}
		else
		{
			cookiesString.append("no cookies");
		}
		model.addAttribute("cookiesString", cookiesString.toString());
		return "admin";
	}
}
