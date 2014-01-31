package com.toptal.expensetracker.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.toptal.expensetracker.common.ValidationException;
import com.toptal.expensetracker.dto.ErrorDTO;

public class BaseController
{
	@ExceptionHandler({ ValidationException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorDTO resolveValidationException(final ValidationException ex)
	{
		return new ErrorDTO(ex.getMessage());
	}

	@ExceptionHandler({ RuntimeException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorDTO resolveException(final RuntimeException ex)
	{
		return new ErrorDTO(ex.getMessage());
	}
}
