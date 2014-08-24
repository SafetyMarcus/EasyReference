package com.au.easyreference.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.Activities.MainActivity;
import com.au.easyreference.app.References.ReferenceList;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class ReferenceListAdapter extends ArrayAdapter<ReferenceList>
{
	private LayoutInflater inflater;
	private ArrayList<ReferenceList> list;
	private int layoutResourceId;

	public ReferenceListAdapter(Context context, int resource, ArrayList<ReferenceList> referenceLists)
	{
		super(context, resource, referenceLists);
		this.inflater = ((MainActivity) context).getLayoutInflater();
		this.list = referenceLists;
		layoutResourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View layout = convertView;
		OldReferenceHolder holder;

		if(layout == null)
		{
			layout = inflater.inflate(layoutResourceId, parent, false);
			holder = new OldReferenceHolder(layout);

			layout.setTag(holder);
		}
		else
			holder = (OldReferenceHolder) layout.getTag();

		holder.information.setText(list.get(position).title);

		return layout;
	}

	public class OldReferenceHolder
	{
		@InjectView(R.id.reference_information)
		public TextView information;

		public OldReferenceHolder(View view)
		{
			ButterKnife.inject(this, view);
		}
	}
}
