package com.toptal.expensetracker.gwt.client;

import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.toptal.expensetracker.gwt.client.services.ExpenseTrackerService;
import com.toptal.expensetracker.gwt.client.services.UserService;
import com.toptal.expensetracker.gwt.client.view.HeaderView;

public class ExpenseTrackerEntryPoint implements EntryPoint
{

	@Override
	public void onModuleLoad()
	{
		final Resource resource = new Resource(GWT.getHostPageBaseURL() + "api");
		final ServiceBus serviceBus = new ServiceBus();
		// final Dispatcher dispatcher = new Dispatcher()
		// {
		// @Override
		// public Request send(final Method method, final RequestBuilder
		// builder) throws RequestException
		// {
		// Window.alert("Sending http request: " + builder.getHTTPMethod() + " "
		// + builder.getUrl() + " ,timeout:"
		// + builder.getTimeoutMillis());
		// return builder.send();
		// }
		// };

		serviceBus.expenseTrackerService = GWT.create(ExpenseTrackerService.class);
		((RestServiceProxy) serviceBus.expenseTrackerService).setResource(resource);
		// ((RestServiceProxy)
		// serviceBus.expenseTrackerService).setDispatcher(dispatcher);

		serviceBus.userService = GWT.create(UserService.class);
		((RestServiceProxy) serviceBus.userService).setResource(resource);
		// ((RestServiceProxy)
		// serviceBus.userService).setDispatcher(dispatcher);

		final HeaderView headerView = new HeaderView(RootPanel.get("slot-header"), RootPanel.get("slot-message"));

		final AppController appController = new AppController(serviceBus, headerView);
		appController.go(RootPanel.get("slot-body"), null);
	}

}
