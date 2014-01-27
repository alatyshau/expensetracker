package com.toptal.expensetracker.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CreateAccountCancelledEvent extends GwtEvent<CreateAccountCancelledEvent.Handler>
{
	public static Type<CreateAccountCancelledEvent.Handler> TYPE = new Type<CreateAccountCancelledEvent.Handler>();

	@Override
	public Type<CreateAccountCancelledEvent.Handler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(final CreateAccountCancelledEvent.Handler handler)
	{
		handler.onCreateAccountCancelled(this);
	}

	public interface Handler extends EventHandler
	{
		void onCreateAccountCancelled(CreateAccountCancelledEvent event);
	}

}
