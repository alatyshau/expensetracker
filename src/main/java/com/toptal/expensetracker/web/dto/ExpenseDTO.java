package com.toptal.expensetracker.web.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.toptal.expensetracker.util.Utils;

public class ExpenseDTO
{
	private Long expenseId;
	private Date dateTime;
	private String description;
	private BigDecimal amount;
	private String comment;

	public Long getExpenseId()
	{
		return this.expenseId;
	}

	public void setExpenseId(final Long expenseId)
	{
		this.expenseId = expenseId;
	}

	public Date getDateTime()
	{
		return this.dateTime;
	}

	public void setDateTime(final Date dateTime)
	{
		this.dateTime = dateTime;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public BigDecimal getAmount()
	{
		return this.amount;
	}

	public void setAmount(final BigDecimal amount)
	{
		this.amount = amount;
	}

	public String getComment()
	{
		return this.comment;
	}

	public void setComment(final String comment)
	{
		this.comment = comment;
	}

	@Override
	public String toString()
	{
		return "ExpenseDTO [expenseId=" + this.expenseId + ", dateTime=" + Utils.dateTime(this.dateTime)
				+ ", description=" + this.description + ", amount=" + this.amount + ", comment=" + this.comment + "]";
	}

	public static ExpenseDTO sample()
	{
		return sample(123L);
	}

	public static ExpenseDTO sample(final Long expenseId)
	{
		final ExpenseDTO expense = new ExpenseDTO();
		expense.setExpenseId(expenseId);
		expense.setDateTime(Utils.dateTime("2014-01-07 12:12"));
		expense.setDescription("groceries");
		expense.setAmount(BigDecimal.valueOf(100.12345d));
		expense.setComment("");
		return expense;
	}
}
