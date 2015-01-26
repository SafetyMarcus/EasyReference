package com.au.easyreference.app.activities.apaactivities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
	protected void onCreate(Bundle savedInstanceState)
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
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, SAVE, SAVE, getString(R.string.save)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case SAVE:
				save();
				onBackPressed();

			default:
				return super.onOptionsItemSelected(item);
		}
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
