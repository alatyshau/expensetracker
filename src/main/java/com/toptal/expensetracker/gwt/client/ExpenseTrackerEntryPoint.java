package com.toptal.expensetracker.gwt.client;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.toptal.expensetracker.gwt.client.data.ExpenseDTO;
import com.toptal.expensetracker.gwt.client.services.ExpenseTrackerService;

public class ExpenseTrackerEntryPoint implements EntryPoint
{

	@Override
	public void onModuleLoad()
	{
		final ExpenseTrackerService service = GWT.create(ExpenseTrackerService.class);
		final Resource resource = new Resource(GWT.getHostPageBaseURL() + "api/expenses");
		((RestServiceProxy) service).setResource(resource);

		service.getExpenses(new MethodCallback<List<ExpenseDTO>>()
		{
			@Override
			public void onSuccess(final Method method, final List<ExpenseDTO> receipt)
			{
				RootPanel.get().add(new Label("got receipt: " + receipt.get(0).description));
			}

			@Override
			public void onFailure(final Method method, final Throwable exception)
			{
				Window.alert("Error: " + exception);
			}
		});
	}

}
