package com.au.easyreference.app.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.References.ReferenceList;
import com.au.easyreference.app.Utils.ERApplication;

/**
 * @author Marcus Hooper
 */
public class BaseAPAReferenceDialogFragment extends Fragment
{
	public static final String KEY_LIST_ID = "key_list_id";
	public static final String KEY_ID = "key_id";

	@InjectView(R.id.author)
	public EditText author;
	@InjectView(R.id.year)
	public EditText year;
	@InjectView(R.id.title)
	public EditText title;
	@InjectView(R.id.subtitle)
	public EditText subtitle;

	@InjectView(R.id.cancel)
	protected Button cancel;
	@InjectView(R.id.save)
	protected Button save;

	public ReferenceList referenceList;
	public ReferenceItem currentReference;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		cancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				getActivity().onBackPressed();
			}
		});

		Bundle args = getArguments();

		if(args != null)
		{
			for(ReferenceList list : ERApplication.referenceLists)
				if(list.id.equalsIgnoreCase(args.getString(KEY_LIST_ID)))
					referenceList = list;

			if(args.containsKey(KEY_ID))
				setUpView(args.getString(KEY_ID));
		}

		return null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				getActivity().onBackPressed();
		}

		return super.onOptionsItemSelected(item);
	}

	public void setUpView(String id)
	{
		for(ReferenceItem reference : referenceList.referenceList)
		{
			currentReference = reference;
			if(reference.id.equalsIgnoreCase(id))
			{
				if(reference.author != null && reference.author.length() > 0)
					author.setText(reference.author);
				if(reference.year != null && reference.year.length() > 0)
					year.setText(reference.year);
				if(reference.title != null && reference.title.length() > 0)
					title.setText(reference.title);
				if(reference.subtitle != null && reference.subtitle.length() > 0)
					subtitle.setText(reference.subtitle);

				break;
			}
		}
	}
}
