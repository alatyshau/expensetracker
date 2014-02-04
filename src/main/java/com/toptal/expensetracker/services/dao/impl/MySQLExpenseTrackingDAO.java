package com.toptal.expensetracker.services.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO expense (dateTime, description, amount, comment, userID) VALUES (?, ?, ?, ?, ?)";
		final int result = this.jdbcTemplate.update(new PreparedStatementCreator()
		{
			@Override
			public PreparedStatement createPreparedStatement(final Connection conn) throws SQLException
			{
				final PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				statement.setTimestamp(1, new Timestamp(expense.getDateTime().getTime()));
				statement.setString(2, expense.getDescription());
				statement.setBigDecimal(3, expense.getAmount());
				statement.setString(4, expense.getComment());
				statement.setString(5, userId);
				return statement;
			}
		}, keyHolder);
		if (result > 0)
		{
			expense.setExpenseID(keyHolder.getKey().longValue());
			return expense;
		}
		else
		{
			return null;
		}
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
		final String sql = "UPDATE expense SET dateTime=?, description=?, amount=?, comment=? WHERE expenseID=? AND userID=?";
		final int result = this.jdbcTemplate.update(new PreparedStatementCreator()
		{
			@Override
			public PreparedStatement createPreparedStatement(final Connection conn) throws SQLException
			{
				final PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				statement.setTimestamp(1, new Timestamp(expense.getDateTime().getTime()));
				statement.setString(2, expense.getDescription());
				statement.setBigDecimal(3, expense.getAmount());
				statement.setString(4, expense.getComment());
				statement.setLong(5, expenseId);
				statement.setString(6, userId);
				return statement;
			}
		});
		return result > 0 ? expense : null;
	}

	@Override
	public void deleteExpenses(final String userId, final List<Long> expenseIds)
	{
		final String sql = "DELETE FROM expense WHERE expenseID=? AND userID=?";
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter()
		{

			@Override
			public void setValues(final PreparedStatement ps, final int i) throws SQLException
			{
				ps.setLong(1, expenseIds.get(i));
				ps.setString(2, userId);
			}

			@Override
			public int getBatchSize()
			{
				return expenseIds.size();
			}
		});
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
