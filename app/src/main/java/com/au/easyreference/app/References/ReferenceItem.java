package com.au.easyreference.app.references;

import android.support.annotation.NonNull;
import com.au.easyreference.app.utils.HelperFunctions;
import com.au.easyreference.app.utils.Result;
import org.json.JSONException;
import org.json.JSONObject;
import utils.State;

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
import static com.au.easyreference.app.utils.Constants.PAGES_OF_CHAPTER;
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
public class ReferenceItem implements Comparable<ReferenceItem>
{
	public final static int BOOK_REFERENCE = 0;
	public final static int JOURNAL_REFERENCE = 1;
	public final static int BOOK_CHAPTER = 2;
	public final static int WEB_PAGE = 3;

	public int italicsStart = -1;
	public int italicsEnd = -1;

	public String id;
	public State<String> author;
	public State<String> year;
	public State<String> title;
	public State<String> subtitle;
	public State<String> location;
	public State<String> publisher;

	//Journal
	public State<String> journalTitle;
	public State<String> volumeNo;
	public State<String> issue;
	public State<String> pageNo;
	public State<String> doi;

	//Book Chapter
	public State<String> editors;
	public State<String> bookTitle;
	public State<String> bookSubtitle;
	public State<String> pagesOfChapter;

	//Web Page
	public State<String> url;

	public int type;
	
	public ReferenceItem(int type)
	{
		id = UUID.randomUUID().toString();
		this.author = new State<>("");
		this.year = new State<>("");
		this.title = new State<>("");
		this.subtitle = new State<>("");
		this.journalTitle = new State<>("");
		this.volumeNo = new State<>("");
		this.issue = new State<>("");
		this.pageNo = new State<>("");
		this.doi = new State<>("");
		this.location = new State<>("");
		this.publisher = new State<>("");
		this.editors = new State<>("");
		this.bookTitle = new State<>("");
		this.bookSubtitle = new State<>("");
		this.pagesOfChapter = new State<>("");
		this.url = new State<>("");

		this.type = type;
	}

	public ReferenceItem(Result result)
	{
		id = UUID.randomUUID().toString();
		type = result.type;

		author = new State<>(result.authorsString);

		if(result.publicationDate.contains("-"))
			year = new State<>(result.publicationDate.split("-")[0]);
		else if(result.publicationDate.contains("/"))
			year = new State<>(result.publicationDate.split("/")[0]);
		else
			year = new State<>(result.publicationDate);

		title = new State<>(result.title);
		subtitle = new State<>(result.subtitle);
		location = new State<>("");
		publisher = new State<>(result.publisher);
		journalTitle = new State<>(result.publicationName);
		volumeNo = new State<>(result.volume);
		issue = new State<>(result.issue);
		pageNo = new State<>(result.pageNo);
		doi = new State<>(result.doi);

		editors = new State<>("");
		bookTitle = new State<>("");
		bookSubtitle = new State<>("");

		url = new State<>("");
	}

	public ReferenceItem(JSONObject referenceObject)
	{
		id = referenceObject.optString(REFERENCE_ITEM_ID);
		type = referenceObject.optInt(ITEM_TYPE);

		//Base values
		author = new State<>(referenceObject.optString(AUTHOR));
		year = new State<>(referenceObject.optString(YEAR));
		title = new State<>(referenceObject.optString(TITLE));
		subtitle = new State<>(referenceObject.optString(SUBTITLE));
		location = new State<>(referenceObject.optString(LOCATION));
		publisher = new State<>(referenceObject.optString(PUBLISHER));

		//Journal
		journalTitle = new State<>(referenceObject.optString(JOURNAL_TITLE));
		volumeNo = new State<>(referenceObject.optString(VOLUME_NO));
		issue = new State<>(referenceObject.optString(ISSUE));
		pageNo = new State<>(referenceObject.optString(PAGE_NO));
		doi = new State<>(referenceObject.optString(DOI));

		//Book Chapter
		editors = new State<>(referenceObject.optString(EDITORS));
		bookTitle = new State<>(referenceObject.optString(BOOK_TITLE));
		bookSubtitle = new State<>(referenceObject.optString(BOOK_SUBTITLE));
		pagesOfChapter = new State<>(referenceObject.optString(PAGES_OF_CHAPTER));

		//Web Page
		url = new State<>(referenceObject.optString(URL));
	}

	public JSONObject toJSON()
	{
		JSONObject referenceObject = new JSONObject();

		try
		{
			referenceObject.put(REFERENCE_ITEM_ID, id);
			referenceObject.put(ITEM_TYPE, type);

			//Base
			referenceObject.put(AUTHOR, author.getValue());
			referenceObject.put(YEAR, year.getValue());
			referenceObject.put(TITLE, title.getValue());
			referenceObject.put(SUBTITLE, subtitle.getValue());
			referenceObject.put(LOCATION, location.getValue());
			referenceObject.put(PUBLISHER, publisher.getValue());

			//Journal
			referenceObject.put(JOURNAL_TITLE, journalTitle.getValue());
			referenceObject.put(VOLUME_NO, volumeNo.getValue());
			referenceObject.put(ISSUE, issue.getValue());
			referenceObject.put(PAGE_NO, pageNo.getValue());
			referenceObject.put(DOI, doi.getValue());

			//Book Chapter
			referenceObject.put(EDITORS, editors.getValue());
			referenceObject.put(BOOK_TITLE, bookTitle.getValue());
			referenceObject.put(BOOK_SUBTITLE, bookSubtitle.getValue());
			referenceObject.put(PAGES_OF_CHAPTER, pagesOfChapter.getValue());

			//Web Page
			referenceObject.put(URL, url.getValue());
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		return referenceObject;
	}

	public boolean isWebPage()
	{
		if(type != WEB_PAGE)
			return false;

		String phrase = HelperFunctions.getReferenceString(this);
		String[] results = phrase.split("\\.");

		return results.length == 0 || results.length == 1 || !results[results.length - 1].equalsIgnoreCase("pdf");
	}

	public boolean hasItalics()
	{
		return italicsStart > -1 && italicsEnd > -1 && italicsStart < italicsEnd;
	}

	@Override
	public int compareTo(@NonNull ReferenceItem another)
	{
		return HelperFunctions.getReferenceString(this).compareTo(HelperFunctions.getReferenceString(another));
	}
}
