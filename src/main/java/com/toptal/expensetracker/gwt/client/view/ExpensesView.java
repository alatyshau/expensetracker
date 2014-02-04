package com.toptal.expensetracker.gwt.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.github.gwtbootstrap.client.ui.base.DivWidget;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
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
import com.toptal.expensetracker.gwt.client.util.Print;
import com.toptal.expensetracker.gwt.client.util.StringUtil;

public class ExpensesView extends Composite implements ExpensesPresenter.Display, RemoveExpenseEvent.HasHandlers,
		EditExpenseEvent.HasHandlers
{
	private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_FULL);
	private static final DateTimeFormat timeFormat = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);
	private static final NumberFormat currencyFormat = NumberFormat.getCurrencyFormat();

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

	private final DivWidget filterDiv;

	private String currentFilter;

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
		this.applyFilterButton.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				applyFilter(ExpensesView.this.filterTextBox.getValue());
			}
		});
		leftPanel.add(this.applyFilterButton);
		this.clearFilterButton = new Button("Clear Filter");
		this.clearFilterButton.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				applyFilter(null);
			}
		});
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
		final Button printButton = new Button("Print");
		printButton.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				final SortedMap<Integer, Week> weeks = ExpensesView.this.weeks;
				final Grid grid = new Grid(weeks.size() + 1, 3);
				grid.setBorderWidth(1);
				grid.setHTML(0, 0, "<b>Week</b>");
				grid.setHTML(0, 1, "<b>Total</b>");
				grid.setHTML(0, 2, "<b>Daily average</b>");
				int rowNum = 1;
				for (final Week week : weeks.values())
				{
					double sum = 0d;
					for (final Day day : week.days)
					{
						for (final Expense expense : day.expenses)
						{
							sum += expense.dto.amount;
						}
					}
					grid.setText(rowNum, 0, week.toString());
					grid.setText(rowNum, 1, currencyFormat.format(sum));
					grid.setText(rowNum, 2, currencyFormat.format(sum / 7));
					rowNum++;
				}
				Print.it(grid);
			}
		});
		rightPanel.add(printButton);

		this.weeksPanel = new FlowPanel();
		this.weeksPanel.setWidth("100%");
		rootPanel.add(menuPanel);
		this.filterDiv = new DivWidget();
		rootPanel.add(this.filterDiv);
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
		applyFilter(null);
		final SortedMap<Integer, Week> weeks = this.weeks;
		final Map<String, int[]> expensesCoords = this.expensesCoords;
		// remove previous widgets
		this.weeksPanel.clear();
		weeks.clear();
		expensesCoords.clear();

		// build new widgets
		final Date today = this.today;
		final int todayDay = DateUtil.dayOfWeek(today);

		for (final ExpenseDTO dto : data)
		{
			addDTO(dto, weeks, expensesCoords, today, todayDay);
		}

		boolean atLeastOneOpen = false;
		for (final Week week : weeks.values())
		{
			final DisclosurePanel weekPanel = createWeekPanel(week);

			if (week.num >= 0 || !atLeastOneOpen)
			{
				atLeastOneOpen = true;
				weekPanel.setOpen(true);
			}
			else
			{
				weekPanel.setOpen(false);
			}

			final FlowPanel daysPanel = initDays(week);

			weekPanel.setContent(daysPanel);
			this.weeksPanel.add(weekPanel);
		}
	}

	public void applyFilter(final String text)
	{
		if (StringUtil.stringEquals(this.currentFilter, text))
		{
			return; // EARLY EXIT !!!
		}
		this.currentFilter = text;

		final boolean filterEnabled = text != null && text.length() > 0;

		this.filterDiv.clear();
		if (filterEnabled)
		{
			this.filterDiv.add(new Label("Filter applied: " + text));
		}

		for (final Week week : this.weeks.values())
		{
			boolean hasTextInWeek = false;
			for (final Day day : week.days)
			{
				boolean hasTextInDay = false;
				final List<Expense> dayExpenses = day.expenses;
				for (int i = 0; i < dayExpenses.size(); i++)
				{
					final Expense expense = dayExpenses.get(i);
					final ExpenseDTO dto = expense.dto;
					final boolean hasText = !filterEnabled || dto.description != null && dto.description.contains(text)
							|| dto.comment != null && dto.comment.contains(text);
					if (hasText)
					{
						hasTextInDay = true;
					}
					day.table.getRowFormatter().setVisible(i + 1, hasText);
				}
				if (hasTextInDay)
				{
					hasTextInWeek = true;
				}
				if (day.table != null)
				{
					day.table.setVisible(hasTextInDay);
				}
			}
			week.panel.setVisible(hasTextInWeek);

			if (hasTextInWeek && filterEnabled)
			{
				week.panel.setOpen(true);
			}
		}
	}

	@Override
	public void addExpense(final ExpenseDTO dto)
	{
		applyFilter(null);
		final Date today = this.today;
		final int todayDay = DateUtil.dayOfWeek(today);

		final SortedMap<Integer, Week> weeks = this.weeks;
		final Map<String, int[]> expensesCoords = this.expensesCoords;
		final int[] coords = addDTO(dto, weeks, expensesCoords, today, todayDay);

		final Week week = weeks.get(coords[0]);
		final Day day = week.days[coords[1]];
		addExpenseWithinWeek(week, day);
	}

	@Override
	public void updateExpense(final ExpenseDTO dto)
	{
		applyFilter(null);
		removeExpense(dto.expenseID);
		addExpense(dto);

		/*
		 * final Date today = this.today; final int todayDay =
		 * DateUtil.dayOfWeek(today);
		 * 
		 * final SortedMap<Integer, Week> weeks = this.weeks; final Map<String,
		 * int[]> expensesCoords = this.expensesCoords;
		 * 
		 * final String expenseID = dto.expenseID; final int[] coords =
		 * expensesCoords.get(expenseID); if (coords == null) { return; // EARLY
		 * EXIT !!! }
		 * 
		 * final Week week = weeks.get(coords[0]); final Day day =
		 * week.days[coords[1]];
		 * 
		 * final List<Expense> oldDayExpenses = day.expenses; Expense oldExpense
		 * = null; int oldIndex = -1; for (int i = 0; i < oldDayExpenses.size();
		 * i++) { final Expense e = oldDayExpenses.get(i); if
		 * (e.dto.expenseID.equals(expenseID)) { oldExpense = e; oldIndex = i; }
		 * }
		 * 
		 * final int newDayNum = DateUtil.dayOfWeek(oldExpense.dateTime); final
		 * int newWeekNum = DateUtil.getWeeksBetween(oldExpense.dateTime, today,
		 * todayDay);
		 * 
		 * final FlexTable dayTable = day.table; if (newWeekNum != week.num) {
		 * // changed week final FlowPanel daysPanel = (FlowPanel)
		 * week.panel.getContent();
		 * 
		 * dayTable.removeRow(oldIndex); oldDayExpenses.remove(oldIndex); if
		 * (oldDayExpenses.isEmpty()) { daysPanel.remove(day.table); day.table =
		 * null;
		 * 
		 * if (daysPanel.getWidgetCount() == 0) {
		 * this.weeksPanel.remove(week.panel); week.panel = null;
		 * weeks.remove(week.num); } }
		 * 
		 * final int[] newCoords = addDTO(dto, weeks, expensesCoords, today,
		 * todayDay);
		 * 
		 * final Week newWeek = weeks.get(newWeekNum); final Day newDay =
		 * newWeek.days[newDayNum]; final Expense newExpense = new Expense(dto);
		 * newDay.expenses.add(newExpense);
		 * 
		 * addExpenseWithinWeek(newWeek, newDay);
		 * 
		 * if (newDay.table == null) { initDay(newDay, newDayExpenses,
		 * daysPanel); } else { final int dayExpensesSize =
		 * newDayExpenses.size(); Collections.sort(newDayExpenses); final int
		 * index = newDayExpenses.indexOf(newExpense);
		 * 
		 * final FlexTable newDayTable = newDay.table;
		 * newDayTable.insertRow(index); initExpense(newExpense, index,
		 * newDayTable, newDayTable.getFlexCellFormatter()); } } else if
		 * (newDayNum != day.num) { // changed day within week final FlowPanel
		 * daysPanel = (FlowPanel) week.panel.getContent();
		 * 
		 * dayTable.removeRow(oldIndex); oldDayExpenses.remove(oldIndex); if
		 * (oldDayExpenses.isEmpty()) { daysPanel.remove(day.table); day.table =
		 * null; }
		 * 
		 * final Day newDay = week.days[newDayNum]; final Expense newExpense =
		 * new Expense(dto);
		 * 
		 * final List<Expense> newDayExpenses = newDay.expenses;
		 * newDayExpenses.add(newExpense);
		 * 
		 * addExpenseWithinExistingWeek(week, newDay); } else { // nothing
		 * changed Collections.sort(oldDayExpenses); final int newIndex =
		 * oldDayExpenses.indexOf(oldExpense); final int indexToUpdate; if
		 * (newIndex < oldIndex) { dayTable.removeRow(oldIndex);
		 * dayTable.insertRow(newIndex); initExpense(oldExpense, newIndex,
		 * dayTable, dayTable.getFlexCellFormatter()); } else if (newIndex >
		 * oldIndex) { dayTable.insertRow(newIndex);
		 * dayTable.removeRow(oldIndex); initExpense(oldExpense, newIndex - 1,
		 * dayTable, dayTable.getFlexCellFormatter()); } else {
		 * updateExpense(oldExpense, newIndex, dayTable,
		 * dayTable.getFlexCellFormatter()); } }
		 * 
		 * if (week.panel == null) { final DisclosurePanel weekPanel =
		 * createWeekPanel(week); weekPanel.setOpen(true); final FlowPanel
		 * daysPanel = initDays(week); weekPanel.setContent(daysPanel);
		 * this.weeksPanel.add(weekPanel); } else { week.panel.setOpen(true);
		 * final FlowPanel daysPanel = (FlowPanel) week.panel.getContent();
		 * final List<Expense> dayExpenses = вфнУxpenses; if (dayTable == null)
		 * { initDay(day, dayExpenses, daysPanel); } else { final int
		 * dayExpensesSize = dayExpenses.size(); final Expense expense =
		 * dayExpenses.get(dayExpensesSize - 1); Collections.sort(dayExpenses);
		 * final int index = dayExpenses.indexOf(expense);
		 * 
		 * final FlexTable dayTable = dayTable; dayTable.insertRow(index);
		 * initExpense(expense, index, dayTable,
		 * dayTable.getFlexCellFormatter()); } }
		 */
	}

	@Override
	public void removeExpenses(final Collection<String> expenseIDs)
	{
		for (final String expenseID : expenseIDs)
		{
			removeExpense(expenseID);
		}
	}

	public void removeExpense(final String expenseID)
	{
		applyFilter(null);
		final SortedMap<Integer, Week> weeks = this.weeks;
		final Map<String, int[]> expensesCoords = this.expensesCoords;

		final int[] coords = expensesCoords.remove(expenseID);
		if (coords == null)
		{
			return; // EARLY EXIT !!!
		}

		final Week week = weeks.get(coords[0]);
		final Day day = week.days[coords[1]];

		final List<Expense> dayExpenses = day.expenses;

		final int myIndex = findExpense(expenseID, dayExpenses);
		final Expense myExpense = myIndex != -1 ? dayExpenses.get(myIndex) : null;

		if (myExpense == null)
		{
			return;// EARLY EXIT !!!
		}

		day.table.removeRow(myIndex + 1);
		dayExpenses.remove(myIndex);
		if (dayExpenses.isEmpty())
		{
			final FlowPanel daysPanel = (FlowPanel) week.panel.getContent();
			daysPanel.remove(day.table);
			day.table = null;

			if (daysPanel.getWidgetCount() == 0)
			{
				this.weeksPanel.remove(week.panel);
				week.panel = null;
				weeks.remove(week.num);
			}
		}
	}

	private static int findExpense(final String expenseID, final List<Expense> dayExpenses)
	{
		int myIndex = -1;
		for (int i = 0; i < dayExpenses.size(); i++)
		{
			final Expense e = dayExpenses.get(i);
			if (e.dto.expenseID.equals(expenseID))
			{
				myIndex = i;
			}
		}
		return myIndex;
	}

	private static int[] addDTO(final ExpenseDTO dto, final SortedMap<Integer, Week> weeks,
			final Map<String, int[]> expensesCoords, final Date today, final int todayDay)
	{
		final Expense expense = new Expense(dto);
		final String expenseID = dto.expenseID;
		final int dayNum = DateUtil.dayOfWeek(expense.dateTime);
		final int weekNum = DateUtil.getWeeksBetween(expense.dateTime, today, todayDay);
		final int[] coords = new int[] { weekNum, dayNum };
		expensesCoords.put(expenseID, coords);
		Week currWeek = weeks.get(weekNum);
		if (currWeek == null)
		{
			currWeek = new Week(weekNum);
			weeks.put(weekNum, currWeek);
		}

		currWeek.days[dayNum].expenses.add(expense);

		return coords;
	}

	private void addExpenseWithinWeek(final Week week, final Day day)
	{
		if (week.panel == null)
		{
			final DisclosurePanel weekPanel = createWeekPanel(week);
			weekPanel.setOpen(true);
			final FlowPanel daysPanel = initDays(week);
			weekPanel.setContent(daysPanel);
			int i = 0;
			for (final Week w : this.weeks.values())
			{
				if (w == week)
				{
					this.weeksPanel.insert(weekPanel, i);
					break;
				}
				i++;
			}
		}
		else
		{
			week.panel.setOpen(true);
			addExpenseWithinExistingWeek(week, day);
		}
	}

	private void addExpenseWithinExistingWeek(final Week week, final Day day)
	{
		final FlowPanel daysPanel = (FlowPanel) week.panel.getContent();
		final List<Expense> dayExpenses = day.expenses;
		if (day.table == null)
		{
			int i = 0;
			for (int d = 6; d >= 0; d--)
			{
				final Day otherDay = week.days[d];
				if (day == otherDay)
				{
					break;
				}
				if (!otherDay.expenses.isEmpty())
				{
					i++;
				}
			}
			initDay(day, i, dayExpenses, daysPanel);
		}
		else
		{
			final int dayExpensesSize = dayExpenses.size();
			final Expense expense = dayExpenses.get(dayExpensesSize - 1);
			Collections.sort(dayExpenses);
			final int index = dayExpenses.indexOf(expense);

			final FlexTable dayTable = day.table;
			dayTable.insertRow(index + 1);
			initExpense(expense, index + 1, dayTable, dayTable.getFlexCellFormatter());
		}
	}

	private static DisclosurePanel createWeekPanel(final Week week)
	{
		final DisclosurePanel weekPanel = new DisclosurePanel();
		weekPanel.setWidth("100%");
		weekPanel.setStylePrimaryName("week-panel");
		week.panel = weekPanel;
		weekPanel.setHeader(new Label(week.toString()));
		return weekPanel;
	}

	private FlowPanel initDays(final Week week)
	{
		final FlowPanel daysPanel = new FlowPanel();
		for (int d = 6; d >= 0; d--)
		{
			final Day day = week.days[d];
			final List<Expense> dayExpenses = day.expenses;
			if (!dayExpenses.isEmpty())
			{
				initDay(day, -1, dayExpenses, daysPanel);
			}
		}
		return daysPanel;
	}

	private void initDay(final Day day, final int index, final List<Expense> dayExpenses, final FlowPanel daysPanel)
	{
		Collections.sort(dayExpenses);
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
			initExpense(expense, i, dayTable, flexCellFormatter);
			i++;
		}
		if (index != -1)
		{
			daysPanel.insert(dayTable, index);
		}
		else
		{
			daysPanel.add(dayTable);
		}
	}

	private void initExpense(final Expense expense, final int index, final FlexTable dayTable,
			final FlexCellFormatter flexCellFormatter)
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
		dayTable.setWidget(index, 0, expense.checkBox);
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
		dayTable.setText(index, 1, timeFormat.format(dateTime));
		dayTable.setWidget(index, 2, descLabel);
		dayTable.setWidget(index, 3, new Label(currencyFormat.format(dto.amount)));
		flexCellFormatter.setWidth(index, 0, "40px");
		flexCellFormatter.setWidth(index, 1, "80px");
		flexCellFormatter.setWidth(index, 3, "120px");
	}

	/*
	 * private void updateExpense(final Expense expense, final int index, final
	 * FlexTable dayTable, final FlexCellFormatter flexCellFormatter) { final
	 * Date dateTime = expense.dateTime; final ExpenseDTO dto = expense.dto;
	 * dayTable.setText(index, 1, timeFormat.format(dateTime)); final Label
	 * descLabel = (Label) dayTable.getWidget(index, 2);
	 * descLabel.setText(dto.description); descLabel.setTitle(dto.comment);
	 * ((Label) dayTable.getWidget(index,
	 * 3)).setText(currencyFormat.format(dto.amount)); }
	 */

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

	private static class Expense implements Comparable<Expense>
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

		@Override
		public int compareTo(final Expense o)
		{
			return this.dateTime.compareTo(o.dateTime);
		}

	}
}
