package com.toptal.expensetracker.gwt.client;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.toptal.expensetracker.gwt.client.data.UserDTO;
import com.toptal.expensetracker.gwt.client.event.AccountCreatedEvent;
import com.toptal.expensetracker.gwt.client.event.CreateAccountCancelledEvent;
import com.toptal.expensetracker.gwt.client.event.CreateAccountEvent;
import com.toptal.expensetracker.gwt.client.event.UserLogOutEvent;
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
	private final ServiceBus serviceBus;
	private final HandlerManager eventBus;
	private HasWidgets container;

	private LoginFormPresenter loginFormPresenter;
	private CreateAccountPresenter createAccPresenter;

	public AppController(final ServiceBus serviceBus, final HandlerManager eventBus)
	{
		super();
		this.serviceBus = serviceBus;
		this.eventBus = eventBus;

		bind();
	}

	public void bind()
	{
		this.eventBus.addHandler(UserLoggedInEvent.TYPE, new UserLoggedInEvent.Handler()
		{

			@Override
			public void onUserLogggedIn(final UserLoggedInEvent event)
			{
				gotoExpenses(event.getUser());
			}
		});

		this.eventBus.addHandler(UserLogOutEvent.TYPE, new UserLogOutEvent.Handler()
		{

			@Override
			public void onUserLogOut(final UserLogOutEvent event)
			{
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

		this.loginFormPresenter = new LoginFormPresenter(serviceBus, eventBus, new LoginFormView());
		this.createAccPresenter = new CreateAccountPresenter(serviceBus, eventBus, new CreateAccountView());

		fetchCurretUser();
	}

	private void gotoExpenses(final UserDTO user)
	{
		final ExpensesPresenter expensesPresenter = new ExpensesPresenter(this.serviceBus, this.eventBus,
				new ExpensesView(), user);
		expensesPresenter.go(this.container);
	}

	private void gotoLoginForm(final String lastEmail)
	{
		if (lastEmail != null)
		{
			AppController.this.loginFormPresenter.setLastEmail(lastEmail);
		}
		AppController.this.loginFormPresenter.go(this.container);
	}

	private void gotoCreateAccountForm()
	{
		AppController.this.createAccPresenter.go(this.container);
	}

	private void doLogout()
	{
		this.serviceBus.userService.logout(new MethodCallback<Void>()
		{

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				fetchCurretUser();
			}

			@Override
			public void onSuccess(final Method method, final Void response)
			{
				gotoLoginForm(null);
			}
		});
	}

	private void fetchCurretUser()
	{
		this.serviceBus.userService.getCurrentUser(new MethodCallback<UserDTO>()
		{

			@Override
			public void onSuccess(final Method method, final UserDTO response)
			{
				final String email = response.email;
				if (email != null && email.length() > 0 && email.indexOf('@') > 0)
				{
					gotoExpenses(response);
				}
				else
				{
					gotoLoginForm(null);
				}
			}

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				Window.alert("Error when getting current user");
				gotoLoginForm(null);
			}
		});
	}
}
