package com.toptal.expensetracker.gwt.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Label;
import com.toptal.expensetracker.gwt.client.AppController;

public class HeaderView extends Composite implements AppController.Display
{

	private final Button logoutButton;
	private final Label emailLabel;

	public HeaderView()
	{
		super();
		final FlowPanel flowPanel = new FlowPanel();
		initWidget(flowPanel);

		this.emailLabel = new Label("");
		flowPanel.add(this.emailLabel);

		this.logoutButton = new Button("Logout");
		flowPanel.add(this.logoutButton);

		setVisible(false);
	}

	@Override
	public HasClickHandlers getLogoutButton()
	{
		return this.logoutButton;
	}

	@Override
	public HasVisibility getVisibilityControl()
	{
		return this;
	}

	@Override
	public void setCurrentEmail(final String email)
	{
		if (email != null)
		{
			this.emailLabel.setText(email);
		}
		else
		{
			this.emailLabel.setText("");
		}
	}

	@Override
	public String getCurrentEmail()
	{
		return this.emailLabel.getText();
	}

}
