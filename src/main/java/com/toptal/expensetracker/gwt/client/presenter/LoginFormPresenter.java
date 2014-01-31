package com.toptal.expensetracker.gwt.client.presenter;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.AppController;
import com.toptal.expensetracker.gwt.client.ServiceBus;
import com.toptal.expensetracker.gwt.client.dto.UserDTO;
import com.toptal.expensetracker.gwt.client.event.CreateAccountEvent;
import com.toptal.expensetracker.gwt.client.event.UserLoggedInEvent;

public class LoginFormPresenter implements Presenter
{
	public interface Display
	{
		HasClickHandlers getLoginButton();

		HasClickHandlers getCreateAccountButton();

		HasValue<String> getEmail();

		HasValue<String> getPassword();

		Widget asWidget();
	}

	private String lastEmail;

	private final ServiceBus serviceBus;
	private final HandlerManager eventBus;
	private final Display display;
	private final AppController.Display rootDisplay;

	public LoginFormPresenter(final ServiceBus serviceBus, final HandlerManager eventBus, final Display display,
			final AppController.Display rootDisplay)
	{
		super();
		this.serviceBus = serviceBus;
		this.eventBus = eventBus;
		this.display = display;
		this.rootDisplay = rootDisplay;

		bind();
	}

	public void bind()
	{
		final AppController.Display rootDisplay = this.rootDisplay;

		this.display.getLoginButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				rootDisplay.clearMessage();
				doLogin();
			}
		});

		this.display.getCreateAccountButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				rootDisplay.clearMessage();
				LoginFormPresenter.this.eventBus.fireEvent(new CreateAccountEvent());
			}
		});
	}

	public String getLastEmail()
	{
		return this.lastEmail;
	}

	public void setLastEmail(final String lastEmail)
	{
		this.lastEmail = lastEmail;
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(this.display.asWidget());
		this.display.getEmail().setValue(this.lastEmail);
		this.display.getPassword().setValue(null);
	}

	private void doLogin()
	{
		final AppController.Display rootDisplay = this.rootDisplay;

		final String email = this.display.getEmail().getValue();
		this.lastEmail = email;
		final String password = this.display.getPassword().getValue();
		this.serviceBus.userService.login(email, password, new MethodCallback<UserDTO>()
		{

			@Override
			public void onSuccess(final Method method, final UserDTO response)
			{
				LoginFormPresenter.this.eventBus.fireEvent(new UserLoggedInEvent(response));
			}

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				rootDisplay.showError("Error logging in", method, exception);
			}
		});
	}
}
