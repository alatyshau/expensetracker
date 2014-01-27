package com.toptal.expensetracker.gwt.client.data;

public class UserDTO
{
	public String email;
	public String password;

	public UserDTO()
	{
		super();
	}

	public UserDTO(final String email, final String password)
	{
		super();
		this.email = email;
		this.password = password;
	}

}
