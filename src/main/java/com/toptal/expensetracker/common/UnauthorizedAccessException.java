package com.toptal.expensetracker.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedAccessException extends RuntimeException
{

	private static final long serialVersionUID = 561492742811652676L;

	public UnauthorizedAccessException()
	{
		super();
	}

	public UnauthorizedAccessException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public UnauthorizedAccessException(final String message)
	{
		super(message);
	}

	public UnauthorizedAccessException(final Throwable cause)
	{
		super(cause);
	}

}
