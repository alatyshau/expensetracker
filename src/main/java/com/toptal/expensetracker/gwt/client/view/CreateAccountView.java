package com.toptal.expensetracker.gwt.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.presenter.CreateAccountPresenter;

public class CreateAccountView extends Composite implements CreateAccountPresenter.Display
{
	private final TextBox email;
	private final PasswordTextBox password;
	private final PasswordTextBox confirmPassword;
	private final FlexTable detailsTable;
	private final Button createButton;
	private final Button cancelButton;

	public CreateAccountView()
	{
		final DecoratorPanel contentDetailsDecorator = new DecoratorPanel();
		contentDetailsDecorator.setWidth("18em");
		initWidget(contentDetailsDecorator);

		final VerticalPanel contentDetailsPanel = new VerticalPanel();
		contentDetailsPanel.setWidth("100%");

		this.detailsTable = new FlexTable();
		this.detailsTable.setCellSpacing(0);
		this.detailsTable.setWidth("100%");
		this.detailsTable.addStyleName("contacts-ListContainer");
		this.detailsTable.getColumnFormatter().addStyleName(1, "add-contact-input");
		this.email = new TextBox();
		this.password = new PasswordTextBox();
		this.confirmPassword = new PasswordTextBox();
		initDetailsTable();
		contentDetailsPanel.add(this.detailsTable);

		final HorizontalPanel menuPanel = new HorizontalPanel();
		this.createButton = new Button("Create");
		this.cancelButton = new Button("Cancel");
		menuPanel.add(this.createButton);
		menuPanel.add(this.cancelButton);
		contentDetailsPanel.add(menuPanel);
		contentDetailsDecorator.add(contentDetailsPanel);
	}

	private void initDetailsTable()
	{
		this.detailsTable.setWidget(0, 0, new Label("Email"));
		this.detailsTable.setWidget(0, 1, this.email);
		this.detailsTable.setWidget(1, 0, new Label("Password"));
		this.detailsTable.setWidget(1, 1, this.password);
		this.detailsTable.setWidget(2, 0, new Label("Confirm password"));
		this.detailsTable.setWidget(2, 1, this.confirmPassword);
		this.email.setFocus(true);
	}

	@Override
	public HasClickHandlers getCreateButton()
	{
		return this.createButton;
	}

	@Override
	public HasClickHandlers getCancelButton()
	{
		return this.cancelButton;
	}

	@Override
	public HasValue<String> getEmail()
	{
		return this.email;
	}

	@Override
	public HasValue<String> getPassword()
	{
		return this.password;
	}

	@Override
	public HasValue<String> getConfirmPassword()
	{
		return this.confirmPassword;
	}

	@Override
	public Widget asWidget()
	{
		return this;
	}
}
