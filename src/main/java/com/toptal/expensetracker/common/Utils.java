package com.toptal.expensetracker.common;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.datepicker.client.CalendarUtil;

public class Utils
{
	public static final SimpleDateFormat s = null;
	public static final String DEFAULT_DATE_FORMAT_STR = "yyyy-MM-dd";
	public static final String DEFAULT_DATE_TIME_FORMAT_STR = "yyyy-MM-dd HH:mm";

	private static final ThreadLocal<SoftReference<DateFormat>> tl1 = new ThreadLocal<SoftReference<DateFormat>>();
	private static final ThreadLocal<SoftReference<DateFormat>> tl2 = new ThreadLocal<SoftReference<DateFormat>>();

	// 2000-01-01 12:00 --> SATURDAY = 5
	private static Date SPECIAL_MONDAY;
	static
	{
		try
		{
			SPECIAL_MONDAY = getDateTimeFormat().parse("2000-01-01 12:00");
		}
		catch (final ParseException e)
		{
			throw new IllegalStateException(e);
		}
	}

	private Utils()
	{
	}

	public static void main(final String[] args) throws Exception
	{
		System.out.println(getWeeksBetween(dateTime("2014-01-02 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2014-01-01 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2013-12-31 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2013-12-30 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2013-12-29 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2013-12-28 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2013-12-27 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2013-12-26 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2013-12-25 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2013-12-24 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2013-12-23 12:00"), dateTime("2013-12-22 12:00")));
		System.out.println(getWeeksBetween(dateTime("2013-12-22 12:00"), dateTime("2013-12-22 12:00")));

		System.out.println((double) dateTime("2013-12-22 12:00").getTime());
	}

	/** Aligned to Monday. */
	public static int getWeeksBetween(final Date d, final Date today)
	{
		final int td = dayOfWeek(today);
		final int daysBetweenD1andMyMonday = td - CalendarUtil.getDaysBetween(d, today);

		if (daysBetweenD1andMyMonday >= 0)
		{
			return daysBetweenD1andMyMonday / 7;
		}
		else
		{
			return -1 + (daysBetweenD1andMyMonday + 1) / 7;
		}
	}

	public static int dayOfWeek(final Date d)
	{
		// = 5 SATURDAY
		final int result = CalendarUtil.getDaysBetween(SPECIAL_MONDAY, d) % 7 - 2;
		return result >= 0 ? result : 7 + result;
	}

	public static int getDaysBetween(final Date start, final Date finish)
	{
		final long aTime = start.getTime();
		final long bTime = finish.getTime();

		long adjust = 60 * 60 * 1000;
		adjust = (bTime > aTime) ? adjust : -adjust;

		return (int) ((bTime - aTime + adjust) / (24 * 60 * 60 * 1000));
	}

	public static DateFormat getDateFormat()
	{
		SoftReference<DateFormat> ref = tl1.get();
		if (ref != null)
		{
			final DateFormat result = ref.get();
			if (result != null)
			{
				return result;
			}
		}
		final DateFormat result = new SimpleDateFormat(DEFAULT_DATE_FORMAT_STR);
		ref = new SoftReference<DateFormat>(result);
		tl1.set(ref);
		return result;
	}

	public static DateFormat getDateTimeFormat()
	{
		SoftReference<DateFormat> ref = tl2.get();
		if (ref != null)
		{
			final DateFormat result = ref.get();
			if (result != null)
			{
				return result;
			}
		}
		final DateFormat result = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT_STR);
		ref = new SoftReference<DateFormat>(result);
		tl2.set(ref);
		return result;
	}

	public static Date dateTime(final String s)
	{
		try
		{
			return getDateTimeFormat().parse(s);
		}
		catch (final ParseException e)
		{
			throw new IllegalArgumentException("Expected format is: \"yyyy-MM-dd HH:mm\". Actual string was: " + s, e);
		}
	}

	public static String dateTime(final Date d)
	{
		return d != null ? getDateTimeFormat().format(d) : "null";
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

	public static void ensureRoles(final ServiceContext ctx, final AccessRole role)
	{
		if (!ctx.getRoles().contains(role))
		{
			throw new UnauthorizedAccessException("Role required: " + role + ". Actual roles: " + ctx.getRoles()
					+ ". User: " + ctx.getUserId());
		}
	}
}
