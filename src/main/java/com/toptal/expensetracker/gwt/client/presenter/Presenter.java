package com.toptal.expensetracker.gwt.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

public abstract interface Presenter<R>
{
	public abstract void go(final HasWidgets container, final R callback);
}
