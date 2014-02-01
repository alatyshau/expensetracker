package com.toptal.expensetracker.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.toptal.expensetracker.common.Utils;

public class ExpenseDTO
{
	private Long expenseID;
	private Date dateTime;
	private String description;
	private BigDecimal amount;
	private String comment;

	public Long getExpenseID()
	{
		return this.expenseID;
	}

	public void setExpenseID(final Long expenseID)
	{
		this.expenseID = expenseID;
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
		return "ExpenseDTO [expenseId=" + this.expenseID + ", dateTime=" + Utils.dateTime(this.dateTime)
				+ ", description=" + this.description + ", amount=" + this.amount + ", comment=" + this.comment + "]";
	}

	public static ExpenseDTO sample()
	{
		return sample(123L);
	}

	public static ExpenseDTO sample(final Long expenseId)
	{
		final ExpenseDTO expense = new ExpenseDTO();
		expense.setExpenseID(expenseId);
		expense.setDateTime(Utils.dateTime("2014-01-07 12:12"));
		expense.setDescription("groceries");
		expense.setAmount(BigDecimal.valueOf(100.12345d));
		expense.setComment("");
		return expense;
	}

	public static ExpenseDTO sample(final Long expenseId, final String date, final String desc)
	{
		final ExpenseDTO expense = new ExpenseDTO();
		expense.setExpenseID(expenseId);
		expense.setDateTime(Utils.dateTime(date));
		expense.setDescription(desc);
		expense.setAmount(BigDecimal.valueOf(100.12345d));
		expense.setComment("comment");
		return expense;
	}
}
