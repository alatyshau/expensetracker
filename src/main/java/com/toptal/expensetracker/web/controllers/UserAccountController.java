package com.toptal.expensetracker.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toptal.expensetracker.dto.UserDTO;
import com.toptal.expensetracker.services.UserAccountService;

@Controller
@RequestMapping("/api/user")
public class UserAccountController extends BaseRestController
{
	@Autowired
	private UserAccountService userAccountService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public UserDTO createUser(@RequestBody final UserDTO userDTO)
	{
		return this.userAccountService.createUser(ctx(), userDTO);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	@ResponseBody
	public UserDTO createNewUser(final UserDTO userDTO)
	{
		return this.userAccountService.createUser(ctx(), userDTO);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public UserDTO getCurrentUser()
	{
		return this.userAccountService.getCurrentUser(ctx());
	}
}
