package com.au.easyreference.app.references;

import android.app.Application;
import com.au.easyreference.app.utils.HelperFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import static com.au.easyreference.app.utils.Constants.REFERENCE;
import static com.au.easyreference.app.utils.Constants.REFERENCE_ID;
import static com.au.easyreference.app.utils.Constants.REFERENCE_LIST;
import static com.au.easyreference.app.utils.Constants.REFERENCE_LIST_TYPE;

/**
 * @author Marcus Hooper
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
		referenceType = referenceObject.optInt(REFERENCE_LIST_TYPE);

		referenceList = new ArrayList<>();
		JSONArray referenceArray = referenceObject.optJSONArray(REFERENCE_LIST);
		if(referenceArray != null)
		{
			for(int i = 0; i < referenceArray.length(); i++)
				referenceList.add(new ReferenceItem(referenceArray.optJSONObject(i)));
		}
	}
	public ReferenceItem getReferenceForId(String id)
	{
		for(ReferenceItem referenceItem : referenceList)
			if(referenceItem.id.equalsIgnoreCase(id))
				return referenceItem;

		return null;
	}

	public void saveToFile(Application app)
	{
		HelperFunctions.saveStringToFile(HelperFunctions.getReferenceListPath(id, app), toJSON().toString());
	}

	public JSONObject toJSON()
	{
		JSONObject referenceObject = new JSONObject();

		try
		{
			referenceObject.put(REFERENCE_ID, id);
			referenceObject.put(REFERENCE, title);
			referenceObject.put(REFERENCE_LIST_TYPE, referenceType);

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
