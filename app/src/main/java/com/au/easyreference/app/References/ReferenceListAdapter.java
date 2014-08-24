package com.au.easyreference.app.References;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;

import java.util.ArrayList;

/**
 * Created by Marcus on 1/06/2014.
 */
public class ReferenceListAdapter extends ArrayAdapter<ReferenceItem>
{
	private int type;
	private LayoutInflater inflater;
	private int layoutResourceId;
	private ArrayList<ReferenceItem> references;

	public ReferenceListAdapter(Context context, int resource, ArrayList<ReferenceItem> references, LayoutInflater inflater, int type)
	{
		super(context, resource, references);
		this.type = type;
		this.inflater = inflater;
		this.references = references;
		layoutResourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ReferenceItem currentReference = getItem(position);
		View layout = convertView;
		ReferenceHolder holder;

		if(layout == null)
		{
			layout = inflater.inflate(layoutResourceId, parent, false);
			holder = new ReferenceHolder(layout);

			layout.setTag(holder);
		}
		else
			holder = (ReferenceHolder) layout.getTag();

		StringBuilder informationBuilder = new StringBuilder();

		if(currentReference.type != ReferenceItem.NEW)
		{
			if(currentReference.author != null && currentReference.author.length() > 0)
				informationBuilder.append(currentReference.author).append(' ');
			if(currentReference.year != null && currentReference.year.length() > 0)
				informationBuilder.append('(').append(currentReference.year).append("). ");
			if(currentReference.title != null && currentReference.title.length() > 0)
				informationBuilder.append(currentReference.title);
			if(currentReference.subtitle != null && currentReference.subtitle.length() > 0)
				informationBuilder.append(": ").append(currentReference.subtitle).append(". ");
			if(currentReference.location != null && currentReference.location.length() > 0)
				informationBuilder.append(currentReference.location).append(": ");
			if(currentReference.publisher != null && currentReference.publisher.length() > 0)
				informationBuilder.append(currentReference.publisher).append('.');
		}
		else
		informationBuilder.append(getContext().getString(R.string.add_new));

		holder.information.setText(informationBuilder.toString());

		return layout;
	}

	public int getSize()
	{
		return references.size();
	}

	public class ReferenceHolder
	{
		@InjectView(R.id.reference_information)
		public TextView information;

		public ReferenceHolder(View view)
		{
			ButterKnife.inject(this, view);
		}
	}
}
