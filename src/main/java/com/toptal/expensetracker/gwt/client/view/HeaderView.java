package com.toptal.expensetracker.gwt.client.view;

import org.fusesource.restygwt.client.Method;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.toptal.expensetracker.gwt.client.AppController;

public class HeaderView implements AppController.Display
{

	private final Button logoutButton;
	private final StringLabel emailLabel;
	private final FlowPanel headerPanel;
	private final RootPanel messageSlot;

	public HeaderView(final RootPanel headerSlot, final RootPanel messageSlot)
	{
		super();
		this.messageSlot = messageSlot;
		this.headerPanel = new FlowPanel();
		headerSlot.add(this.headerPanel);

		this.emailLabel = new StringLabel();
		this.headerPanel.add(this.emailLabel);

		this.logoutButton = new Button("Logout");
		this.headerPanel.add(this.logoutButton);

		this.headerPanel.setVisible(false);
	}

	@Override
	public HasClickHandlers getLogoutButton()
	{
		return this.logoutButton;
	}

	@Override
	public HasVisibility getHeaderVisibility()
	{
		return this.headerPanel;
	}

	@Override
	public TakesValue<String> currentEmail()
	{
		return this.emailLabel;
	}

	@Override
	public void showError(final String message)
	{
		final FlexTable table = new FlexTable();
		final Image icon = new Image();
		icon.setUrl("images/message-warn.png");
		table.setWidget(0, 0, icon);
		table.getCellFormatter().setWidth(0, 0, "40px");
		table.setWidget(0, 1, new Label(message));
		this.messageSlot.add(table);
	}

	@Override
	public void showError(final String message, final Method method, final Throwable exception)
	{
		final FlexTable table = new FlexTable();
		final Image icon = new Image();
		icon.setUrl("images/message-warn.png");
		table.setWidget(0, 0, icon);
		table.getCellFormatter().setWidth(0, 0, "40px");
		final Response response = method.getResponse();
		final HTML html = new HTML(message + "<br>Code: " + response.getStatusCode() + "<br>Text: "
				+ response.getText() + "<br>Exception: " + exception);
		table.setWidget(0, 1, html);
		this.messageSlot.add(table);
	}

	@Override
	public void showInfo(final String message)
	{
		final FlexTable table = new FlexTable();
		final Image icon = new Image();
		icon.setUrl("images/message-info.png");
		table.setWidget(0, 0, icon);
		table.getCellFormatter().setWidth(0, 0, "40px");
		table.setWidget(0, 1, new Label(message));
		this.messageSlot.add(table);
	}

	@Override
	public void showTick(final String message)
	{
		final FlexTable table = new FlexTable();
		final Image icon = new Image();
		icon.setUrl("images/message-tick.png");
		table.setWidget(0, 0, icon);
		table.getCellFormatter().setWidth(0, 0, "40px");
		table.setWidget(0, 1, new Label(message));
		this.messageSlot.add(table);
	}

	@Override
	public void clearMessage()
	{
		this.messageSlot.clear();
	}

}
