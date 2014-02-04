package com.toptal.expensetracker.gwt.client.view;

import java.util.Date;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.presenter.EditExpensePresenter;
import com.toptal.expensetracker.gwt.client.presenter.Field;

public class EditExpenseView implements EditExpensePresenter.Display
{
	interface Binder extends UiBinder<Widget, EditExpenseView>
	{
		Binder BINDER = GWT.create(Binder.class);
	}

	@UiField
	FieldDecorator<Date> dateTime;
	@UiField
	FieldDecorator<Double> amount;
	@UiField
	FieldDecorator<String> description;
	@UiField
	FieldDecorator<String> comment;
	@UiField
	Button okButton;
	@UiField
	Button cancelButton;

	private final Modal modal;

	public EditExpenseView()
	{
		this.modal = (Modal) Binder.BINDER.createAndBindUi(this);
		this.modal.setCloseVisible(false);
	}

	@Override
	public Field<Date> dateTime()
	{
		return this.dateTime;
	}

	@Override
	public Field<Double> amount()
	{
		return this.amount;
	}

	@Override
	public Field<String> description()
	{
		return this.description;
	}

	@Override
	public Field<String> comment()
	{
		return this.comment;
	}

	@Override
	public HasClickHandlers okButton()
	{
		return this.okButton;
	}

	@Override
	public HasClickHandlers cancelButton()
	{
		return this.cancelButton;
	}

	@Override
	public void open(final String caption)
	{
		this.modal.setTitle(caption);
		this.modal.show();
	}

	@Override
	public void close()
	{
		this.modal.hide();
	}

	@Override
	public Widget asWidget()
	{
		return this.modal;
	}

}
