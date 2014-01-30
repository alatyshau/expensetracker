package com.toptal.expensetracker.gwt.client;

import java.util.Date;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.HasWidgets;
import com.toptal.expensetracker.gwt.client.data.UserDTO;
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

		HasVisibility getVisibilityControl();

		String getCurrentEmail();

		void setCurrentEmail(String email);
	}

	private final ServiceBus serviceBus;
	private final HandlerManager eventBus;
	private final Display display;
	private HasWidgets container;

	private LoginFormPresenter loginFormPresenter;
	private CreateAccountPresenter createAccPresenter;

	public AppController(final ServiceBus serviceBus, final HandlerManager eventBus, final Display display)
	{
		super();
		this.serviceBus = serviceBus;
		this.eventBus = eventBus;
		this.display = display;

		bind();
	}

	public void bind()
	{
		this.eventBus.addHandler(UserLoggedInEvent.TYPE, new UserLoggedInEvent.Handler()
		{

			@Override
			public void onUserLogggedIn(final UserLoggedInEvent event)
			{
				doAfterLogin(event.getUser());
			}
		});

		this.display.getLogoutButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
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

		fetchCurrentUser();
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

	private void doAfterLogin(final UserDTO user)
	{
		this.display.getVisibilityControl().setVisible(true);
		this.display.setCurrentEmail(user.email);

		final ExpensesView expensesView = new ExpensesView(new Date());
		final ExpensesPresenter expensesPresenter = new ExpensesPresenter(this.serviceBus, this.eventBus, expensesView,
				user);
		expensesPresenter.go(this.container);

		// final ServiceBus serviceBus = this.serviceBus;
		// final HandlerManager eventBus = this.eventBus;
		// final HasWidgets container = this.container;
		// this.serviceBus.userService.today(new MethodCallback<String>()
		// {
		//
		// @Override
		// public void onSuccess(final Method method, final String response)
		// {
		// final ExpensesPresenter expensesPresenter = new
		// ExpensesPresenter(serviceBus, eventBus,
		// new ExpensesView(new Date()), user);
		// expensesPresenter.go(container);
		// }
		//
		// @Override
		// public void onFailure(final Method method, final Throwable exception)
		// {
		// Window.alert("Error when communicating with server\n" +
		// String.valueOf(exception));
		// gotoLoginForm(user.email);
		// }
		// });
	}

	private void doLogout()
	{
		this.display.getVisibilityControl().setVisible(false);
		final String lastEmail = this.display.getCurrentEmail();
		this.display.setCurrentEmail(null);

		this.serviceBus.userService.logout(new MethodCallback<Void>()
		{

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
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
				Window.alert("Error when getting current user\n" + String.valueOf(exception));
				gotoLoginForm(null);
			}
		});
	}
}
