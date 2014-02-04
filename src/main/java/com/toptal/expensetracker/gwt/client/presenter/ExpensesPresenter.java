package com.toptal.expensetracker.gwt.client.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.AppController;
import com.toptal.expensetracker.gwt.client.ServiceBus;
import com.toptal.expensetracker.gwt.client.dto.ExpenseDTO;
import com.toptal.expensetracker.gwt.client.dto.UserDTO;
import com.toptal.expensetracker.gwt.client.event.EditExpenseEvent;
import com.toptal.expensetracker.gwt.client.event.RemoveExpenseEvent;
import com.toptal.expensetracker.gwt.client.view.EditExpenseView;

public class ExpensesPresenter implements Presenter<Void>, EditExpensePresenter.Callback
{
	public interface Display
	{
		HasClickHandlers getAddButton();

		EditExpenseEvent.HasHandlers getEditExpenseHandlers();

		RemoveExpenseEvent.HasHandlers getRemoveExpenseHandlers();

		void setData(Collection<ExpenseDTO> data);

		void addExpense(ExpenseDTO expense);

		void updateExpense(ExpenseDTO expense);

		void removeExpenses(Collection<String> expenseIDs);

		Widget asWidget();
	}

	private List<ExpenseDTO> expenses;

	private final ServiceBus serviceBus;
	private final Display display;
	private final AppController.Display rootDisplay;
	private final UserDTO user;
	private final Date today;
	private HasWidgets container;

	public ExpensesPresenter(final ServiceBus serviceBus, final Display display,
			final AppController.Display rootDisplay, final UserDTO user, final Date today)
	{
		super();
		this.serviceBus = serviceBus;
		this.display = display;
		this.rootDisplay = rootDisplay;
		this.user = user;
		this.today = today;

		bind();
	}

	public void bind()
	{
		this.display.getAddButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				doEditExpense(new ExpenseDTO());
			}
		});

		this.display.getEditExpenseHandlers().addEditExpenseHandler(new EditExpenseEvent.Handler()
		{

			@Override
			public void onEditExpense(final EditExpenseEvent event)
			{
				doEditExpense(event.getExpense());
			}
		});

		this.display.getRemoveExpenseHandlers().addRemoveExpenseHandler(new RemoveExpenseEvent.Handler()
		{

			@Override
			public void onRemoveExpense(final RemoveExpenseEvent event)
			{
				doDeleteExpense(event.getExpenses());
			}
		});
	}

	@Override
	public void go(final HasWidgets container, final Void callback)
	{
		this.container = container;
		container.clear();
		container.add(this.display.asWidget());
		fetchExpenses();
	}

	private void doEditExpense(final ExpenseDTO expense)
	{
		new EditExpensePresenter(expense, new EditExpenseView(), this.today).go(this.container, this);
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

	private void doDeleteExpense(final Collection<ExpenseDTO> expenses)
	{

		final List<String> expenseIDs = new ArrayList<String>();
		for (final ExpenseDTO dto : expenses)
		{
			expenseIDs.add(dto.expenseID);
		}
		this.serviceBus.expenseTrackerService.deleteExpenses(expenseIDs, new MethodCallback<Void>()
		{
			@Override
			public void onSuccess(final Method method, final Void response)
			{
				ExpensesPresenter.this.display.removeExpenses(expenseIDs);
			}

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				ExpensesPresenter.this.rootDisplay
						.showError("Error while sending expense to server", method, exception);
			}
		});
	}

	@Override
	public void onSave(final EditExpensePresenter presenter, final ExpenseDTO dto)
	{
		presenter.close();

		final String expenseID = dto.expenseID;
		final boolean isAdding = expenseID == null;

		final MethodCallback<ExpenseDTO> callback = new MethodCallback<ExpenseDTO>()
		{
			@Override
			public void onSuccess(final Method method, final ExpenseDTO response)
			{
				if (isAdding)
				{
					ExpensesPresenter.this.display.addExpense(response);
				}
				else
				{
					ExpensesPresenter.this.display.updateExpense(response);
				}
			}

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				ExpensesPresenter.this.rootDisplay
						.showError("Error while sending expense to server", method, exception);
			}
		};

		if (isAdding)
		{
			this.serviceBus.expenseTrackerService.createExpense(dto, callback);
		}
		else
		{
			this.serviceBus.expenseTrackerService.updateExpense(expenseID, dto, callback);
		}
	}

	@Override
	public void onCancel(final EditExpensePresenter presenter)
	{
		presenter.close();
	}

}
