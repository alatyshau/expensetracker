package com.toptal.expensetracker.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Utils
{
	public static final DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat defaultDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private Utils()
	{
	}

	public static Date dateTime(final String s)
	{
		try
		{
			return defaultDateTimeFormat.parse(s);
		}
		catch (final ParseException e)
		{
			throw new IllegalArgumentException("Expected format is: \"yyyy-MM-dd HH:mm\". Actual string was: " + s, e);
		}
	}

	public static String dateTime(final Date d)
	{
		return d != null ? defaultDateTimeFormat.format(d) : "null";
	}

	public static double skipNull(final Double value)
	{
		return value != null ? value.doubleValue() : 0d;
	}

	public static float skipNull(final Float value)
	{
		return value != null ? value.floatValue() : 0f;
	}

	public static BigDecimal skipNull(final BigDecimal value)
	{
		return value != null ? value : BigDecimal.ZERO;
	}

	public static int skipNull(final Integer value)
	{
		return value != null ? value.intValue() : 0;
	}

	public static long skipNull(final Long value)
	{
		return value != null ? value.longValue() : 0;
	}

	public static <T> Collection<T> skipNull(final Collection<T> collection)
	{
		return collection != null ? collection : Collections.<T> emptyList();
	}

	public static <T> List<T> skipNull(final List<T> list)
	{
		return list != null ? list : Collections.<T> emptyList();
	}

}
