package com.au.easyreference.app.utils;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.au.easyreference.app.R;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static com.au.easyreference.app.references.ReferenceItem.BOOK_REFERENCE;
import static com.au.easyreference.app.references.ReferenceItem.WEB_PAGE;

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

	@Nullable
	public static ReferenceList getReferenceListForId(String id)
	{
		for(ReferenceList list : ERApplication.referenceLists)
		{
			if(list.id.equalsIgnoreCase(id))
				return list;
		}

		return null;
	}

	public static float convertIntToDp(int amount)
	{
		return ERApplication.getInstance().getResources().getDisplayMetrics().density * amount;
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

		if(!TextUtils.isEmpty(currentReference.author.getValue()))
			informationBuilder.append(currentReference.author.getValue()).append(' ');
		if(!TextUtils.isEmpty(currentReference.year.getValue()))
			informationBuilder.append('(').append(currentReference.year.getValue()).append("). ");
		if(!TextUtils.isEmpty(currentReference.title.getValue()))
		{
			if(currentReference.type == BOOK_REFERENCE || currentReference.type == WEB_PAGE)
				currentReference.italicsStart = informationBuilder.length() - 1;

			informationBuilder.append(currentReference.title.getValue());

			if(currentReference.type == BOOK_REFERENCE && TextUtils.isEmpty(currentReference.subtitle.getValue()) || currentReference.type == WEB_PAGE)
				currentReference.italicsEnd = informationBuilder.length();

			if(TextUtils.isEmpty(currentReference.subtitle.getValue()))
				informationBuilder.append('.');
		}
		if(!TextUtils.isEmpty(currentReference.subtitle.getValue()))
		{
			if(currentReference.type == BOOK_REFERENCE && TextUtils.isEmpty(currentReference.title.getValue()))
				currentReference.italicsStart = informationBuilder.length() - 1;

			informationBuilder.append(": ").append(currentReference.subtitle.getValue()).append('.');

			if(currentReference.type == BOOK_REFERENCE)
				currentReference.italicsEnd = informationBuilder.length();
		}

		return informationBuilder.toString();
	}

	public static String getAPABookReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));
		informationBuilder.append(' ');

		if(!TextUtils.isEmpty(currentReference.location.getValue()))
			informationBuilder.append(currentReference.location.getValue()).append(": ");
		if(!TextUtils.isEmpty(currentReference.publisher.getValue()))
			informationBuilder.append(currentReference.publisher.getValue()).append('.');

		return informationBuilder.toString();
	}

	public static String getAPABookChapterReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));
		informationBuilder.append(' ');
		Context context = ERApplication.getInstance();

		if(!TextUtils.isEmpty(currentReference.editors.getValue()))
			informationBuilder.append(context.getString(R.string.in))
					.append(' ')
					.append(currentReference.editors.getValue())
					.append(' ')
					.append(context.getString(R.string.eds))
					.append(", ");
		if(!TextUtils.isEmpty(currentReference.bookTitle.getValue()))
		{
			currentReference.italicsStart = informationBuilder.length() - 1;
			informationBuilder.append(currentReference.bookTitle.getValue());

			if(TextUtils.isEmpty(currentReference.bookSubtitle.getValue()))
				currentReference.italicsEnd = informationBuilder.length();
		}
		if(!TextUtils.isEmpty(currentReference.bookSubtitle.getValue()))
		{
			if(TextUtils.isEmpty(currentReference.bookTitle.getValue()))
				currentReference.italicsStart = informationBuilder.length() - 1;

			informationBuilder.append(": ")
					.append(currentReference.bookSubtitle.getValue());
			currentReference.italicsEnd = informationBuilder.length();
		}
		if(!TextUtils.isEmpty(currentReference.pagesOfChapter.getValue()))
			informationBuilder.append(" (")
					.append(context.getString(R.string.pp))
					.append(currentReference.pagesOfChapter.getValue())
					.append(").");
		if(!TextUtils.isEmpty(currentReference.location.getValue()))
			informationBuilder.append(' ').append(currentReference.location).append(": ");
		if(!TextUtils.isEmpty(currentReference.publisher.getValue()))
			informationBuilder.append(currentReference.publisher).append('.');

		return informationBuilder.toString();
	}

	public static String getAPAJournalReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));
		informationBuilder.append(' ');

		if(!TextUtils.isEmpty(currentReference.journalTitle.getValue()))
		{
			currentReference.italicsStart = informationBuilder.length() - 1;
			informationBuilder.append(currentReference.journalTitle.getValue());

			if(TextUtils.isEmpty(currentReference.volumeNo.getValue()))
				currentReference.italicsEnd = informationBuilder.length();
		}
		if(!TextUtils.isEmpty(currentReference.volumeNo.getValue()))
		{
			if(TextUtils.isEmpty(currentReference.journalTitle.getValue()))
				currentReference.italicsStart = informationBuilder.length() - 1;

			informationBuilder.append(' ').append(currentReference.volumeNo.getValue());
			currentReference.italicsEnd = informationBuilder.length();
		}
		if(!TextUtils.isEmpty(currentReference.issue.getValue()))
			informationBuilder.append('(').append(currentReference.issue.getValue()).append("), ");
		if(!TextUtils.isEmpty(currentReference.pageNo.getValue()))
			informationBuilder.append(currentReference.pageNo.getValue()).append('.');
		if(!TextUtils.isEmpty(currentReference.doi.getValue()))
			informationBuilder.append(" doi:").append(currentReference.doi.getValue());

		return informationBuilder.toString();
	}

	public static String getAPAWebPageReferenceString(ReferenceItem currentReference)
	{
		StringBuilder informationBuilder = new StringBuilder(getAPAReferenceString(currentReference));
		informationBuilder.append(' ');

		if(!TextUtils.isEmpty(currentReference.url.getValue()))
			informationBuilder.append("Retrieved from ").append(currentReference.url.getValue());

		return informationBuilder.toString();
	}

	public static String getReferenceString(ReferenceItem currentReference)
	{
		if(currentReference.type == BOOK_REFERENCE)
			return getAPABookReferenceString(currentReference);
		else if(currentReference.type == ReferenceItem.BOOK_CHAPTER)
			return getAPABookChapterReferenceString(currentReference);
		else if(currentReference.type == ReferenceItem.JOURNAL_REFERENCE)
			return getAPAJournalReferenceString(currentReference);
		else if(currentReference.type == WEB_PAGE)
			return getAPAWebPageReferenceString(currentReference);

		return "";
	}
}
