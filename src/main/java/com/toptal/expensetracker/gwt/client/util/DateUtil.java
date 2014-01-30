package com.toptal.expensetracker.gwt.client.util;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class DateUtil
{
	public static final DateTimeFormat DEFAULT_DATE_TIME_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm");

	// 2000-01-01 12:00 --> SATURDAY = 5
	private static final Date SPECIAL_MONDAY = DEFAULT_DATE_TIME_FORMAT.parse("2000-01-01 12:00");

	private DateUtil()
	{
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

	/** Aligned to Monday. */
	public static int getWeeksBetween(final Date d, final Date today, final int todayDay)
	{
		final int daysBetweenD1andMyMonday = todayDay - CalendarUtil.getDaysBetween(d, today);

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

	public static Date dateTime(final String s)
	{
		return DEFAULT_DATE_TIME_FORMAT.parse(s);
	}

	public static String dateTime(final Date d)
	{
		return DEFAULT_DATE_TIME_FORMAT.format(d);
	}
}
