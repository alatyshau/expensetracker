package com.toptal.expensetracker.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CreateAccountEvent extends GwtEvent<CreateAccountEvent.Handler>
{
	public static Type<CreateAccountEvent.Handler> TYPE = new Type<CreateAccountEvent.Handler>();

	@Override
	public Type<CreateAccountEvent.Handler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(final CreateAccountEvent.Handler handler)
	{
		handler.onCreateAccount(this);
	}

	public interface Handler extends EventHandler
	{
		void onCreateAccount(CreateAccountEvent event);
	}

}
