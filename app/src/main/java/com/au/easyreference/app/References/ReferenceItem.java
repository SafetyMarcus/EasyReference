package com.au.easyreference.app.References;

import android.content.Context;
import com.au.easyreference.app.R;
import com.au.easyreference.app.Utils.Result;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static com.au.easyreference.app.Utils.Constants.AUTHOR;
import static com.au.easyreference.app.Utils.Constants.DOI;
import static com.au.easyreference.app.Utils.Constants.ISSUE;
import static com.au.easyreference.app.Utils.Constants.ITEM_TYPE;
import static com.au.easyreference.app.Utils.Constants.JOURNAL_TITLE;
import static com.au.easyreference.app.Utils.Constants.LOCATION;
import static com.au.easyreference.app.Utils.Constants.PAGE_NO;
import static com.au.easyreference.app.Utils.Constants.PUBLISHER;
import static com.au.easyreference.app.Utils.Constants.REFERENCE_ITEM_ID;
import static com.au.easyreference.app.Utils.Constants.SUBTITLE;
import static com.au.easyreference.app.Utils.Constants.TITLE;
import static com.au.easyreference.app.Utils.Constants.VOLUME_NO;
import static com.au.easyreference.app.Utils.Constants.YEAR;

/**
 * @author Marcus Hooper
 */
public class ReferenceItem
{
	public final static int BOOK_REFERENCE = 0;
	public final static int JOURNAL_REFERENCE = 1;
	public final static int BOOK_CHAPTER = 2;
	public final static int WEBPAGE = 3;
	public final static int WEB_DOCUMENT = 4;

	public String id;
	public String author;
	public String year;
	public String title;
	public String subtitle;
	public String location;
	public String publisher;
	public String journalTitle;
	public String volumeNo;
	public String issue;
	public String pageNo;
	public String doi;

	public int type;

	public static int getIcon(int type)
	{
		switch(type)
		{
			case BOOK_REFERENCE:
				return R.drawable.icon_book;
			case JOURNAL_REFERENCE:
				return R.drawable.icon_journal;
			case BOOK_CHAPTER:
				return R.drawable.icon_book_chapter;
			case WEBPAGE:
				return R.drawable.webpage;
			case WEB_DOCUMENT:
				return R.drawable.web_document;
		}

		return 0;
	}

	public static String getTitle(int type, Context context)
	{
		switch(type)
		{
			case BOOK_REFERENCE:
				return context.getString(R.string.book);
			case JOURNAL_REFERENCE:
				return context.getString(R.string.journal);
			case BOOK_CHAPTER:
				return context.getString(R.string.book_chapter);
			case WEBPAGE:
				return context.getString(R.string.webpage);
			case WEB_DOCUMENT:
				return context.getString(R.string.web_document);
		}

		return "";
	}

	public ReferenceItem(int type)
	{
		id = UUID.randomUUID().toString();
		this.author = "";
		this.year = "";
		this.title = "";
		this.subtitle = "";
		this.journalTitle = "";
		this.volumeNo = "";
		this.issue = "";
		this.pageNo = "";
		this.doi = "";
		this.location = "";
		this.publisher = "";

		this.type = type;
	}

	public ReferenceItem(Result result)
	{
		id = UUID.randomUUID().toString();
		type = result.type;

		author = result.authorsString;

		if(result.publicationDate.contains("-"))
			year = result.publicationDate.split("-")[0];
		else if(result.publicationDate.contains("/"))
			year = result.publicationDate.split("/")[0];
		else
			year = result.publicationDate;

		title = result.title;
		subtitle = "";
		location = "";
		publisher = result.publisher;
		journalTitle = result.publicationName;
		volumeNo = result.volume;
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
		journalTitle = referenceObject.optString(JOURNAL_TITLE);
		volumeNo = referenceObject.optString(VOLUME_NO);
		issue = referenceObject.optString(ISSUE);
		pageNo = referenceObject.optString(PAGE_NO);
		doi = referenceObject.optString(DOI);
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
			referenceObject.put(JOURNAL_TITLE, journalTitle);
			referenceObject.put(VOLUME_NO, volumeNo);
			referenceObject.put(ISSUE, issue);
			referenceObject.put(PAGE_NO, pageNo);
			referenceObject.put(DOI, doi);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		return referenceObject;
	}
}
