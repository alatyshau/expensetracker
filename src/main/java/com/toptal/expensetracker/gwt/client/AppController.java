package com.toptal.expensetracker.gwt.client;

import java.util.Date;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.HasWidgets;
import com.toptal.expensetracker.gwt.client.dto.UserDTO;
import com.toptal.expensetracker.gwt.client.presenter.CreateAccountPresenter;
import com.toptal.expensetracker.gwt.client.presenter.ExpensesPresenter;
import com.toptal.expensetracker.gwt.client.presenter.LoginFormPresenter;
import com.toptal.expensetracker.gwt.client.presenter.Presenter;
import com.toptal.expensetracker.gwt.client.view.CreateAccountView;
import com.toptal.expensetracker.gwt.client.view.ExpensesView;
import com.toptal.expensetracker.gwt.client.view.LoginFormView;

public class AppController implements Presenter<Void>, LoginFormPresenter.Callback, CreateAccountPresenter.Callback
{
	public interface Display
	{
		HasClickHandlers getLogoutButton();

		HasVisibility getHeaderVisibility();

		TakesValue<String> currentEmail();

		void showError(String message);

		void showError(String string, Method method, Throwable exception);

		void showInfo(String message);

		void showTick(String message);

		/** Call it only in user action handlers (e.g. click handlers). */
		void clearMessage();
	}

	private final ServiceBus serviceBus;
	private final Display rootDisplay;
	private HasWidgets container;

	private LoginFormPresenter loginFormPresenter;
	private CreateAccountPresenter createAccPresenter;

	public AppController(final ServiceBus serviceBus, final Display display)
	{
		super();
		this.serviceBus = serviceBus;
		this.rootDisplay = display;

		bind();
	}

	public void bind()
	{
		final Display rootDisplay = this.rootDisplay;
		rootDisplay.getLogoutButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				rootDisplay.clearMessage();
				doLogout();
			}
		});
	}

	@Override
	public void go(final HasWidgets container, final Void callback)
	{
		this.container = container;

		final ServiceBus serviceBus = this.serviceBus;

		this.loginFormPresenter = new LoginFormPresenter(serviceBus, new LoginFormView(), this.rootDisplay);
		this.createAccPresenter = new CreateAccountPresenter(serviceBus, new CreateAccountView(), this.rootDisplay);

		fetchCurrentUser();
	}

	private void gotoLoginForm(final String lastEmail)
	{
		if (lastEmail != null)
		{
			this.loginFormPresenter.setLastEmail(lastEmail);
		}
		this.loginFormPresenter.go(this.container, this);
	}

	private void gotoCreateAccountForm()
	{
		this.createAccPresenter.go(this.container, this);
	}

	private void doAfterLogin(final UserDTO user)
	{
		this.rootDisplay.getHeaderVisibility().setVisible(true);
		this.rootDisplay.currentEmail().setValue(user.email);

		final Date today = new Date();
		final ExpensesView expensesView = new ExpensesView(today);
		final ExpensesPresenter expensesPresenter = new ExpensesPresenter(this.serviceBus, expensesView,
				this.rootDisplay, user, today);
		expensesPresenter.go(this.container, null);
	}

	private void doLogout()
	{
		final Display rootDisplay = this.rootDisplay;
		rootDisplay.getHeaderVisibility().setVisible(false);
		final String lastEmail = rootDisplay.currentEmail().getValue();
		rootDisplay.currentEmail().setValue(null);

		this.serviceBus.userService.logout(new MethodCallback<Void>()
		{

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				rootDisplay.showError("Server error during logout", method, exception);
				fetchCurrentUser();
			}

			@Override
			public void onSuccess(final Method method, final Void response)
			{
				gotoLoginForm(lastEmail);
			}
		});
	}

	private void fetchCurrentUser()
	{
		final Display rootDisplay = this.rootDisplay;
		this.serviceBus.userService.getCurrentUser(new MethodCallback<UserDTO>()
		{

			@Override
			public void onSuccess(final Method method, final UserDTO response)
			{
				final String email = response.email;
				if (email != null && email.length() > 0 && email.indexOf('@') > 0)
				{
					doAfterLogin(response);
				}
				else
				{
					gotoLoginForm(null);
				}
			}

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				rootDisplay.showError("Error when getting current user", method, exception);
				gotoLoginForm(null);
			}
		});
	}

	@Override
	public void onCancelCreateAccount()
	{
		gotoLoginForm(null);
	}

	@Override
	public void onAccountCreated(final UserDTO user)
	{
		gotoLoginForm(user.email);
	}

	@Override
	public void onCreateAccount()
	{
		gotoCreateAccountForm();
	}

	@Override
	public void onUserLoggedIn(final UserDTO user)
	{
		doAfterLogin(user);
	}
}
