package com.au.easyreference.app.Utils;

import android.app.Application;
import android.content.Context;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.References.ReferenceList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Marcus on 10/07/2014.
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
		if(currentReference.author != null && currentReference.author.length() > 0)
			informationBuilder.append(currentReference.author).append(' ');
		if(currentReference.year != null && currentReference.year.length() > 0)
			informationBuilder.append('(').append(currentReference.year).append("). ");
		if(currentReference.title != null && currentReference.title.length() > 0)
			informationBuilder.append(currentReference.title);
		if(currentReference.subtitle != null && currentReference.subtitle.length() > 0)
			informationBuilder.append(": ").append(currentReference.subtitle).append(". ");

		return informationBuilder.toString();
	}

	public static String getAPABookReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));

		if(currentReference.location != null && currentReference.location.length() > 0)
			informationBuilder.append(currentReference.location).append(": ");
		if(currentReference.publisher != null && currentReference.publisher.length() > 0)
			informationBuilder.append(currentReference.publisher).append('.');

		return informationBuilder.toString();
	}

	public static String getAPABookChapterReferenceString(ReferenceItem currentReference, Context context)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));

		if(currentReference.editors != null && currentReference.editors.length() > 0)
			informationBuilder.append(context.getString(R.string.in))
					.append(' ')
					.append(currentReference.editors)
					.append(". ")
					.append(context.getString(R.string.eds))
					.append(", ");
		if(currentReference.bookTitle != null && currentReference.bookTitle.length() > 0)
			informationBuilder.append(currentReference.bookTitle);
		if(currentReference.bookSubtitle != null && currentReference.bookSubtitle.length() > 0)
			informationBuilder
					.append(": ")
					.append(currentReference.bookSubtitle);
		if(currentReference.pagesOfChapter != null && currentReference.pagesOfChapter.length() > 0)
			informationBuilder.append("(")
					.append(context.getString(R.string.pp))
					.append(currentReference.pagesOfChapter)
					.append("). ");
		if(currentReference.location != null && currentReference.location.length() > 0)
			informationBuilder.append(currentReference.location).append(": ");
		if(currentReference.publisher != null && currentReference.publisher.length() > 0)
			informationBuilder.append(currentReference.publisher).append('.');

		return informationBuilder.toString();
	}

	public static String getAPAJournalReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));

		if(currentReference.journalTitle != null && currentReference.journalTitle.length() > 0)
			informationBuilder.append(' ').append(currentReference.journalTitle).append(", ");
		if(currentReference.journalTitle != null && currentReference.journalTitle.length() > 0)
			informationBuilder.append(currentReference.volumeNo);
		if(currentReference.issue != null && currentReference.issue.length() > 0)
			informationBuilder.append('(').append(currentReference.issue).append("), ");
		if(currentReference.pageNo != null && currentReference.pageNo.length() > 0)
			informationBuilder.append(currentReference.pageNo).append('.');
		if(currentReference.doi != null && currentReference.doi.length() > 0)
			informationBuilder.append(" doi:").append(currentReference.doi);

		return informationBuilder.toString();
	}

	public static String getAPAWebPageReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));

		if(currentReference.url != null && currentReference.url.length() > 0)
			informationBuilder.append("Retrieved from ").append(currentReference.url);

		return informationBuilder.toString();
	}
}
