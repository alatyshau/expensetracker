package com.toptal.expensetracker.gwt.client.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.toptal.expensetracker.gwt.client.data.ExpenseDTO;
import com.toptal.expensetracker.gwt.client.data.WeeklyExpensesDTO;

@Path("/api")
public interface ExpenseTrackerService extends RestService
{
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/expenses")
	public void getExpenses(MethodCallback<List<ExpenseDTO>> callback);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/expenses/weekly")
	public void getWeeklyStatistics(MethodCallback<List<WeeklyExpensesDTO>> callback);

}
