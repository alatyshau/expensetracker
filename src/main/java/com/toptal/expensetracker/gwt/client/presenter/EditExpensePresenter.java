package com.toptal.expensetracker.gwt.client.presenter;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.dto.ExpenseDTO;
import com.toptal.expensetracker.gwt.client.util.DateUtil;

public class EditExpensePresenter implements Presenter<EditExpensePresenter.Callback>
{
	public static interface Callback
	{
		void onSave(EditExpensePresenter presenter, ExpenseDTO dto);

		void onCancel(EditExpensePresenter presenter);
	}

	public static interface Display
	{
		Field<Date> dateTime();

		Field<Double> amount();

		Field<String> description();

		Field<String> comment();

		HasClickHandlers okButton();

		HasClickHandlers cancelButton();

		void open(String caption);

		void close();

		Widget asWidget();
	}

	private final ExpenseDTO expenseDTO;
	private final Display display;
	private final Date today;
	private Callback callback;
	private HasWidgets container;

	public EditExpensePresenter(final ExpenseDTO expenseDTO, final Display display, final Date today)
	{
		super();
		this.expenseDTO = expenseDTO;
		this.display = display;
		this.today = today;

		bind();
	}

	public void bind()
	{

		this.display.okButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				doSave();
			}
		});

		this.display.cancelButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				clearMessages();
				EditExpensePresenter.this.callback.onCancel(EditExpensePresenter.this);
			}
		});
	}

	@Override
	public void go(final HasWidgets container, final Callback callback)
	{
		this.container = container;
		this.callback = callback;
		final ExpenseDTO dto = this.expenseDTO;
		final Display disp = this.display;
		disp.dateTime().setValue(dto.dateTime != null ? new Date(dto.dateTime) : null);
		disp.amount().setValue(dto.amount);
		disp.description().setValue(dto.description);
		disp.comment().setValue(dto.comment);

		container.add(disp.asWidget());

		disp.open(dto.expenseID == null ? "Add expense" : "Edit expense");
	}

	public void close()
	{
		this.display.close();
		this.container.remove(this.display.asWidget());
	}

	private boolean validate(final Display display)
	{
		boolean result = true;

		display.dateTime().clearErrors();
		final Date dateTime = display.dateTime().getValue();
		if (dateTime == null)
		{
			display.dateTime().showError("Date is empty");
			result = false;
		}
		else if (Math.abs(DateUtil.getWeeksBetween(dateTime, this.today)) > 100)
		{
			display.dateTime().showError("Date is to dar from today (must be in radius of 100 weeks)");
			result = false;
		}

		display.amount().clearErrors();
		final Double amount = display.amount().getValue();
		if (amount == null)
		{
			display.amount().showError("Amount is empty");
			result = false;
		}
		else if (amount <= 0)
		{
			display.amount().showError("Amount must be positive");
			result = false;
		}
		else if (amount >= 40000000)
		{
			display.amount().showError("Amount is too high (max 40 millions)");
			result = false;
		}

		display.description().clearErrors();
		final String description = display.description().getValue();
		if (description == null || description.isEmpty())
		{
			display.description().showError("Description is empty");
			result = false;
		}
		else if (description.length() > 256)
		{
			display.description().showError("Description is too long (max: 256)");
			result = false;
		}

		display.comment().clearErrors();
		final String comment = display.comment().getValue();
		if (comment != null && comment.length() > 1024)
		{
			display.comment().showError("Comment is too long (max: 1024)");
			result = false;
		}

		return result;
	}

	private void clearMessages()
	{
		this.display.dateTime().clearErrors();
		this.display.amount().clearErrors();
		this.display.description().clearErrors();
		this.display.comment().clearErrors();
	}

	private void doSave()
	{
		final Display disp = this.display;
		if (!validate(disp))
		{
			return; // EARLY EXIT !!!
		}
		final ExpenseDTO dto = this.expenseDTO;
		dto.dateTime = disp.dateTime().getValue().getTime();
		dto.amount = disp.amount().getValue();
		dto.description = disp.description().getValue();
		dto.comment = disp.comment().getValue();
		this.callback.onSave(this, dto);
	}
}
