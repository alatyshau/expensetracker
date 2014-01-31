package com.toptal.expensetracker.common;

import java.util.Locale;

import com.toptal.expensetracker.dto.UserDTO;

public interface ServiceContext
{
	Locale getLocale();

	UserDTO getUser();
}
