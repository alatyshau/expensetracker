package com.toptal.expensetracker.gwt.client.data;

public class ExpenseDTO
{
	public String expenseId;
	public Long dateTime;
	public String description;
	public Double amount;
	public String comment;

	@Override
	public String toString()
	{
		return "$" + this.amount + " -- " + this.description + " (" + this.dateTime + ")";
	}
}
