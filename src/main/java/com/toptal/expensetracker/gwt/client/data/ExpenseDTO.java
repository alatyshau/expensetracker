package com.toptal.expensetracker.gwt.client.data;

public class ExpenseDTO
{
	public String expenseId;
	public String dateTime;
	public String description;
	public String amount;
	public String comment;

	@Override
	public String toString()
	{
		return "$" + this.amount + " -- " + this.description + " (" + this.dateTime + ")";
	}
}
