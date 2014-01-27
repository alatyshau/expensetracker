package com.toptal.expensetracker.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.toptal.expensetracker.gwt.client.data.UserDTO;

public class UserLoggedInEvent extends GwtEvent<UserLoggedInEvent.Handler>
{
	public static Type<UserLoggedInEvent.Handler> TYPE = new Type<UserLoggedInEvent.Handler>();
	private final UserDTO user;

	public UserLoggedInEvent(final UserDTO user)
	{
		super();
		this.user = user;
	}

	public UserDTO getUser()
	{
		return this.user;
	}

	@Override
	public Type<UserLoggedInEvent.Handler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(final UserLoggedInEvent.Handler handler)
	{
		handler.onUserLogggedIn(this);
	}

	public interface Handler extends EventHandler
	{
		void onUserLogggedIn(UserLoggedInEvent event);
	}

}
