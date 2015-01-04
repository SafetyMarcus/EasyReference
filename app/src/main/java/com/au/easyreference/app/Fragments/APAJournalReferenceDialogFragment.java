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
public class APAJournalReferenceDialogFragment extends BaseAPAReferenceDialogFragment
{
	@InjectView(R.id.journal_title)
	public EditText journalTitle;
	@InjectView(R.id.volume_number)
	public EditText volumeNumber;
	@InjectView(R.id.issue)
	public EditText issue;
	@InjectView(R.id.pageNo)
	public EditText pageNo;
	@InjectView(R.id.doi)
	public EditText doi;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View layout = getActivity().getLayoutInflater().inflate(R.layout.apa_journal_reference_layout, container, false);
		ButterKnife.inject(this, layout);

		super.onCreateView(inflater, container, savedInstanceState);
		((DialogActivity) getActivity()).toolbar.setTitle(getString(R.string.apa_journal_reference));

		setHasOptionsMenu(true);

		if(currentReference == null)
		{
			currentReference = new ReferenceItem(ReferenceItem.JOURNAL_REFERENCE);
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
		currentReference.issue = issue.getText().toString();
		currentReference.pageNo = pageNo.getText().toString();
		currentReference.doi = doi.getText().toString();
		currentReference.journalTitle = journalTitle.getText().toString();
		currentReference.volumeNo = volumeNumber.getText().toString();
	}

	public void setUpView(String id)
	{
		super.setUpView(id);
		if(currentReference != null)
		{
			if(currentReference.issue != null && currentReference.issue.length() > 0)
				issue.setText(currentReference.issue);
			if(currentReference.pageNo != null && currentReference.pageNo.length() > 0)
				pageNo.setText(currentReference.pageNo);
			if(currentReference.doi != null && currentReference.doi.length() > 0)
				doi.setText(currentReference.doi);

		}
	}
}
