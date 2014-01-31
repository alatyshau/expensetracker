package com.toptal.expensetracker.gwt.client.presenter;

import java.util.Collection;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.AppController;
import com.toptal.expensetracker.gwt.client.ServiceBus;
import com.toptal.expensetracker.gwt.client.dto.ExpenseDTO;
import com.toptal.expensetracker.gwt.client.dto.UserDTO;
import com.toptal.expensetracker.gwt.client.event.EditExpenseEvent;
import com.toptal.expensetracker.gwt.client.event.RemoveExpenseEvent;

public class ExpensesPresenter
{
	public interface Display
	{
		HasClickHandlers getAddButton();

		EditExpenseEvent.HasHandlers getEditExpenseHandlers();

		RemoveExpenseEvent.HasHandlers getRemoveExpenseHandlers();

		HasClickHandlers getDeleteButton();

		void setData(Collection<ExpenseDTO> data);

		void addExpense(ExpenseDTO expense);

		void updateExpense(ExpenseDTO expense);

		void removeExpenses(Collection<String> expenseIds);

		Collection<String> getSelectedExpenses();

		Widget asWidget();
	}

	private List<ExpenseDTO> expenses;

	private final ServiceBus serviceBus;
	private final HandlerManager eventBus;
	private final Display display;
	private final AppController.Display rootDisplay;
	private final UserDTO user;

	public ExpensesPresenter(final ServiceBus serviceBus, final HandlerManager eventBus, final Display display,
			final AppController.Display rootDisplay, final UserDTO user)
	{
		super();
		this.serviceBus = serviceBus;
		this.eventBus = eventBus;
		this.display = display;
		this.rootDisplay = rootDisplay;
		this.user = user;

		bind();
	}

	public void bind()
	{
		// this.display.getAddButton().addClickHandler(new ClickHandler()
		// {
		// @Override
		// public void onClick(final ClickEvent event)
		// {
		// // ExpensesPresenter.this.eventBus.fireEvent(new
		// // AddExpenseEvent());
		// }
		// });
		//
		// this.display.getEditExpenseHandlers().addEditExpenseHandler(new
		// EditExpenseEvent.Handler()
		// {
		//
		// @Override
		// public void onEditExpense(final EditExpenseEvent event)
		// {
		// final String expenseId = event.getExpense().expenseId;
		// // ExpensesPresenter.this.eventBus.fireEvent(new
		// // EditExpenseEvent(expenseId));
		// }
		// });
		//
		// this.display.getDeleteButton().addClickHandler(new ClickHandler()
		// {
		// @Override
		// public void onClick(final ClickEvent event)
		// {
		// doDeleteExpense();
		// }
		//
		// });
	}

	public void go(final HasWidgets container)
	{
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
				ExpensesPresenter.this.rootDisplay.showError("Error fetching expenses", method, exception);
			}
		});
	}

	private void doDeleteExpense()
	{
		// final List<Integer> selectedExpenses =
		// ExpensesPresenter.this.display.getSelectedExpenses();

		// if (selectedRow >= 0)
		// {
		// final String expenseId =
		// ExpensesPresenter.this.expenses.reget(selectedRow).expenseId;

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
		// }
	}

}