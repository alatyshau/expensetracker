package com.toptal.expensetracker.gwt.client;

import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
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

		serviceBus.expenseTrackerService = GWT.create(ExpenseTrackerService.class);
		((RestServiceProxy) serviceBus.expenseTrackerService).setResource(resource);

		serviceBus.userService = GWT.create(UserService.class);
		((RestServiceProxy) serviceBus.userService).setResource(resource);

		final HandlerManager eventBus = new HandlerManager(null);

		final HeaderView headerView = new HeaderView();
		RootPanel.get("slot-header").add(headerView);

		final AppController appController = new AppController(serviceBus, eventBus, headerView);
		appController.go(RootPanel.get("slot-body"));
	}

}
