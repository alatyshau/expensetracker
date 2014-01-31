package com.toptal.expensetracker.dto;

public class LoginResultDTO
{
	private String token;
	private UserDTO user;

	public String getToken()
	{
		return this.token;
	}

	public void setToken(final String token)
	{
		this.token = token;
	}

	public UserDTO getUser()
	{
		return this.user;
	}

	public void setUser(final UserDTO user)
	{
		this.user = user;
	}

	@Override
	public String toString()
	{
		return "LoginResultDTO [token=" + this.token + ", user=" + this.user + "]";
	}

	public static LoginResultDTO sample(final String email)
	{
		final LoginResultDTO loginResultDTO = new LoginResultDTO();
		loginResultDTO.setToken("xcdsd3eqwdwdw");
		loginResultDTO.setUser(new UserDTO(email));
		return loginResultDTO;
	}
}
