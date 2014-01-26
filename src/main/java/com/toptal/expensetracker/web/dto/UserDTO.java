package com.toptal.expensetracker.web.dto;

public class UserDTO
{
	private String email;

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	@Override
	public String toString()
	{
		return "UserDTO [email=" + this.email + "]";
	}

	public static UserDTO sample(final String email)
	{
		final UserDTO userDTO = new UserDTO();
		userDTO.setEmail(email);
		return userDTO;
	}

}
