package com.au.easyreference.app.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class Result implements Parcelable
{
	public String title;
	public String subtitle;
	public String volume;
	public String publicationName;
	public String publicationDate;
	public String publisher;
	public String authorsString;
	public String issue;
	public String pageNo;
	public String doi;

	public int type;

	public Result(JSONObject result, int type)
	{
		try
		{
			Log.d("RESULT", result.toString(4));
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		this.type = type;

		String[] titleSubTitle = result.optString("title").split(":");

		if(titleSubTitle.length == 0 || titleSubTitle.length == 1)
			title = result.optString("title");
		else if(titleSubTitle.length >= 2)
		{
			title = titleSubTitle[0].trim();
			subtitle = titleSubTitle[1].trim();
		}

		volume = result.optString("volume");
		publicationName = result.optString("publicationName");
		publicationDate = result.optString("publicationDate");
		publisher = result.optString("publisher");
		issue = result.optString("number");
		pageNo = result.optString("startingPage");
		doi = result.optString("doi");

		ArrayList<String> authors = new ArrayList<>();
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
			String[] authorName = null;

			if(author.contains(","))
				authorName = author.split(",");

			if(authorName != null)
			{
				authorName[0] = authorName[0].split(":\"")[1];

				authorsStringBuilder.append(authorName[0]).append(", ")
						.append(authorName[1].trim().charAt(0));
			}
			else
				authorsStringBuilder.append(author.trim());

			authorsStringBuilder.append(". ");
		}

		authorsString = authorsStringBuilder.toString();
	}

	public Result(Parcel in)
	{
		String[] data = new String[9];
		in.readStringArray(data);
		title = data[0];
		volume = data[1];
		publicationName = data[2];
		publicationDate = data[3];
		publisher = data[4];
		authorsString = data[5];
		issue = data[6];
		pageNo = data[7];
		doi = data[8];
		type = in.readInt();
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
	{
		public Result createFromParcel(Parcel in)
		{
			return new Result(in);
		}

		public Result[] newArray(int size)
		{
			return new Result[size];
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		out.writeStringArray(new String[]{
				title,
				volume,
				publicationName,
				publicationDate,
				publisher,
				authorsString,
				issue,
				pageNo,
				doi
		});
		out.writeInt(type);
	}
}
