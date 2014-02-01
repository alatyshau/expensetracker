package com.toptal.expensetracker.gwt.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.expensetracker.gwt.client.dto.ExpenseDTO;
import com.toptal.expensetracker.gwt.client.event.EditExpenseEvent;
import com.toptal.expensetracker.gwt.client.event.RemoveExpenseEvent;
import com.toptal.expensetracker.gwt.client.presenter.ExpensesPresenter;
import com.toptal.expensetracker.gwt.client.util.DateUtil;

public class ExpensesView extends Composite implements ExpensesPresenter.Display
{
	private final Date today;
	private final Button addButton;
	private final Button deleteButton;

	private final TextBox filterTextBox;
	private final Button applyFilterButton;
	private final Button clearFilterButton;

	private final FlowPanel weeksPanel;

	private final SortedMap<Integer, Week> weeks;
	private final Map<String, int[]> expensesCoords = new HashMap<String, int[]>();

	public ExpensesView(final Date today)
	{
		this.today = today;
		this.weeks = new TreeMap<Integer, Week>(new Comparator<Integer>()
		{
			@Override
			public int compare(final Integer o1, final Integer o2)
			{
				return o2.compareTo(o1);
			}
		});

		final FlowPanel rootPanel = new FlowPanel();
		rootPanel.setWidth("100%");
		initWidget(rootPanel);

		final FlexTable menuPanel = new FlexTable();
		menuPanel.setWidth("100%");

		final FlowPanel leftPanel = new FlowPanel();
		final FlowPanel rightPanel = new FlowPanel();
		menuPanel.setWidget(0, 0, leftPanel);
		menuPanel.setWidget(0, 1, rightPanel);

		final CellFormatter cellFormatter = menuPanel.getCellFormatter();
		cellFormatter.setAlignment(0, 0, HorizontalAlignmentConstant.startOf(Direction.DEFAULT),
				HasVerticalAlignment.ALIGN_MIDDLE);
		cellFormatter.setAlignment(0, 1, HorizontalAlignmentConstant.endOf(Direction.DEFAULT),
				HasVerticalAlignment.ALIGN_MIDDLE);

		this.filterTextBox = new TextBox();
		leftPanel.add(this.filterTextBox);
		this.applyFilterButton = new Button("Filter");
		leftPanel.add(this.applyFilterButton);
		this.clearFilterButton = new Button("Clear Filter");
		leftPanel.add(this.clearFilterButton);

		this.addButton = new Button("Add Expense");
		rightPanel.add(this.addButton);
		this.deleteButton = new Button("Delete Expenses");
		rightPanel.add(this.deleteButton);

		this.weeksPanel = new FlowPanel();
		this.weeksPanel.setWidth("100%");
		rootPanel.add(menuPanel);
		rootPanel.add(this.weeksPanel);
	}

	@Override
	public HasClickHandlers getAddButton()
	{
		return this.addButton;
	}

	@Override
	public HasClickHandlers getDeleteButton()
	{
		return this.deleteButton;
	}

	@Override
	public Widget asWidget()
	{
		return this;
	}

	@Override
	public EditExpenseEvent.HasHandlers getEditExpenseHandlers()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoveExpenseEvent.HasHandlers getRemoveExpenseHandlers()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setData(final Collection<ExpenseDTO> data)
	{
		final SortedMap<Integer, Week> weeks = this.weeks;
		final Map<String, int[]> expensesCoords = this.expensesCoords;
		// TODO remove previous widgets

		// build new widgets
		final Date today = this.today;
		final int todayDay = DateUtil.dayOfWeek(today);

		for (final ExpenseDTO expense : data)
		{
			final String expenseId = expense.expenseID;
			final Date dateTime = new Date(expense.dateTime);
			final int dayNum = DateUtil.dayOfWeek(dateTime);
			final int weekNum = DateUtil.getWeeksBetween(dateTime, today, todayDay);
			expensesCoords.put(expenseId, new int[] { weekNum, dayNum });
			Week currWeek = weeks.get(weekNum);
			if (currWeek == null)
			{
				currWeek = new Week(weekNum);
				weeks.put(weekNum, currWeek);
			}
			currWeek.days[dayNum].expenses.add(expense);
		}

		boolean atLeastOneOpen = false;
		for (final Week week : weeks.values())
		{
			final DisclosurePanel weekPanel = new DisclosurePanel();
			weekPanel.setWidth("100%");
			weekPanel.setStylePrimaryName("week-panel");
			if (week.num >= 0 || !atLeastOneOpen)
			{
				atLeastOneOpen = true;
				weekPanel.setOpen(true);
			}
			else
			{
				weekPanel.setOpen(false);
			}

			week.panel = weekPanel;
			weekPanel.setHeader(new Label("Week #" + week.num));
			final FlowPanel daysPanel = new FlowPanel();

			final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_FULL);
			final DateTimeFormat timeFormat = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);

			for (int d = 6; d >= 0; d--)
			{
				final Day day = week.days[d];
				final List<ExpenseDTO> dayExpenses = day.expenses;
				if (!dayExpenses.isEmpty())
				{
					final FlexTable dayTable = new FlexTable();
					dayTable.setWidth("100%");
					dayTable.setStylePrimaryName("day-table");
					day.table = dayTable;
					final String dayTitle = dateFormat.format(new Date(dayExpenses.get(0).dateTime));
					dayTable.setText(0, 0, dayTitle);
					final FlexCellFormatter flexCellFormatter = dayTable.getFlexCellFormatter();
					flexCellFormatter.setColSpan(0, 0, 4);
					flexCellFormatter.setStylePrimaryName(0, 0, "day-table-header");
					int i = 1;
					for (final ExpenseDTO expense : dayExpenses)
					{
						final Date dateTime = new Date(expense.dateTime);
						dayTable.setWidget(i, 0, new CheckBox());
						final Label descLabel = new Label(expense.description);
						if (expense.comment != null)
						{
							descLabel.setTitle(expense.comment);
						}
						descLabel.addDoubleClickHandler(new DoubleClickHandler()
						{
							@Override
							public void onDoubleClick(final DoubleClickEvent event)
							{
								final EditExpenseEvent editEvent = new EditExpenseEvent(expense);
								Window.alert("EditEvent: " + expense.expenseID + " -- " + editEvent);
								// fire Edit event
							}
						});
						dayTable.setText(i, 1, timeFormat.format(dateTime));
						dayTable.setWidget(i, 2, descLabel);
						dayTable.setWidget(i, 3, new Label(NumberFormat.getCurrencyFormat().format(expense.amount)));
						flexCellFormatter.setWidth(i, 0, "40px");
						flexCellFormatter.setWidth(i, 1, "80px");
						flexCellFormatter.setWidth(i, 3, "120px");
						i++;
					}
					daysPanel.add(dayTable);
				}
			}

			weekPanel.setContent(daysPanel);
			this.weeksPanel.add(weekPanel);
		}
	}

	@Override
	public void addExpense(final ExpenseDTO expense)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateExpense(final ExpenseDTO expense)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void removeExpenses(final Collection<String> expenseIds)
	{
		// TODO Auto-generated method stub

	}

	private static class Week
	{
		public final int num;
		public final Day[] days = // seven days of week
		new Day[] { new Day(0), new Day(1), new Day(2), new Day(3), new Day(4), new Day(5), new Day(6) };
		public DisclosurePanel panel;

		public Week(final int num)
		{
			super();
			this.num = num;
		}

	}

	private static class Day
	{
		public final int num;
		public final List<ExpenseDTO> expenses = new ArrayList<ExpenseDTO>();
		public FlexTable table;

		public Day(final int num)
		{
			super();
			this.num = num;
		}

	}

	@Override
	public Collection<String> getSelectedExpenses()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
