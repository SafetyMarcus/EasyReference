package com.au.easyreference.app.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
	public String authorsString;

	public Result(JSONObject result)
	{
		title = result.optString("title");
		volume = result.optString("volume");
		publicationName = result.optString("publicationName");
		publicationDate = result.optString("publicationDate");
		publisher = result.optString("publisher");

		ArrayList<String> authors = new ArrayList<String>();
		JSONArray authorsArray = result.optJSONArray("creators");
		if(authorsArray != null)
		{
			for(int i = 0; i < authorsArray.length(); i++)
				authors.add(authorsArray.optString(i));
		}

		StringBuilder authorsStringBuilder = new StringBuilder();

		for(int i = 0; i < authors.size(); i++)
		{
			String author = authors.get(i);
			String[] authorName = author.split(",");
			authorName[0] = authorName[0].split(":\"")[1];

			authorsStringBuilder.append(authorName[0]).append(", ")
					.append(authorName[1].trim().charAt(0))
					.append(". ");
		}

		authorsString = authorsStringBuilder.toString();
	}
}
