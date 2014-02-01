package com.toptal.expensetracker.services.dao;

import java.util.List;

import com.toptal.expensetracker.dto.ExpenseDTO;

public interface ExpenseTrackingDAO
{
	List<ExpenseDTO> selectExpenses(String userId);

	ExpenseDTO insertExpense(String userId, ExpenseDTO expense);

	ExpenseDTO selectExpense(String userId, Long expenseId);

	ExpenseDTO updateExpense(String userId, Long expenseId, ExpenseDTO expense);

	void deleteExpenses(String userId, List<Long> expenseIds);
}
