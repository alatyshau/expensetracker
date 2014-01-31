package com.toptal.expensetracker.service;

import java.util.List;

import com.toptal.expensetracker.common.ServiceContext;
import com.toptal.expensetracker.dto.ExpenseDTO;

public interface ExpenseTrackingService
{
	List<ExpenseDTO> getExpenses(ServiceContext ctx);
}
