package com.toptal.expensetracker.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.toptal.expensetracker.gwt.client.dto.ExpenseDTO;

public class EditExpenseEvent extends GwtEvent<EditExpenseEvent.Handler>
{
	public static Type<EditExpenseEvent.Handler> TYPE = new Type<EditExpenseEvent.Handler>();
	private final ExpenseDTO expense;

	public EditExpenseEvent(final ExpenseDTO expense)
	{
		super();
		this.expense = expense;
	}

	public ExpenseDTO getExpense()
	{
		return this.expense;
	}

	@Override
	public Type<EditExpenseEvent.Handler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(final EditExpenseEvent.Handler handler)
	{
		handler.onEditExpense(this);
	}

	public interface Handler extends EventHandler
	{
		void onEditExpense(EditExpenseEvent event);
	}

	public interface HasHandlers extends com.google.gwt.event.shared.HasHandlers
	{
		HandlerRegistration addEditExpenseHandler(EditExpenseEvent.Handler handler);
	}

}
