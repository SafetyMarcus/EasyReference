package com.au.easyreference.app.Utils;

import android.app.Application;
import android.content.Context;
import com.au.easyreference.app.R;
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
}
