package com.au.easyreference.app.Events;

import com.au.easyreference.app.Utils.Result;

/**
 * @author Marcus Hooper
 */
public class ResultSelectedEvent
{
	public Result result;

	public ResultSelectedEvent(Result result)
	{
		this.result = result;
	}
}
