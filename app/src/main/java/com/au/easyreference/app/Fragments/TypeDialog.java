package com.au.easyreference.app.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.Activities.DialogActivity;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class TypeDialog extends Fragment
{
	public static final String TYPE = "type";

	@InjectView(R.id.types_list)
	protected ListView typesList;

	private ArrayList<String> types;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		types = new ArrayList<>();
		types.add(getString(R.string.book));
		types.add(getString(R.string.journal));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.types_dialog, container, false);
		ButterKnife.inject(this, view);

		setHasOptionsMenu(true);
		((DialogActivity) getActivity()).toolbar.setTitle(getString(R.string.selecty_type));
		((DialogActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.selecty_type));

		TypeAdapter adapter = new TypeAdapter();
		typesList.setAdapter(adapter);
		typesList.setDivider(null);

		typesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent result = new Intent();
				result.putExtra(TYPE, position);
				getActivity().setResult(Activity.RESULT_OK, result);
				getActivity().onBackPressed();
			}
		});

		return view;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				getActivity().setResult(Activity.RESULT_CANCELED);
				getActivity().onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	private class TypeAdapter extends ArrayAdapter<String>
	{
		public TypeAdapter()
		{
			super(getActivity(), R.layout.type_item, types);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if(convertView == null)
				convertView = getActivity().getLayoutInflater().inflate(R.layout.type_item, parent, false);

			ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
			TextView title = (TextView) convertView.findViewById(R.id.title);

			icon.setImageResource(ReferenceItem.getIcon(position));
			icon.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
			title.setText(ReferenceItem.getTitle(position, getActivity()));

			return convertView;
		}
	}
}
