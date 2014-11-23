package com.au.easyreference.app.Utils;

import android.app.Application;
import android.util.Log;
import com.au.easyreference.app.References.ReferenceList;
import com.squareup.otto.Bus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class ERApplication extends Application
{
	public static ArrayList<ReferenceList> referenceLists;
	public static final Bus BUS = new Bus();

	@Override
	public void onCreate()
	{
		super.onCreate();
		referenceLists = new ArrayList<ReferenceList>();
	}

	public void retrieveReferencesService()
	{
		referenceLists.clear();
		File referencesFolder = new File(HelperFunctions.getReferenceListBasePath(this));

		if(!referencesFolder.exists())
			return;

		File[] files = referencesFolder.listFiles();

		if(files != null && files.length > 0)
		{
			for(File file : files)
			{
				if(file.getName().endsWith(".rl"));
				{
					try
					{
						InputStream inputStream = new FileInputStream(file.getPath());
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
						StringBuilder stringBuilder = new StringBuilder();
						String line;

						try
						{
							while((line = bufferedReader.readLine()) != null)
							{
								stringBuilder.append(line);
							}
						}
						catch(IOException e)
						{
							e.printStackTrace();
						}

						JSONObject referenceObject = new JSONObject(stringBuilder.toString());
						referenceLists.add(new ReferenceList(referenceObject));
					}
					catch(FileNotFoundException e)
					{
						Log.d("ERApplication", "File not found:");
						e.printStackTrace();
					}
					catch(JSONException e)
					{
						Log.d("ERApplication", "JSON Exception:");
						e.printStackTrace();
					}
				}
			}
		}
	}
}
