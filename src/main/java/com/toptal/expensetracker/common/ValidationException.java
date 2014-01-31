package com.toptal.expensetracker.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ValidationException extends RuntimeException
{
	private static final long serialVersionUID = 5462020615160273250L;

	public ValidationException()
	{
		super();
	}

	public ValidationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public ValidationException(final String message)
	{
		super(message);
	}

	public ValidationException(final Throwable cause)
	{
		super(cause);
	}

}
