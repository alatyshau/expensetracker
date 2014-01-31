package com.toptal.expensetracker.dto;

public class ErrorDTO
{
	private String message;

	public ErrorDTO()
	{
		super();
	}

	public ErrorDTO(final String message)
	{
		super();
		this.message = message;
	}

	public String getMessage()
	{
		return this.message;
	}

	public void setMessage(final String message)
	{
		this.message = message;
	}

}
