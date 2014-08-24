package com.au.easyreference.app.References;

import android.app.Application;
import com.au.easyreference.app.Utils.HelperFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import static com.au.easyreference.app.Utils.Constants.REFERENCE;
import static com.au.easyreference.app.Utils.Constants.REFERENCE_ID;
import static com.au.easyreference.app.Utils.Constants.REFERENCE_LIST;
import static com.au.easyreference.app.Utils.Constants.REFERENCE_TYPE;

/**
 * Created by Marcus on 1/06/2014.
 */
public class ReferenceList
{
	public final static int APA = 0;
	public final static int HARVARD = 1;

	public String id;
	public String title;
	public int referenceType;
	public ArrayList<ReferenceItem> referenceList;

	public ReferenceList(String title, int referenceType, ArrayList<ReferenceItem> list)
	{
		id = UUID.randomUUID().toString();
		this.title = title;
		this.referenceType = referenceType;
		referenceList = list;
	}

	public ReferenceList(JSONObject referenceObject)
	{
		id = referenceObject.optString(REFERENCE_ID);
		title = referenceObject.optString(REFERENCE);
		referenceType = referenceObject.optInt(REFERENCE_TYPE);

		referenceList = new ArrayList<ReferenceItem>();
		JSONArray referenceArray = referenceObject.optJSONArray(REFERENCE_LIST);
		for(int i = 0; i < referenceArray.length(); i++)
		{
			referenceList.add(new ReferenceItem(referenceArray.optJSONObject(i)));
		}
	}

	public void saveToFile(ReferenceList referenceList, Application app)
	{
		HelperFunctions.saveStringToFile(HelperFunctions.getReferenceListPath(referenceList.id, app), referenceList.toJSON().toString());
	}

	public JSONObject toJSON()
	{
		JSONObject referenceObject = new JSONObject();

		try
		{
			referenceObject.put(REFERENCE_ID, id);
			referenceObject.put(REFERENCE, title);
			referenceObject.put(REFERENCE_TYPE, referenceType);

			if(referenceList != null && referenceList.size() > 0)
			{
				JSONArray referenceItemArray = new JSONArray();

				for(ReferenceItem item : referenceList)
					referenceItemArray.put(item.toJSON());

				referenceObject.put(REFERENCE_LIST, referenceItemArray);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		return referenceObject;
	}
}
