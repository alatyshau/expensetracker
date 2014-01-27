package com.toptal.expensetracker.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.toptal.expensetracker.gwt.client.data.UserDTO;

public class AccountCreatedEvent extends GwtEvent<AccountCreatedEvent.Handler>
{
	public static Type<AccountCreatedEvent.Handler> TYPE = new Type<AccountCreatedEvent.Handler>();
	private final UserDTO user;

	public AccountCreatedEvent(final UserDTO user)
	{
		super();
		this.user = user;
	}

	public UserDTO getUser()
	{
		return this.user;
	}

	@Override
	public Type<AccountCreatedEvent.Handler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(final AccountCreatedEvent.Handler handler)
	{
		handler.onAccountCreated(this);
	}

	public interface Handler extends EventHandler
	{
		void onAccountCreated(AccountCreatedEvent event);
	}

}
