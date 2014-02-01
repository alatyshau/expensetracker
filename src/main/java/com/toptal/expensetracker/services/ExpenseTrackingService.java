package com.toptal.expensetracker.services;

import java.util.List;

import com.toptal.expensetracker.common.ServiceContext;
import com.toptal.expensetracker.dto.ExpenseDTO;

public interface ExpenseTrackingService
{
	List<ExpenseDTO> searchExpenses(ServiceContext ctx);

	ExpenseDTO createExpense(ServiceContext ctx, ExpenseDTO expense);

	List<ExpenseDTO> createExpenses(ServiceContext ctx, List<ExpenseDTO> expenses);

	ExpenseDTO readExpense(ServiceContext ctx, Long expenseId);

	ExpenseDTO updateExpense(ServiceContext ctx, Long expenseId, ExpenseDTO expense);

	void deleteExpenses(ServiceContext ctx, List<Long> expenseIds);
}
