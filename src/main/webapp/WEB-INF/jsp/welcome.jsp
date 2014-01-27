<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello World!</title>
</head>
<body>
	<spring:message code="welcome.helloWorld" />
	<hr>
	<%=SecurityContextHolder.getContext().getAuthentication()%>
	<hr>
	<h3>Login:</h3>
	<ul>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/login?email=admin@gmail.com&password=password">http://localhost:8080/toptal-expensetracker/api/login?email=admin@gmail.com&password=password</a>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/login?email=user@gmail.com&password=password">http://localhost:8080/toptal-expensetracker/api/login?email=user@gmail.com&password=password</a>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/login?email=asd@gmail.com&password=qwerty">http://localhost:8080/toptal-expensetracker/api/login?email=asd@gmail.com&password=qwerty</a>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/logout">http://localhost:8080/toptal-expensetracker/api/logout</a>
	</ul>
	<hr>
	<h3>Users:</h3>
	<ul>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/user">http://localhost:8080/toptal-expensetracker/api/user</a>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/user/new?email=asd@gmail.com&password=qwerty">http://localhost:8080/toptal-expensetracker/api/user/new?email=asd@gmail.com&password=qwerty</a>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/user/new?emailxyz@gmail.com&password=qwerty">http://localhost:8080/toptal-expensetracker/api/user/new?email=xyz@gmail.com&password=qwerty</a>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/user/new?email=abc@gmail.com&password=qwerty">http://localhost:8080/toptal-expensetracker/api/user/new?email=abc@gmail.com&password=qwerty</a>
	</ul>
	<hr>
	<h3>Expenses:</h3>
	<ul>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/expenses">http://localhost:8080/toptal-expensetracker/api/expenses</a>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/expenses/weekly">http://localhost:8080/toptal-expensetracker/api/expenses/weekly</a>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/expenses/1">http://localhost:8080/toptal-expensetracker/api/expenses/1</a>
		<li><a href="http://localhost:8080/toptal-expensetracker/api/expenses/2">http://localhost:8080/toptal-expensetracker/api/expenses/2</a>
	</ul>
	<hr>
	<a href="http://localhost:8080/toptal-expensetracker/admin">Admin page</a>
</body>
</html>
