package com.toptal.expensetracker.common;

import org.springframework.security.core.GrantedAuthority;

public enum AccessRole implements GrantedAuthority
{
	USER, ADMIN;

	@Override
	public String getAuthority()
	{
		return name();
	}

}
