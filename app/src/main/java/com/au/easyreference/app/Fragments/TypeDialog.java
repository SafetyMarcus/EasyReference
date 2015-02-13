package com.au.easyreference.app.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.events.TypeResultEvent;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.utils.ERApplication;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class TypeDialog extends Fragment
{
	public static final String SEARCH = "search";

	@InjectView(R.id.types_list)
	protected ListView typesList;

	private ArrayList<String> types;
	private boolean shouldSearch = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		types = new ArrayList<>();
		types.add(getString(R.string.book));
		types.add(getString(R.string.journal));
		types.add(getString(R.string.book_chapter));
		types.add(getString(R.string.web_page_or_document));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.types_dialog, container, false);
		ButterKnife.inject(this, view);

		((ContainerDialogFragment) getParentFragment()).toolbar.setTitle(getString(R.string.selecty_type));

		TypeAdapter adapter = new TypeAdapter();
		typesList.setAdapter(adapter);
		typesList.setDivider(null);

		if(getArguments() != null)
			shouldSearch = getArguments().getBoolean(SEARCH, false);

		typesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if(!shouldSearch)
				{
					ERApplication.BUS.post(new TypeResultEvent(position));
					((ContainerDialogFragment) getParentFragment()).onBackPressed();
				}
				else
				{
					SearchDialog dialog = new SearchDialog();
					Bundle args = new Bundle();
					args.putInt(SearchDialog.TYPE, position);
					dialog.setArguments(args);

					((ContainerDialogFragment) getParentFragment()).showChild(dialog);
				}
			}
		});

		return view;
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
