package com.toptal.expensetracker.gwt.client.dto;

public class ExpenseDTO
{
	public String expenseID;
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
