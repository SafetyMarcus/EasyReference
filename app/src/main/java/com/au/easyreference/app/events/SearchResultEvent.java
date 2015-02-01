package com.au.easyreference.app.events;

import com.au.easyreference.app.utils.Result;

/**
 * @author Marcus Hooper
 */
public class SearchResultEvent
{
	public Result result;

	public SearchResultEvent(Result result)
	{
		this.result = result;
	}
}
