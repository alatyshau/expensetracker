package com.toptal.expensetracker.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalRequestException extends RuntimeException
{

	private static final long serialVersionUID = 2192793063296016354L;

	public IllegalRequestException()
	{
		super();
	}

	public IllegalRequestException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public IllegalRequestException(final String message)
	{
		super(message);
	}

	public IllegalRequestException(final Throwable cause)
	{
		super(cause);
	}

}
