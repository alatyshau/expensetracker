package com.toptal.expensetracker.web.misc;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.toptal.expensetracker.common.AccessRole;
import com.toptal.expensetracker.common.ServiceContext;

public class ContextInitInterceptor implements HandlerInterceptor
{

	private static final ThreadLocal<ServiceContext> threadLocal = new ThreadLocal<ServiceContext>();
	private final LocaleResolver localeResolver;

	public ContextInitInterceptor(final LocaleResolver localeResolver)
	{
		super();
		this.localeResolver = localeResolver;
	}

	public static ServiceContext getCurrentContext()
	{
		final ServiceContext result = threadLocal.get();
		if (result == null)
		{
			throw new IllegalStateException("ServiceContext was not initialized. Current tread: "
					+ Thread.currentThread().getName());
		}
		return result;
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception
	{
		final ServiceContext ctx = new WebServiceContext(request);
		threadLocal.set(ctx);
		return true;
	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
			final ModelAndView modelAndView) throws Exception
	{

	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler, final Exception ex) throws Exception
	{
		threadLocal.set(null);
	}

	private final class WebServiceContext implements ServiceContext
	{
		private final HttpServletRequest request;
		private String userId;
		private Locale locale;
		private Set<AccessRole> roles;

		private WebServiceContext(final HttpServletRequest request)
		{
			this.request = request;
		}

		@Override
		public String getUserId()
		{
			if (this.userId != null)
			{
				return this.userId;
			}
			else
			{
				return this.userId = SecurityContextHolder.getContext().getAuthentication().getName();
			}
		}

		@Override
		public Locale getLocale()
		{
			if (this.locale != null)
			{
				return this.locale;
			}
			else
			{
				return this.locale = ContextInitInterceptor.this.localeResolver.resolveLocale(this.request);
			}
		}

		@Override
		public Set<AccessRole> getRoles()
		{
			if (this.roles != null)
			{
				return this.roles; // EARLY_EXIT!!!
			}
			else
			{
				final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				final Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
				if (grantedAuthorities == null || grantedAuthorities.isEmpty())
				{
					return this.roles = Collections.<AccessRole> emptySet(); // EARLY_EXIT!!!
				}
				final Builder<AccessRole> builder = ImmutableSet.<AccessRole> builder();
				for (final GrantedAuthority authority : grantedAuthorities)
				{
					if (authority instanceof AccessRole)
					{
						builder.add((AccessRole) authority);
					}
				}
				return this.roles = builder.build();
			}
		}
	}

}
