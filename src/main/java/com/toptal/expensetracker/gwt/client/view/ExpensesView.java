package com.toptal.expensetracker.gwt.client.view;

import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.data.ExpenseDTO;
import com.toptal.expensetracker.gwt.client.presenter.ExpensesPresenter;

public class ExpensesView extends Composite implements ExpensesPresenter.Display
{
	private final Button addButton;
	private final Button editButton;
	private final Button deleteButton;
	private final Button logoutButton;
	private final FlexTable expensesTable;
	private final FlexTable contentTable;

	public ExpensesView()
	{
		final DecoratorPanel contentTableDecorator = new DecoratorPanel();
		initWidget(contentTableDecorator);
		contentTableDecorator.setWidth("100%");
		contentTableDecorator.setWidth("18em");

		this.contentTable = new FlexTable();
		this.contentTable.setWidth("100%");
		this.contentTable.getCellFormatter().addStyleName(0, 0, "contacts-ListContainer");
		this.contentTable.getCellFormatter().setWidth(0, 0, "100%");
		this.contentTable.getFlexCellFormatter().setVerticalAlignment(0, 0, DockPanel.ALIGN_TOP);

		// Create the menu
		//
		final HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setBorderWidth(0);
		hPanel.setSpacing(0);
		hPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		this.addButton = new Button("Add");
		hPanel.add(this.addButton);
		this.editButton = new Button("Edit");
		hPanel.add(this.editButton);
		this.deleteButton = new Button("Delete");
		hPanel.add(this.deleteButton);
		this.logoutButton = new Button("Logout");
		hPanel.add(this.logoutButton);
		this.contentTable.getCellFormatter().addStyleName(0, 0, "contacts-ListMenu");
		this.contentTable.setWidget(0, 0, hPanel);

		// Create the contacts list
		//
		this.expensesTable = new FlexTable();
		this.expensesTable.setCellSpacing(0);
		this.expensesTable.setCellPadding(0);
		this.expensesTable.setWidth("100%");
		this.expensesTable.addStyleName("contacts-ListContents");
		this.expensesTable.getColumnFormatter().setWidth(0, "15px");
		this.contentTable.setWidget(1, 0, this.expensesTable);

		contentTableDecorator.add(this.contentTable);
	}

	@Override
	public HasClickHandlers getAddButton()
	{
		return this.addButton;
	}

	@Override
	public HasClickHandlers getEditButton()
	{
		return this.editButton;
	}

	@Override
	public HasClickHandlers getDeleteButton()
	{
		return this.deleteButton;
	}

	@Override
	public HasClickHandlers getLogoutButton()
	{
		return this.logoutButton;
	}

	@Override
	public void setData(final List<ExpenseDTO> data)
	{
		this.expensesTable.removeAllRows();

		for (int i = 0; i < data.size(); ++i)
		{
			this.expensesTable.setWidget(i, 0, new CheckBox());
			final ExpenseDTO expense = data.get(i);
			this.expensesTable.setText(i, 1, expense.toString());
		}
	}

	@Override
	public int getSelectedRow()
	{
		for (int i = 0; i < this.expensesTable.getRowCount(); ++i)
		{
			final CheckBox checkBox = (CheckBox) this.expensesTable.getWidget(i, 0);
			if (checkBox.getValue())
			{
				return i;
			}
		}

		return -1;
	}

	@Override
	public Widget asWidget()
	{
		return this;
	}
}
