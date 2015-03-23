package com.au.easyreference.app.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.au.easyreference.app.R;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author Marcus Hooper
 */
public class HelperFunctions
{

	public static void saveStringToFile(String absoluteFilePath, String stringToSave)
	{
		File file = new File(absoluteFilePath);
		File directory = new File(file.getParent());
		directory.mkdirs();

		BufferedWriter bw = null;

		try
		{
			file.createNewFile();

			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"), 1024);
			bw.write(stringToSave);
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(bw != null)
					bw.close();
			}
			catch(IOException e)
			{
			}
		}
	}

	public static String getReferenceListBasePath(Application app)
	{
		StringBuilder path = new StringBuilder(app.getFilesDir().getAbsolutePath());
		path.append("/references/");

		return path.toString();
	}

	public static String getReferenceListPath(String itemId, Application app)
	{
		StringBuilder newPath = new StringBuilder(getReferenceListBasePath(app));
		newPath.append(itemId);
		newPath.append(".rl");
		return newPath.toString();
	}

	public static ReferenceList getReferenceListForId(String id)
	{
		for(ReferenceList list : ERApplication.referenceLists)
		{
			if(list.id.equalsIgnoreCase(id))
				return list;
		}

		return null;
	}

	public static String getReferenceListTypeString(int referenceType, Context context)
	{
		if(referenceType == ReferenceList.APA)
			return context.getString(R.string.apa_reference_list);
		else if(referenceType == ReferenceList.HARVARD)
			return context.getString(R.string.harvard_reference_list);
		else
			return "";
	}

	private static String getAPAReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder();

		if(!TextUtils.isEmpty(currentReference.author))
			informationBuilder.append(currentReference.author).append(' ');
		if(!TextUtils.isDigitsOnly(currentReference.year))
			informationBuilder.append('(').append(currentReference.year).append("). ");
		if(!TextUtils.isEmpty(currentReference.title))
		{
			currentReference.italicsStart = informationBuilder.length() - 1;
			informationBuilder.append(currentReference.title);
		}
		if(!TextUtils.isEmpty(currentReference.subtitle))
			informationBuilder.append(": ").append(currentReference.subtitle).append(". ");

		currentReference.italicsEnd = informationBuilder.length();

		return informationBuilder.toString();
	}

	public static String getAPABookReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));

		if(!TextUtils.isEmpty(currentReference.location))
			informationBuilder.append(currentReference.location).append(": ");
		if(!TextUtils.isEmpty(currentReference.publisher))
			informationBuilder.append(currentReference.publisher).append('.');

		return informationBuilder.toString();
	}

	public static String getAPABookChapterReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));
		Context context = ERApplication.getInstance();

		if(!TextUtils.isEmpty(currentReference.editors))
			informationBuilder.append(context.getString(R.string.in))
					.append(' ')
					.append(currentReference.editors)
					.append(". ")
					.append(context.getString(R.string.eds))
					.append(", ");
		if(!TextUtils.isEmpty(currentReference.bookTitle))
			informationBuilder.append(currentReference.bookTitle);
		if(!TextUtils.isEmpty(currentReference.bookSubtitle))
			informationBuilder
					.append(": ")
					.append(currentReference.bookSubtitle);
		if(!TextUtils.isEmpty(currentReference.pagesOfChapter))
			informationBuilder.append("(")
					.append(context.getString(R.string.pp))
					.append(currentReference.pagesOfChapter)
					.append("). ");
		if(!TextUtils.isEmpty(currentReference.location))
			informationBuilder.append(currentReference.location).append(": ");
		if(!TextUtils.isEmpty(currentReference.publisher))
			informationBuilder.append(currentReference.publisher).append('.');

		return informationBuilder.toString();
	}

	public static String getAPAJournalReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));

		if(!TextUtils.isEmpty(currentReference.journalTitle))
			informationBuilder.append(' ').append(currentReference.journalTitle).append(", ");
		if(!TextUtils.isEmpty(currentReference.volumeNo))
			informationBuilder.append(currentReference.volumeNo);
		if(!TextUtils.isEmpty(currentReference.issue))
			informationBuilder.append('(').append(currentReference.issue).append("), ");
		if(!TextUtils.isEmpty(currentReference.pageNo))
			informationBuilder.append(currentReference.pageNo).append('.');
		if(!TextUtils.isEmpty(currentReference.doi))
			informationBuilder.append(" doi:").append(currentReference.doi);

		return informationBuilder.toString();
	}

	public static String getAPAWebPageReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));

		if(!TextUtils.isEmpty(currentReference.url))
			informationBuilder.append("Retrieved from ").append(currentReference.url);

		return informationBuilder.toString();
	}

	public static String getReferenceString(ReferenceItem currentReference)
	{
		if(currentReference.type == ReferenceItem.BOOK_REFERENCE)
			return getAPABookReferenceString(currentReference);
		else if(currentReference.type == ReferenceItem.BOOK_CHAPTER)
			return getAPABookChapterReferenceString(currentReference);
		else if(currentReference.type == ReferenceItem.JOURNAL_REFERENCE)
			return getAPAJournalReferenceString(currentReference);
		else if(currentReference.type == ReferenceItem.WEB_PAGE)
			return getAPAWebPageReferenceString(currentReference);

		return "";
	}
}
