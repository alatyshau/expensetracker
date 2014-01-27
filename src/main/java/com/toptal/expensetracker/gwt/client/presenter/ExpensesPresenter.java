package com.toptal.expensetracker.gwt.client.presenter;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.ServiceBus;
import com.toptal.expensetracker.gwt.client.data.ExpenseDTO;
import com.toptal.expensetracker.gwt.client.data.UserDTO;
import com.toptal.expensetracker.gwt.client.event.UserLogOutEvent;

public class ExpensesPresenter
{
	public interface Display
	{
		HasClickHandlers getAddButton();

		HasClickHandlers getEditButton();

		HasClickHandlers getDeleteButton();

		HasClickHandlers getLogoutButton();

		void setData(List<ExpenseDTO> data);

		int getSelectedRow();

		Widget asWidget();
	}

	private List<ExpenseDTO> expenses;

	private final ServiceBus serviceBus;
	private final HandlerManager eventBus;
	private final Display display;
	private final UserDTO user;

	public ExpensesPresenter(final ServiceBus serviceBus, final HandlerManager eventBus, final Display display,
			final UserDTO user)
	{
		super();
		this.serviceBus = serviceBus;
		this.eventBus = eventBus;
		this.display = display;
		this.user = user;
	}

	public void bind()
	{
		this.display.getAddButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				// ExpensesPresenter.this.eventBus.fireEvent(new
				// AddExpenseEvent());
			}
		});

		this.display.getEditButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				final int selectedRow = ExpensesPresenter.this.display.getSelectedRow();

				if (selectedRow >= 0)
				{
					final String expenseId = ExpensesPresenter.this.expenses.get(selectedRow).expenseId;
					// ExpensesPresenter.this.eventBus.fireEvent(new
					// EditExpenseEvent(expenseId));
				}
			}
		});

		this.display.getDeleteButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				doDeleteExpense();
			}

		});

		this.display.getLogoutButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				ExpensesPresenter.this.eventBus.fireEvent(new UserLogOutEvent());
			}
		});
	}

	public void go(final HasWidgets container)
	{
		bind();
		container.clear();
		container.add(this.display.asWidget());
		fetchExpenses();
	}

	private void fetchExpenses()
	{
		this.serviceBus.expenseTrackerService.getExpenses(new MethodCallback<List<ExpenseDTO>>()
		{

			@Override
			public void onSuccess(final Method method, final List<ExpenseDTO> response)
			{
				ExpensesPresenter.this.expenses = response;
				// sortContactDetails();
				// final List<String> data = new ArrayList<String>();
				//
				// for (int i = 0; i < result.size(); ++i)
				// {
				// data.add(contactDetails.get(i).getDisplayName());
				// }

				ExpensesPresenter.this.display.setData(response);
			}

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				Window.alert("Error fetching expenses");
			}
		});
	}

	private void doDeleteExpense()
	{
		final int selectedRow = ExpensesPresenter.this.display.getSelectedRow();

		if (selectedRow >= 0)
		{
			final String expenseId = ExpensesPresenter.this.expenses.get(selectedRow).expenseId;

			// serviceBus.expenseTrackerService.delete
			//
			// rpcService.deleteContacts(ids, new
			// AsyncCallback<ArrayList<ContactDetails>>() {
			// public void onSuccess(ArrayList<ContactDetails> result) {
			// contactDetails = result;
			// sortContactDetails();
			// List<String> data = new ArrayList<String>();
			//
			// for (int i = 0; i < result.size(); ++i) {
			// data.add(contactDetails.get(i).getDisplayName());
			// }
			//
			// display.setData(data);
			//
			// }
			//
			// public void onFailure(Throwable caught) {
			// Window.alert("Error deleting selected contacts");
			// }
			// });
		}
	}

}
