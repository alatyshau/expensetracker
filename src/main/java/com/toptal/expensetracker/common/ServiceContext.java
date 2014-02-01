package com.toptal.expensetracker.common;

import java.util.Locale;
import java.util.Set;

public interface ServiceContext
{
	Locale getLocale();

	String getUserId();

	Set<AccessRole> getRoles();
}