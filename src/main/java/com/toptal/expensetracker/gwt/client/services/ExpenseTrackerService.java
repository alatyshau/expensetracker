package com.toptal.expensetracker.gwt.client.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.toptal.expensetracker.gwt.client.dto.ExpenseDTO;

@Path("/api")
public interface ExpenseTrackerService extends RestService
{
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/expenses")
	void getExpenses(MethodCallback<List<ExpenseDTO>> callback);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/expenses")
	void createExpense(ExpenseDTO expense, MethodCallback<ExpenseDTO> callback);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/expenses/{expenseID}")
	void updateExpense(@PathParam("expenseID") String expenseID, ExpenseDTO expense, MethodCallback<ExpenseDTO> callback);

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/expenses")
	void deleteExpenses(List<String> expenseIDs, MethodCallback<Void> callback);

}
