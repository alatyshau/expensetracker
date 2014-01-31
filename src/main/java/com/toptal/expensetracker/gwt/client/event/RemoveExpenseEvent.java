package com.toptal.expensetracker.gwt.client.event;

import java.util.Collection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.toptal.expensetracker.gwt.client.dto.ExpenseDTO;

public class RemoveExpenseEvent extends GwtEvent<RemoveExpenseEvent.Handler>
{
	public static Type<RemoveExpenseEvent.Handler> TYPE = new Type<RemoveExpenseEvent.Handler>();
	private final Collection<ExpenseDTO> expenses;

	public RemoveExpenseEvent(final Collection<ExpenseDTO> expenses)
	{
		super();
		this.expenses = expenses;
	}

	public Collection<ExpenseDTO> getExpenses()
	{
		return this.expenses;
	}

	@Override
	public Type<RemoveExpenseEvent.Handler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(final RemoveExpenseEvent.Handler handler)
	{
		handler.onEditExpense(this);
	}

	public interface Handler extends EventHandler
	{
		void onEditExpense(RemoveExpenseEvent event);
	}

	public interface HasHandlers extends com.google.gwt.event.shared.HasHandlers
	{
		HandlerRegistration addEditExpenseHandler(RemoveExpenseEvent.Handler handler);
	}

}
