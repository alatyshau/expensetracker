package com.toptal.expensetracker.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class SystemException extends RuntimeException
{
	private static final long serialVersionUID = 4668348715387471568L;

	public SystemException()
	{
		super();
	}

	public SystemException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public SystemException(final String message)
	{
		super(message);
	}

	public SystemException(final Throwable cause)
	{
		super(cause);
	}

}
