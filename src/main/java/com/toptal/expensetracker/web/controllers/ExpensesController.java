package com.toptal.expensetracker.web.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toptal.expensetracker.web.dto.ExpenseDTO;

@Controller
@RequestMapping("/api/expenses")
public class ExpensesController
{
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<ExpenseDTO> getExpenses()
	{
		return Arrays.asList(ExpenseDTO.sample());
	}

	@RequestMapping(value = "/{expenseId}", method = RequestMethod.GET)
	@ResponseBody
	public ExpenseDTO getExpense(@PathVariable final Long expenseId)
	{
		return ExpenseDTO.sample(expenseId);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ExpenseDTO createExpense(final Model model, final ExpenseDTO expense)
	{
		expense.setExpenseId(123L);
		return expense;
	}

	@RequestMapping(value = "/{expenseId}", method = RequestMethod.PUT)
	@ResponseBody
	public ExpenseDTO updateExpense(@PathVariable final Long expenseId, final ExpenseDTO expense)
	{
		return expense;
	}

	@RequestMapping(value = "/{expenseId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ExpenseDTO deleteExpense(@PathVariable final Long expenseId)
	{
		return ExpenseDTO.sample(expenseId);
	}
}
