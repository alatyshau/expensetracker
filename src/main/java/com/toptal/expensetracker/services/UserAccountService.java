package com.toptal.expensetracker.services;

import com.toptal.expensetracker.common.ServiceContext;
import com.toptal.expensetracker.dto.UserDTO;

public interface UserAccountService
{
	UserDTO createUser(ServiceContext ctx, UserDTO userDTO);

	UserDTO getCurrentUser(ServiceContext ctx);
}
