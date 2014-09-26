package com.au.easyreference.app.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.Activities.ReferenceListActivity;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.Utils.ERApplication;

/**
 * @author Marcus Hooper
 */
public class APAJournalReferenceDialogFragment extends BaseAPAReferenceDialogFragment
{
	public static final String KEY_ID = "key_id";

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
		View layout = getActivity().getLayoutInflater().inflate(R.layout.apa_journal_reference_layout, null);
		ButterKnife.inject(this, layout);

		super.onCreateView(inflater, container, savedInstanceState);

		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(currentReference == null)
				{
					ReferenceItem newItem = new ReferenceItem(author.getText().toString(), year.getText().toString(),
							title.getText().toString(), subtitle.getText().toString(), journalTitle.getText().toString(),
							volumeNumber.getText().toString(), issue.getText().toString(), pageNo.getText().toString(),
							doi.getText().toString(), ReferenceItem.JOURNAL_REFERENCE);

					if(listener != null)
						listener.onReferenceCreated(newItem);
				}
				else
				{
					currentReference.author = author.getText().toString();
					currentReference.year = year.getText().toString();
					currentReference.title = title.getText().toString();
					currentReference.subtitle = subtitle.getText().toString();
					currentReference.issue = issue.getText().toString();
					currentReference.pageNo = pageNo.getText().toString();
					currentReference.doi = doi.getText().toString();

					if(listener != null)
						listener.onReferenceCreated(currentReference);
				}

				dismiss();
			}
		});

		Bundle args = getArguments();

		if(args != null)
			setUpView(args.getString(KEY_ID));

		listener = ((ReferenceListActivity) getActivity()).apaListener;

		return layout;
	}

	public void setUpView(String id)
	{
		super.setUpView(id);
		for(ReferenceItem reference : ERApplication.allReferences)
		{
			currentReference = reference;
			if(reference.id.equalsIgnoreCase(id))
			{
				if(reference.issue != null && reference.issue.length() > 0)
					issue.setText(reference.issue);
				if(reference.pageNo != null && reference.pageNo.length() > 0)
					pageNo.setText(reference.pageNo);
				if(reference.doi != null && reference.doi.length() > 0)
					doi.setText(reference.doi);
			}
			break;
		}
	}
}
