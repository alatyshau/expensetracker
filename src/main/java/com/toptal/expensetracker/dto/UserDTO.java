package com.toptal.expensetracker.dto;

public class UserDTO
{
	private String email;
	private String password;

	public UserDTO()
	{
		super();
	}

	public UserDTO(final String email)
	{
		super();
		this.email = email;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	@Override
	public String toString()
	{
		return "UserDTO [email=" + this.email + "]";
	}
}
