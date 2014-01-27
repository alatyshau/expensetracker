package com.toptal.expensetracker.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class UserLogOutEvent extends GwtEvent<UserLogOutEvent.Handler>
{
	public static Type<UserLogOutEvent.Handler> TYPE = new Type<UserLogOutEvent.Handler>();

	public UserLogOutEvent()
	{
		super();
	}

	@Override
	public Type<UserLogOutEvent.Handler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(final UserLogOutEvent.Handler handler)
	{
		handler.onUserLogOut(this);
	}

	public interface Handler extends EventHandler
	{
		void onUserLogOut(UserLogOutEvent event);
	}

}
