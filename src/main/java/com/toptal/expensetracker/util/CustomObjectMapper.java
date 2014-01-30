package com.toptal.expensetracker.util;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

public class CustomObjectMapper extends ObjectMapper
{

	public CustomObjectMapper()
	{
		super();
		configure(Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		setDateFormat(new SimpleDateFormat(Utils.DEFAULT_DATE_TIME_FORMAT_STR));
	}
}