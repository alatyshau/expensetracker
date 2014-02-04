package com.toptal.expensetracker.gwt.client.presenter;

import com.google.gwt.user.client.TakesValue;

public interface Field<T> extends TakesValue<T>
{
	void showError(String msg);

	void clearErrors();
}
