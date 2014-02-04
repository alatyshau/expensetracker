package com.toptal.expensetracker.gwt.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
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

public class ExpensesView extends Composite implements ExpensesPresenter.Display, RemoveExpenseEvent.HasHandlers,
		EditExpenseEvent.HasHandlers
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
	private final Set<ExpenseDTO> selectedExpenses = new HashSet<ExpenseDTO>();

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
		this.deleteButton.setEnabled(false);
		this.deleteButton.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				fireEvent(new RemoveExpenseEvent(ExpensesView.this.selectedExpenses));
			}
		});

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
	public Widget asWidget()
	{
		return this;
	}

	@Override
	public EditExpenseEvent.HasHandlers getEditExpenseHandlers()
	{
		return this;
	}

	@Override
	public RemoveExpenseEvent.HasHandlers getRemoveExpenseHandlers()
	{
		return this;
	}

	@Override
	public HandlerRegistration addEditExpenseHandler(final EditExpenseEvent.Handler handler)
	{
		return addHandler(handler, EditExpenseEvent.TYPE);
	}

	@Override
	public HandlerRegistration addRemoveExpenseHandler(final RemoveExpenseEvent.Handler handler)
	{
		return addHandler(handler, RemoveExpenseEvent.TYPE);
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

		for (final ExpenseDTO dto : data)
		{
			final Expense expense = new Expense(dto);
			final String expenseID = dto.expenseID;
			final int dayNum = DateUtil.dayOfWeek(expense.dateTime);
			final int weekNum = DateUtil.getWeeksBetween(expense.dateTime, today, todayDay);
			expensesCoords.put(expenseID, new int[] { weekNum, dayNum });
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
			weekPanel.setHeader(new Label(week.toString()));
			final FlowPanel daysPanel = new FlowPanel();

			final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_FULL);
			final DateTimeFormat timeFormat = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);

			for (int d = 6; d >= 0; d--)
			{
				final Day day = week.days[d];
				final List<Expense> dayExpenses = day.expenses;
				if (!dayExpenses.isEmpty())
				{
					final FlexTable dayTable = new FlexTable();
					dayTable.setWidth("100%");
					dayTable.setStylePrimaryName("day-table");
					day.table = dayTable;
					final String dayTitle = dateFormat.format(dayExpenses.get(0).dateTime);
					dayTable.setText(0, 0, dayTitle);
					final FlexCellFormatter flexCellFormatter = dayTable.getFlexCellFormatter();
					flexCellFormatter.setColSpan(0, 0, 4);
					flexCellFormatter.setStylePrimaryName(0, 0, "day-table-header");
					int i = 1;
					for (final Expense expense : dayExpenses)
					{
						final Date dateTime = expense.dateTime;
						final ExpenseDTO dto = expense.dto;
						expense.checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>()
						{
							@Override
							public void onValueChange(final ValueChangeEvent<Boolean> event)
							{
								final Boolean checked = event.getValue();
								final Set<ExpenseDTO> selectedExpenses = ExpensesView.this.selectedExpenses;
								if (checked != null && checked)
								{
									selectedExpenses.add(expense.dto);
								}
								else
								{
									selectedExpenses.remove(expense.dto);
								}
								ExpensesView.this.deleteButton.setEnabled(!selectedExpenses.isEmpty());
							}
						});
						dayTable.setWidget(i, 0, expense.checkBox);
						final Label descLabel = new Label(dto.description);
						if (dto.comment != null)
						{
							descLabel.setTitle(dto.comment);
						}
						descLabel.addDoubleClickHandler(new DoubleClickHandler()
						{
							@Override
							public void onDoubleClick(final DoubleClickEvent event)
							{
								fireEvent(new EditExpenseEvent(dto));
							}
						});
						dayTable.setText(i, 1, timeFormat.format(dateTime));
						dayTable.setWidget(i, 2, descLabel);
						dayTable.setWidget(i, 3, new Label(NumberFormat.getCurrencyFormat().format(dto.amount)));
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
		Window.alert("Added Expense: " + expense.expenseID);
	}

	@Override
	public void updateExpense(final ExpenseDTO expense)
	{
		Window.alert("Updated Expense: " + expense.expenseID);
	}

	@Override
	public void removeExpenses(final Collection<String> expenseIDs)
	{
		Window.alert("Removed Expenses: " + expenseIDs);
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

		@Override
		public String toString()
		{
			final int num = this.num;
			if (num == 0)
			{
				return "This week";
			}
			if (num == -1)
			{
				return "Previous week";
			}
			if (num == 1)
			{
				return "Next week";
			}
			if (num < -1)
			{
				return "" + (-num) + " weeks ago";
			}
			return "In " + num + " weeks";
		}
	}

	private static class Day
	{
		public final int num;
		public final List<Expense> expenses = new ArrayList<Expense>();
		public FlexTable table;

		public Day(final int num)
		{
			super();
			this.num = num;
		}

	}

	private static class Expense
	{
		public final CheckBox checkBox = new CheckBox();
		public final ExpenseDTO dto;
		public final Date dateTime;

		public Expense(final ExpenseDTO dto)
		{
			super();
			this.dto = dto;
			this.dateTime = new Date(dto.dateTime);
		}

	}
}
