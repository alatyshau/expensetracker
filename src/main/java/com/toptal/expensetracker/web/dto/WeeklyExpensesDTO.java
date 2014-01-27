package com.toptal.expensetracker.web.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WeeklyExpensesDTO
{
	private int week;
	private BigDecimal total;
	private BigDecimal dailyAverage;

	public WeeklyExpensesDTO()
	{
		super();
	}

	public WeeklyExpensesDTO(final int week, final BigDecimal total, final BigDecimal dailyAverage)
	{
		super();
		this.week = week;
		this.total = total;
		this.dailyAverage = dailyAverage;
	}

	public int getWeek()
	{
		return this.week;
	}

	public void setWeek(final int week)
	{
		this.week = week;
	}

	public BigDecimal getTotal()
	{
		return this.total;
	}

	public void setTotal(final BigDecimal total)
	{
		this.total = total;
	}

	public BigDecimal getDailyAverage()
	{
		return this.dailyAverage;
	}

	public void setDailyAverage(final BigDecimal dailyAverage)
	{
		this.dailyAverage = dailyAverage;
	}

	@Override
	public String toString()
	{
		return "WeeklyExpensesDTO [week=" + this.week + ", total=" + this.total + ", dailyAverage=" + this.dailyAverage
				+ "]";
	}

	public static WeeklyExpensesDTO sample(final int week, final int total)
	{
		final BigDecimal totalDecimal = BigDecimal.valueOf(total);
		final BigDecimal seven = BigDecimal.valueOf(7);
		final BigDecimal dailyAverage = totalDecimal.divide(seven, RoundingMode.HALF_UP);
		return new WeeklyExpensesDTO(week, totalDecimal, dailyAverage);
	}
}
