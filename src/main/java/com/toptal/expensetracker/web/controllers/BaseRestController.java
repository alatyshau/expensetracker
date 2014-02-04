package com.toptal.expensetracker.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.toptal.expensetracker.common.IllegalRequestException;
import com.toptal.expensetracker.common.ServiceContext;
import com.toptal.expensetracker.common.ValidationException;
import com.toptal.expensetracker.dto.ErrorDTO;
import com.toptal.expensetracker.web.misc.ContextInitInterceptor;

public class BaseRestController
{
	@ExceptionHandler({ ValidationException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorDTO resolveValidationException(final ValidationException ex)
	{
		ex.printStackTrace();
		return new ErrorDTO(ex.getMessage());
	}

	@ExceptionHandler({ IllegalRequestException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO resolveIllegalRequestException(final IllegalRequestException ex)
	{
		ex.printStackTrace();
		return new ErrorDTO(ex.getMessage());
	}

	@ExceptionHandler({ RuntimeException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorDTO resolveException(final RuntimeException ex)
	{
		ex.printStackTrace();
		return new ErrorDTO(ex.getMessage());
	}

	public ServiceContext ctx()
	{
		return ContextInitInterceptor.getCurrentContext();
	}
}
