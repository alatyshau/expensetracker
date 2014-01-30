package com.toptal.expensetracker.gwt.client.services;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.toptal.expensetracker.gwt.client.data.UserDTO;

@Path("/api")
public interface UserService extends RestService
{
	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public void getCurrentUser(MethodCallback<UserDTO> callback);

	@POST
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public void createUser(UserDTO user, MethodCallback<UserDTO> callback);

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public void login(@QueryParam("email") String email, @QueryParam("password") String password,
			MethodCallback<UserDTO> callback);

	@POST
	@Path("/logout")
	public void logout(MethodCallback<java.lang.Void> callback);
}
