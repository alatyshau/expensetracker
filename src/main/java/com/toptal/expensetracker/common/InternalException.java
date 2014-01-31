package com.toptal.expensetracker.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalException extends RuntimeException
{
	private static final long serialVersionUID = 4668348715387471568L;

	public InternalException()
	{
		super();
	}

	public InternalException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public InternalException(final String message)
	{
		super(message);
	}

	public InternalException(final Throwable cause)
	{
		super(cause);
	}

}
