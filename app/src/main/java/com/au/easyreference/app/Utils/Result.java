package com.au.easyreference.app.Utils;

import org.json.JSONObject;

/**
 * @author Marcus Hooper
 */
public class Result
{
	public String title;
	public String volume;
	public String publicationName;
	public String publicationDate;
	public String publisher;

	public Result(JSONObject result)
	{
		title = result.optString("title");
		volume = result.optString("volume");
		publicationName = result.optString("publicationName");
		publicationDate = result.optString("publicationDate");
		publisher = result.optString("publisher");
	}
}
