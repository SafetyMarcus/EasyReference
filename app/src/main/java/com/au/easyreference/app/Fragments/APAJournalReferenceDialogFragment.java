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
			referenceList.referenceList.remove(referenceList.referenceList.size() - 1);
			referenceList.referenceList.add(currentReference);
			referenceList.referenceList.add(new ReferenceItem(ReferenceItem.NEW));
		}

		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				currentReference.author = author.getText().toString();
				currentReference.year = year.getText().toString();
				currentReference.title = title.getText().toString();
				currentReference.subtitle = subtitle.getText().toString();
				currentReference.issue = issue.getText().toString();
				currentReference.pageNo = pageNo.getText().toString();
				currentReference.doi = doi.getText().toString();
				currentReference.journalTitle = journalTitle.getText().toString();
				currentReference.volumeNo = volumeNumber.getText().toString();

				getActivity().onBackPressed();
			}
		});

		Bundle args = getArguments();

		if(args != null && args.containsKey(KEY_ID))
			setUpView(args.getString(KEY_ID));

		return layout;
	}

	public void setUpView(String id)
	{
		super.setUpView(id);
		for(ReferenceItem reference : referenceList.referenceList)
		{
			if(reference.id.equalsIgnoreCase(id))
			{
				if(reference.issue != null && reference.issue.length() > 0)
					issue.setText(reference.issue);
				if(reference.pageNo != null && reference.pageNo.length() > 0)
					pageNo.setText(reference.pageNo);
				if(reference.doi != null && reference.doi.length() > 0)
					doi.setText(reference.doi);

				break;
			}
		}
	}
}
