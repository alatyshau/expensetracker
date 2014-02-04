package com.toptal.expensetracker.web.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toptal.expensetracker.dto.ExpenseDTO;
import com.toptal.expensetracker.services.ExpenseTrackingService;

@Controller
@RequestMapping("/api/expenses")
public class ExpensesController extends BaseRestController
{
	@Autowired
	private ExpenseTrackingService expenseTrackingService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<ExpenseDTO> getExpenses()
	{
		return this.expenseTrackingService.searchExpenses(ctx());
	}

	@RequestMapping(value = "/{expenseId}", method = RequestMethod.GET)
	@ResponseBody
	public ExpenseDTO getExpense(@PathVariable final Long expenseId)
	{
		return this.expenseTrackingService.readExpense(ctx(), expenseId);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ExpenseDTO createExpense(final Model model, @RequestBody final ExpenseDTO expense)
	{
		return this.expenseTrackingService.createExpense(ctx(), expense);
	}

	@RequestMapping(value = "/{expenseID}", method = RequestMethod.PUT)
	@ResponseBody
	public ExpenseDTO updateExpense(@PathVariable final Long expenseID, @RequestBody final ExpenseDTO expense)
	{
		return this.expenseTrackingService.updateExpense(ctx(), expenseID, expense);
	}

	@RequestMapping(value = "/{expenseID}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteExpense(@PathVariable final Long expenseID)
	{
		this.expenseTrackingService.deleteExpenses(ctx(), Collections.singletonList(expenseID));
	}

	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteExpenses(@RequestBody final List<Long> expenseIDs)
	{
		this.expenseTrackingService.deleteExpenses(ctx(), expenseIDs);
	}

}
