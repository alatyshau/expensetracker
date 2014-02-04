package com.toptal.expensetracker.gwt.client.view;

import java.io.IOException;

import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ValueLabel;

public class StringLabel extends ValueLabel<String>
{
	private static final Renderer<String> RENDERER = new Renderer<String>()
	{

		@Override
		public String render(final String object)
		{
			return object;
		}

		@Override
		public void render(final String object, final Appendable appendable) throws IOException
		{
			appendable.append(object);
		}
	};

	public StringLabel()
	{
		super(RENDERER);
	}
}
