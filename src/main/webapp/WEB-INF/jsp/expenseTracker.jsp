<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="welcome.helloWorld" /></title>
<link rel="stylesheet" type="text/css" media="all" href="css/main.css" />
<script type="text/javascript" language="javascript" src="expenseTracker/expenseTracker.nocache.js"></script>
</head>
<body>
    <center>
        <div style="width: 800px; min-height: 100%; border-left: 1px solid #aaa; border-right: 1px solid #aaa;">
            <table style="width: 100%;" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td><img src="images/icon_256.png" width="64" height="64" border="0"> <span
                        style="font-size: 16px; font-weight: bolder;">Expense Tracker</span></td>
                    <td align="right"><div id="slot-header"></div></td>
                </tr>
                <tr>
                    <td colspan="2"><div id="slot-body"></div></td>
                </tr>
            </table>
        </div>
    </center>
    <!-- OPTIONAL: include this if you want history support
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
        style="position: absolute; width: 0; height: 0; border: 0"></iframe>
         -->
</body>
</html>
