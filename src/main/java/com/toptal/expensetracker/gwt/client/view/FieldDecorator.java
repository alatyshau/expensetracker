package com.toptal.expensetracker.gwt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.presenter.Field;

public class FieldDecorator<T> extends Composite implements Field<T>
{
	interface Binder extends UiBinder<Widget, FieldDecorator<?>>
	{
		Binder BINDER = GWT.create(Binder.class);
	}

	@UiField
	SimplePanel contents;

	@UiField
	DivElement errorLabel;

	private TakesValue<T> innerValue;

	private String msg;

	@UiConstructor
	public FieldDecorator()
	{
		initWidget(Binder.BINDER.createAndBindUi(this));
	}

	public FieldDecorator(final TakesValue<T> widget)
	{
		this();
		this.contents.add((Widget) widget);
		this.innerValue = widget;
	}

	@SuppressWarnings("unchecked")
	@UiChild(limit = 1, tagname = "valuebox")
	public void setValueBox(final Widget widget)
	{
		this.contents.add(widget);
		this.innerValue = (TakesValue<T>) widget;
	}

	public Widget getInnerWidget()
	{
		return (Widget) this.innerValue;
	}

	public FocusWidget getFocusWidget()
	{
		return (FocusWidget) this.innerValue;
	}

	@Override
	public T getValue()
	{
		return this.innerValue.getValue();
	}

	@Override
	public void setValue(final T value)
	{
		this.innerValue.setValue(value);
	}

	@Override
	public void showError(final String msg)
	{
		if (this.msg == msg || this.msg != null && this.msg.equals(msg))
		{
			return;
		}
		this.msg = msg;
		if (msg == null || msg.length() == 0)
		{
			this.errorLabel.setInnerText("");
			this.errorLabel.getStyle().setDisplay(Display.NONE);
			return;
		}

		this.errorLabel.setInnerText(msg);
		this.errorLabel.getStyle().setDisplay(Display.INLINE_BLOCK);
	}

	@Override
	public void clearErrors()
	{
		showError(null);
	}
}
