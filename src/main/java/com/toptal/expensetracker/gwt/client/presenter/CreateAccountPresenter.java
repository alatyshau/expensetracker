package com.toptal.expensetracker.gwt.client.presenter;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.AppController;
import com.toptal.expensetracker.gwt.client.ServiceBus;
import com.toptal.expensetracker.gwt.client.dto.UserDTO;

public class CreateAccountPresenter implements Presenter<CreateAccountPresenter.Callback>
{
	public interface Callback
	{
		void onCancelCreateAccount();

		void onAccountCreated(UserDTO user);
	}

	public interface Display
	{
		HasClickHandlers getCreateButton();

		HasClickHandlers getCancelButton();

		Field<String> email();

		Field<String> password();

		Field<String> confirmPassword();

		Widget asWidget();
	}

	private final ServiceBus serviceBus;
	private final Display display;
	private final AppController.Display rootDisplay;
	private Callback callback;

	public CreateAccountPresenter(final ServiceBus serviceBus, final Display display,
			final AppController.Display rootDisplay)
	{
		super();
		this.serviceBus = serviceBus;
		this.display = display;
		this.rootDisplay = rootDisplay;

		bind();
	}

	public void bind()
	{
		final AppController.Display rootDisplay = this.rootDisplay;

		this.display.getCreateButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				rootDisplay.clearMessage();
				doCreateAccount();
			}
		});

		this.display.getCancelButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				clearMessages();
				CreateAccountPresenter.this.callback.onCancelCreateAccount();
			}
		});
	}

	@Override
	public void go(final HasWidgets container, final Callback callback)
	{
		this.callback = callback;
		container.clear();
		container.add(this.display.asWidget());
		this.display.email().setValue(null);
		this.display.password().setValue(null);
		this.display.confirmPassword().setValue(null);
	}

	private boolean validate(final Display display)
	{
		boolean result = true;

		display.email().clearErrors();
		final String email = display.email().getValue();
		if (email == null || email.isEmpty())
		{
			display.email().showError("Email is empty");
			result = false;
		}
		else if (email.length() > 256)
		{
			display.email().showError("Email is too long (max: 256)");
			result = false;
		}
		else if (email.indexOf('@') <= 0)
		{
			display.email().showError("Email has wrong format");
			result = false;
		}

		display.password().clearErrors();
		final String password = display.password().getValue();
		final String confirmPassword = display.confirmPassword().getValue();
		if (password == null || password.length() < 4)
		{
			display.password().showError("Password field must be at least 4 characters long");
			result = false;
		}
		else if (password.length() > 256)
		{
			display.password().showError("Password is too long (max: 256)");
			result = false;
		}
		else if (!password.equals(confirmPassword))
		{
			display.password().showError("Passwords are different");
			result = false;
		}
		return result;
	}

	private void clearMessages()
	{
		this.rootDisplay.clearMessage();
		this.display.email().clearErrors();
		this.display.password().clearErrors();
	}

	private void doCreateAccount()
	{
		final AppController.Display rootDisplay = this.rootDisplay;
		if (!validate(this.display))
		{
			return; // EARLY EXIT !!!
		}

		final String email = this.display.email().getValue();
		final String password = this.display.password().getValue();

		this.serviceBus.userService.createUser(new UserDTO(email, password), new MethodCallback<UserDTO>()
		{

			@Override
			public void onSuccess(final Method method, final UserDTO response)
			{
				CreateAccountPresenter.this.callback.onAccountCreated(response);
			}

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				rootDisplay.showError("Error creating account: check fields", method, exception);
			}
		});
	}
}
