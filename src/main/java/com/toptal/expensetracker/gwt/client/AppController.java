package com.toptal.expensetracker.gwt.client;

import java.util.Date;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.HasWidgets;
import com.toptal.expensetracker.gwt.client.dto.UserDTO;
import com.toptal.expensetracker.gwt.client.event.AccountCreatedEvent;
import com.toptal.expensetracker.gwt.client.event.CreateAccountCancelledEvent;
import com.toptal.expensetracker.gwt.client.event.CreateAccountEvent;
import com.toptal.expensetracker.gwt.client.event.UserLoggedInEvent;
import com.toptal.expensetracker.gwt.client.presenter.CreateAccountPresenter;
import com.toptal.expensetracker.gwt.client.presenter.ExpensesPresenter;
import com.toptal.expensetracker.gwt.client.presenter.LoginFormPresenter;
import com.toptal.expensetracker.gwt.client.presenter.Presenter;
import com.toptal.expensetracker.gwt.client.view.CreateAccountView;
import com.toptal.expensetracker.gwt.client.view.ExpensesView;
import com.toptal.expensetracker.gwt.client.view.LoginFormView;

public class AppController implements Presenter
{
	public interface Display
	{
		HasClickHandlers getLogoutButton();

		HasVisibility getHeaderVisibility();

		String getCurrentEmail();

		void setCurrentEmail(String email);

		void showError(String message);

		void showError(String string, Method method, Throwable exception);

		void showInfo(String message);

		void showTick(String message);

		/** Call it only in user action handlers (e.g. click handlers). */
		void clearMessage();
	}

	private final ServiceBus serviceBus;
	private final HandlerManager eventBus;
	private final Display rootDisplay;
	private HasWidgets container;

	private LoginFormPresenter loginFormPresenter;
	private CreateAccountPresenter createAccPresenter;

	public AppController(final ServiceBus serviceBus, final HandlerManager eventBus, final Display display)
	{
		super();
		this.serviceBus = serviceBus;
		this.eventBus = eventBus;
		this.rootDisplay = display;

		bind();
	}

	public void bind()
	{
		final Display rootDisplay = this.rootDisplay;
		this.eventBus.addHandler(UserLoggedInEvent.TYPE, new UserLoggedInEvent.Handler()
		{

			@Override
			public void onUserLogggedIn(final UserLoggedInEvent event)
			{

				doAfterLogin(event.getUser());
			}
		});

		this.rootDisplay.getLogoutButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				rootDisplay.clearMessage();
				doLogout();
			}
		});

		this.eventBus.addHandler(CreateAccountEvent.TYPE, new CreateAccountEvent.Handler()
		{

			@Override
			public void onCreateAccount(final CreateAccountEvent event)
			{
				gotoCreateAccountForm();
			}
		});

		this.eventBus.addHandler(CreateAccountCancelledEvent.TYPE, new CreateAccountCancelledEvent.Handler()
		{

			@Override
			public void onCreateAccountCancelled(final CreateAccountCancelledEvent event)
			{
				gotoLoginForm(null);
			}
		});

		this.eventBus.addHandler(AccountCreatedEvent.TYPE, new AccountCreatedEvent.Handler()
		{

			@Override
			public void onAccountCreated(final AccountCreatedEvent event)
			{
				gotoLoginForm(event.getUser().email);
			}
		});
	}

	@Override
	public void go(final HasWidgets container)
	{
		this.container = container;

		final ServiceBus serviceBus = this.serviceBus;
		final HandlerManager eventBus = this.eventBus;

		this.loginFormPresenter = new LoginFormPresenter(serviceBus, eventBus, new LoginFormView(), this.rootDisplay);
		this.createAccPresenter = new CreateAccountPresenter(serviceBus, eventBus, new CreateAccountView(),
				this.rootDisplay);

		fetchCurrentUser();
	}

	private void gotoLoginForm(final String lastEmail)
	{
		if (lastEmail != null)
		{
			this.loginFormPresenter.setLastEmail(lastEmail);
		}
		this.loginFormPresenter.go(this.container);
	}

	private void gotoCreateAccountForm()
	{
		this.createAccPresenter.go(this.container);
	}

	private void doAfterLogin(final UserDTO user)
	{
		this.rootDisplay.getHeaderVisibility().setVisible(true);
		this.rootDisplay.setCurrentEmail(user.email);

		final ExpensesView expensesView = new ExpensesView(new Date());
		final ExpensesPresenter expensesPresenter = new ExpensesPresenter(this.serviceBus, this.eventBus, expensesView,
				this.rootDisplay, user);
		expensesPresenter.go(this.container);
	}

	private void doLogout()
	{
		final Display rootDisplay = this.rootDisplay;
		rootDisplay.getHeaderVisibility().setVisible(false);
		final String lastEmail = rootDisplay.getCurrentEmail();
		rootDisplay.setCurrentEmail(null);

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
}
