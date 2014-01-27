package com.toptal.expensetracker.web.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toptal.expensetracker.web.dto.UserDTO;

@Controller
@RequestMapping("/api/user")
public class UserAccountController
{
	@Autowired
	private UserDetailsManager userDetailsManager;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public UserDTO createUser(final UserDTO userDTO)
	{
		final UserDetails existingUser = this.userDetailsManager.loadUserByUsername(userDTO.getEmail());
		if (existingUser != null)
		{
			throw new IllegalArgumentException("Such email has been already registered: " + userDTO.getEmail());
		}

		// TODO validate email
		// TODO validate password

		final List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("USER"));
		final User user = new User(userDTO.getEmail(), userDTO.getPassword(), authorities);
		this.userDetailsManager.createUser(user);

		userDTO.setPassword(null);
		return userDTO;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	@ResponseBody
	public UserDTO createNewUser(final UserDTO userDTO)
	{
		if (this.userDetailsManager.userExists(userDTO.getEmail()))
		{
			throw new IllegalArgumentException("Such email has been already registered: " + userDTO.getEmail());
		}

		// TODO validate email
		// TODO validate password

		final List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("USER"));
		final User user = new User(userDTO.getEmail(), userDTO.getPassword(), authorities);
		this.userDetailsManager.createUser(user);

		userDTO.setPassword(null);
		return userDTO;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public UserDTO getCurrentUser()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final UserDTO userDTO = new UserDTO(authentication.getName());
		return userDTO;
	}
}
