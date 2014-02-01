package com.toptal.expensetracker.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toptal.expensetracker.common.AccessRole;
import com.toptal.expensetracker.common.ServiceContext;
import com.toptal.expensetracker.common.Utils;
import com.toptal.expensetracker.dto.ExpenseDTO;
import com.toptal.expensetracker.services.ExpenseTrackingService;
import com.toptal.expensetracker.services.dao.ExpenseTrackingDAO;

@Transactional
@Service("expenseTrackingService")
public class DefaultExpenseTrackingService implements ExpenseTrackingService
{
	@Autowired
	private ExpenseTrackingDAO expenseTrackingDAO;

	@Override
	public List<ExpenseDTO> searchExpenses(final ServiceContext ctx)
	{
		Utils.ensureRoles(ctx, AccessRole.USER);
		return this.expenseTrackingDAO.selectExpenses(ctx.getUserId());
	}

	@Override
	public ExpenseDTO createExpense(final ServiceContext ctx, final ExpenseDTO expense)
	{
		Utils.ensureRoles(ctx, AccessRole.USER);
		return this.expenseTrackingDAO.insertExpense(ctx.getUserId(), expense);
	}

	@Override
	public List<ExpenseDTO> createExpenses(final ServiceContext ctx, final List<ExpenseDTO> expenses)
	{
		Utils.ensureRoles(ctx, AccessRole.USER);
		final String userId = ctx.getUserId();
		final List<ExpenseDTO> result = new ArrayList<>();
		for (final ExpenseDTO expense : expenses)
		{
			final ExpenseDTO insertedExpense = this.expenseTrackingDAO.insertExpense(userId, expense);
			result.add(insertedExpense);
		}
		return result;
	}

	@Override
	public ExpenseDTO readExpense(final ServiceContext ctx, final Long expenseId)
	{
		Utils.ensureRoles(ctx, AccessRole.USER);
		return this.expenseTrackingDAO.selectExpense(ctx.getUserId(), expenseId);
	}

	@Override
	public ExpenseDTO updateExpense(final ServiceContext ctx, final Long expenseId, final ExpenseDTO expense)
	{
		Utils.ensureRoles(ctx, AccessRole.USER);
		return this.expenseTrackingDAO.updateExpense(ctx.getUserId(), expenseId, expense);
	}

	@Override
	public void deleteExpenses(final ServiceContext ctx, final List<Long> expenseIds)
	{
		Utils.ensureRoles(ctx, AccessRole.USER);
		this.expenseTrackingDAO.deleteExpenses(ctx.getUserId(), expenseIds);
	}

}
