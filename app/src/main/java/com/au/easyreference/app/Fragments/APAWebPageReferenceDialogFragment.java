package com.au.easyreference.app.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.Activities.DialogActivity;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;

/**
 * @author Marcus Hooper
 */
public class APAWebPageReferenceDialogFragment extends BaseAPAReferenceDialogFragment
{
	@InjectView(R.id.url)
	protected EditText url;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View layout = getActivity().getLayoutInflater().inflate(R.layout.apa_web_page_reference_layout, container, false);
		ButterKnife.inject(this, layout);

		super.onCreateView(inflater, container, savedInstanceState);
		((DialogActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.apa_book_reference));
		((DialogActivity) getActivity()).toolbar.setTitle(getString(R.string.apa_book_reference));

		setHasOptionsMenu(true);

		if(currentReference == null)
		{
			currentReference = new ReferenceItem(ReferenceItem.BOOK_REFERENCE);
			referenceList.referenceList.add(currentReference);
		}

		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				save();
				getActivity().onBackPressed();
			}
		});

		Bundle args = getArguments();

		if(args != null && args.containsKey(KEY_ID))
			setUpView(args.getString(KEY_ID));

		return layout;
	}

	@Override
	public void save()
	{
		super.save();
		currentReference.url = url.getText().toString();
	}

	public void setUpView(String id)
	{
		super.setUpView(id);
		if(currentReference != null)
		{
			if(currentReference.url != null && currentReference.url.length() > 0)
				url.setText(currentReference.url);
		}
	}
}
