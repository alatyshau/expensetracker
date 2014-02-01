package com.toptal.expensetracker.services.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.toptal.expensetracker.common.AccessRole;
import com.toptal.expensetracker.common.ServiceContext;
import com.toptal.expensetracker.common.ValidationException;
import com.toptal.expensetracker.dto.UserDTO;
import com.toptal.expensetracker.services.UserAccountService;

@Service("userAccountService")
public class DefaultUserAccountService implements UserAccountService
{
	@Autowired
	private UserDetailsManager userDetailsManager;

	@Override
	public UserDTO createUser(final ServiceContext ctx, final UserDTO userDTO)
	{
		if (this.userDetailsManager.userExists(userDTO.getEmail()))
		{
			throw new ValidationException("Such email has been already registered: " + userDTO.getEmail());
			// throw new ValidationException("user.create.emailAlreadyExists");
		}

		// TODO validate email
		// TODO validate password

		final List<AccessRole> authorities = Arrays.asList(AccessRole.USER);
		final User user = new User(userDTO.getEmail(), userDTO.getPassword(), authorities);
		this.userDetailsManager.createUser(user);

		// TODO also create few expenses

		userDTO.setPassword(null);
		return userDTO;
	}

	@Override
	public UserDTO getCurrentUser(final ServiceContext ctx)
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final UserDTO userDTO = new UserDTO(authentication.getName());
		return userDTO;
	}
}
