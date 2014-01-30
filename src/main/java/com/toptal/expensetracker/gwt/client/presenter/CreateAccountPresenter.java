package com.toptal.expensetracker.gwt.client.presenter;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.ServiceBus;
import com.toptal.expensetracker.gwt.client.data.UserDTO;
import com.toptal.expensetracker.gwt.client.event.AccountCreatedEvent;
import com.toptal.expensetracker.gwt.client.event.CreateAccountCancelledEvent;

public class CreateAccountPresenter implements Presenter
{
	public interface Display
	{
		HasClickHandlers getCreateButton();

		HasClickHandlers getCancelButton();

		HasValue<String> getEmail();

		HasValue<String> getPassword();

		HasValue<String> getConfirmPassword();

		Widget asWidget();
	}

	private final ServiceBus serviceBus;
	private final HandlerManager eventBus;
	private final Display display;

	public CreateAccountPresenter(final ServiceBus serviceBus, final HandlerManager eventBus, final Display display)
	{
		super();
		this.serviceBus = serviceBus;
		this.eventBus = eventBus;
		this.display = display;
	}

	public void bind()
	{
		this.display.getCreateButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				doCreateAccount();
			}
		});

		this.display.getCancelButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				CreateAccountPresenter.this.eventBus.fireEvent(new CreateAccountCancelledEvent());
			}
		});
	}

	@Override
	public void go(final HasWidgets container)
	{
		bind();
		container.clear();
		container.add(this.display.asWidget());
		this.display.getEmail().setValue(null);
		this.display.getPassword().setValue(null);
		this.display.getConfirmPassword().setValue(null);
	}

	private void doCreateAccount()
	{
		final String email = this.display.getEmail().getValue();
		final String password = this.display.getPassword().getValue();
		final String confirmPassword = this.display.getConfirmPassword().getValue();

		if (password != confirmPassword)
		{
			Window.alert("Passwords are different");
			return; // EARLY EXIT !!!
		}

		this.serviceBus.userService.createUser(new UserDTO(email, password), new MethodCallback<UserDTO>()
		{

			@Override
			public void onSuccess(final Method method, final UserDTO response)
			{
				CreateAccountPresenter.this.eventBus.fireEvent(new AccountCreatedEvent(response));
			}

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				Window.alert("Error creating account: check fields\n" + String.valueOf(exception));
			}
		});
	}
}
