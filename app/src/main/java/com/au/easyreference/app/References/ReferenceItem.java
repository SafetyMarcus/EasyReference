package com.au.easyreference.app.references;

import android.content.Context;
import com.au.easyreference.app.R;
import com.au.easyreference.app.utils.Result;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static com.au.easyreference.app.utils.Constants.AUTHOR;
import static com.au.easyreference.app.utils.Constants.BOOK_SUBTITLE;
import static com.au.easyreference.app.utils.Constants.BOOK_TITLE;
import static com.au.easyreference.app.utils.Constants.DOI;
import static com.au.easyreference.app.utils.Constants.EDITORS;
import static com.au.easyreference.app.utils.Constants.ISSUE;
import static com.au.easyreference.app.utils.Constants.ITEM_TYPE;
import static com.au.easyreference.app.utils.Constants.JOURNAL_TITLE;
import static com.au.easyreference.app.utils.Constants.LOCATION;
import static com.au.easyreference.app.utils.Constants.PAGE_NO;
import static com.au.easyreference.app.utils.Constants.PUBLISHER;
import static com.au.easyreference.app.utils.Constants.REFERENCE_ITEM_ID;
import static com.au.easyreference.app.utils.Constants.SUBTITLE;
import static com.au.easyreference.app.utils.Constants.TITLE;
import static com.au.easyreference.app.utils.Constants.URL;
import static com.au.easyreference.app.utils.Constants.VOLUME_NO;
import static com.au.easyreference.app.utils.Constants.YEAR;

/**
 * @author Marcus Hooper
 */
public class ReferenceItem
{
	public final static int BOOK_REFERENCE = 0;
	public final static int JOURNAL_REFERENCE = 1;
	public final static int BOOK_CHAPTER = 2;
	public final static int WEB_PAGE = 3;

	public String id;
	public String author;
	public String year;
	public String title;
	public String subtitle;
	public String location;
	public String publisher;

	//Journal
	public String journalTitle;
	public String volumeNo;
	public String issue;
	public String pageNo;
	public String doi;

	//Book Chapter
	public String editors;
	public String bookTitle;
	public String bookSubtitle;
	public String pagesOfChapter;

	//Web Page
	public String url;

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
			case WEB_PAGE:
				return R.drawable.webpage;
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
			case WEB_PAGE:
				return context.getString(R.string.web_page_or_document);
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
		this.editors = "";
		this.bookTitle = "";
		this.bookSubtitle = "";
		this.url = "";

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

		editors = "";
		bookTitle = "";
		bookSubtitle = "";

		url = "";
	}

	public ReferenceItem(JSONObject referenceObject)
	{
		id = referenceObject.optString(REFERENCE_ITEM_ID);
		type = referenceObject.optInt(ITEM_TYPE);

		//Base values
		author = referenceObject.optString(AUTHOR);
		year = referenceObject.optString(YEAR);
		title = referenceObject.optString(TITLE);
		subtitle = referenceObject.optString(SUBTITLE);
		location = referenceObject.optString(LOCATION);
		publisher = referenceObject.optString(PUBLISHER);

		//Journal
		journalTitle = referenceObject.optString(JOURNAL_TITLE);
		volumeNo = referenceObject.optString(VOLUME_NO);
		issue = referenceObject.optString(ISSUE);
		pageNo = referenceObject.optString(PAGE_NO);
		doi = referenceObject.optString(DOI);

		//Book Chapter
		editors = referenceObject.optString(EDITORS);
		bookTitle = referenceObject.optString(BOOK_TITLE);
		bookSubtitle = referenceObject.optString(BOOK_SUBTITLE);

		//Web Page
		url = referenceObject.optString(URL);
	}

	public JSONObject toJSON()
	{
		JSONObject referenceObject = new JSONObject();

		try
		{
			referenceObject.put(REFERENCE_ITEM_ID, id);
			referenceObject.put(ITEM_TYPE, type);

			//Base
			referenceObject.put(AUTHOR, author);
			referenceObject.put(YEAR, year);
			referenceObject.put(TITLE, title);
			referenceObject.put(SUBTITLE, subtitle);
			referenceObject.put(LOCATION, location);
			referenceObject.put(PUBLISHER, publisher);

			//Journal
			referenceObject.put(JOURNAL_TITLE, journalTitle);
			referenceObject.put(VOLUME_NO, volumeNo);
			referenceObject.put(ISSUE, issue);
			referenceObject.put(PAGE_NO, pageNo);
			referenceObject.put(DOI, doi);

			//Book Chapter
			referenceObject.put(EDITORS, editors);
			referenceObject.put(BOOK_TITLE, bookTitle);
			referenceObject.put(BOOK_SUBTITLE, bookSubtitle);

			//Web Page
			referenceObject.put(URL, url);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		return referenceObject;
	}
}
