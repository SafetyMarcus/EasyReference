package com.au.easyreference.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.Activities.MainActivity;
import com.au.easyreference.app.References.ReferenceList;
import com.au.easyreference.app.Utils.HelperFunctions;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class ReferenceListAdapter extends ArrayAdapter<ReferenceList> implements UndoAdapter
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

		holder.title.setText(list.get(position).title);
		holder.subtext.setText(HelperFunctions.getReferenceListTypeString(list.get(position).referenceType, getContext()));

		return layout;
	}

	@NonNull
	@Override
	public View getUndoView(int i, @Nullable View convertView, @NonNull ViewGroup parent)
	{
		if(convertView == null)
			convertView = inflater.inflate(R.layout.undo_view, parent, false);

		convertView.getLayoutParams().height = 96;
		return convertView;
	}

	@NonNull
	@Override
	public View getUndoClickView(@NonNull View convertView)
	{
		return convertView.findViewById(R.id.undo_button);
	}

	public class OldReferenceHolder
	{
		@InjectView(R.id.reference_information)
		public TextView title;
		@InjectView(R.id.reference_subtext)
		public TextView subtext;

		public OldReferenceHolder(View view)
		{
			ButterKnife.inject(this, view);
		}
	}
}
