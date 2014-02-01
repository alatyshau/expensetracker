package com.toptal.expensetracker.services.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.toptal.expensetracker.common.InternalException;
import com.toptal.expensetracker.dto.ExpenseDTO;
import com.toptal.expensetracker.services.dao.ExpenseTrackingDAO;

@Repository("expenseTrackingDAO")
public class MySQLExpenseTrackingDAO implements ExpenseTrackingDAO
{
	private static final ExpenseDTOMapper EXPENSE_DTO_MAPPER = new ExpenseDTOMapper();

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ExpenseDTO> selectExpenses(final String userId)
	{
		return this.jdbcTemplate.query(
				"SELECT expenseID, dateTime, description, amount, comment FROM expense WHERE userID = ?",
				EXPENSE_DTO_MAPPER, userId);
	}

	@Override
	public ExpenseDTO insertExpense(final String userId, final ExpenseDTO expense)
	{
		throw new InternalException("not implemented");
	}

	@Override
	public ExpenseDTO selectExpense(final String userId, final Long expenseId)
	{
		final List<ExpenseDTO> list = this.jdbcTemplate
				.query("SELECT expenseID, dateTime, description, amount, comment FROM expense WHERE userID = ? AND expenseID = ?",
						EXPENSE_DTO_MAPPER, userId, expenseId);
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public ExpenseDTO updateExpense(final String userId, final Long expenseId, final ExpenseDTO expense)
	{
		throw new InternalException("not implemented");
	}

	@Override
	public void deleteExpenses(final String userId, final List<Long> expenseIds)
	{
		throw new InternalException("not implemented");
	}

	private static final class ExpenseDTOMapper implements RowMapper<ExpenseDTO>
	{
		@Override
		public ExpenseDTO mapRow(final ResultSet rs, final int rowNum) throws SQLException
		{
			final ExpenseDTO dto = new ExpenseDTO();
			dto.setExpenseID(rs.getLong(1));
			dto.setDateTime(new Date(rs.getTimestamp(2).getTime()));
			dto.setDescription(rs.getString(3));
			dto.setAmount(rs.getBigDecimal(4));
			dto.setComment(rs.getString(5));
			return dto;
		}
	}
}
