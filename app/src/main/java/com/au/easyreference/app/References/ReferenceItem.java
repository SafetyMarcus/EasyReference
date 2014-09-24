package com.au.easyreference.app.References;

import com.au.easyreference.app.Utils.Result;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static com.au.easyreference.app.Utils.Constants.AUTHOR;
import static com.au.easyreference.app.Utils.Constants.ITEM_TYPE;
import static com.au.easyreference.app.Utils.Constants.LOCATION;
import static com.au.easyreference.app.Utils.Constants.PUBLISHER;
import static com.au.easyreference.app.Utils.Constants.REFERENCE_ITEM_ID;
import static com.au.easyreference.app.Utils.Constants.SUBTITLE;
import static com.au.easyreference.app.Utils.Constants.TITLE;
import static com.au.easyreference.app.Utils.Constants.YEAR;

/**
 * @author Marcus Hooper
 */
public class ReferenceItem
{
	public final static int BOOK_REFERENCE = 0;
	public final static int JOURNAL_REFERENCE = 1;
	public final static int NEW = 10;

	public String id;
	public String author;
	public String year;
	public String title;
	public String subtitle;
	public String location;
	public String publisher;
	public String issue;
	public String pageNo;
	public String doi;

	public int type;

	public ReferenceItem(String author, String year, String title, String subtitle, String location, String publisher, int type)
	{
		id = UUID.randomUUID().toString();
		this.author = author;
		this.year = year;
		this.title = title;
		this.subtitle = subtitle;
		this.location = location;
		this.publisher = publisher;
		this.type = type;
	}

	public ReferenceItem(int type)
	{
		if(type == NEW)
		{
			this.author = "";
			this.year = "";
			this.title = "";
			this.subtitle = "";
			this.location = "";
			this.publisher = "";
			this.type = type;
		}
	}

	public ReferenceItem(Result result)
	{
		id = UUID.randomUUID().toString();
		type = result.type;

		author = result.authorsString;
		year = result.publicationDate;
		title = result.title;
		subtitle = "";
		location = "";
		publisher = result.publisher;
		issue = result.issue;
		pageNo = result.pageNo;
		doi = result.doi;
	}

	public ReferenceItem(JSONObject referenceObject)
	{
		id = referenceObject.optString(REFERENCE_ITEM_ID);
		author = referenceObject.optString(AUTHOR);
		year = referenceObject.optString(YEAR);
		title = referenceObject.optString(TITLE);
		subtitle = referenceObject.optString(SUBTITLE);
		location = referenceObject.optString(LOCATION);
		publisher = referenceObject.optString(publisher);
		type = referenceObject.optInt(ITEM_TYPE);
	}

	public JSONObject toJSON()
	{
		JSONObject referenceObject = new JSONObject();

		try
		{
			referenceObject.put(REFERENCE_ITEM_ID, id);
			referenceObject.put(AUTHOR, author);
			referenceObject.put(YEAR, year);
			referenceObject.put(TITLE, title);
			referenceObject.put(SUBTITLE, subtitle);
			referenceObject.put(LOCATION, location);
			referenceObject.put(PUBLISHER, publisher);
			referenceObject.put(ITEM_TYPE, type);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		return referenceObject;
	}
}
