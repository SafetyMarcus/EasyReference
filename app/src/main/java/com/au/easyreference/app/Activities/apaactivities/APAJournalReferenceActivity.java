package com.au.easyreference.app.activities.apaactivities;

import android.os.Bundle;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.BaseAPAReferenceActivity;
import com.au.easyreference.app.references.ReferenceItem;

/**
 * @author Marcus Hooper
 */
public class APAJournalReferenceActivity extends BaseAPAReferenceActivity
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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.apa_journal_reference_layout);
		ButterKnife.inject(this);

		toolbar.setTitle(getString(R.string.apa_journal_reference));
		super.onCreate(savedInstanceState);

		if(currentReference == null)
		{
			currentReference = new ReferenceItem(ReferenceItem.JOURNAL_REFERENCE);
			referenceList.referenceList.add(currentReference);
		}

		Bundle args = getIntent().getExtras();

		if(args != null && args.containsKey(KEY_ID))
			setUpView(args.getString(KEY_ID));
	}

	@Override
	public void onBackPressed()
	{
		save();
		super.onBackPressed();
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
			if(!currentReference.journalTitle.isEmpty())
				journalTitle.setText(currentReference.journalTitle);
			if(!currentReference.volumeNo.isEmpty())
				volumeNumber.setText(currentReference.volumeNo);
			if(!currentReference.issue.isEmpty())
				issue.setText(currentReference.issue);
			if(!currentReference.pageNo.isEmpty())
				pageNo.setText(currentReference.pageNo);
			if(!currentReference.doi.isEmpty())
				doi.setText(currentReference.doi);
		}
	}
}
