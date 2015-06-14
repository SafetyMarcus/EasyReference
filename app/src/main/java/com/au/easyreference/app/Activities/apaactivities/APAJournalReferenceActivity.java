package com.au.easyreference.app.activities.apaactivities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
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
	@InjectView(R.id.journal_title_label)
	public TextView journalTitleLabel;
	@InjectView(R.id.journal_title)
	public EditText journalTitle;
	@InjectView(R.id.volume_number_label)
	public TextView volumeNoLabel;
	@InjectView(R.id.volume_number)
	public EditText volumeNumber;
	@InjectView(R.id.issue_label)
	public TextView issueLabel;
	@InjectView(R.id.issue)
	public EditText issue;
	@InjectView(R.id.page_no_label)
	public TextView pageNoLabel;
	@InjectView(R.id.page_no)
	public EditText pageNo;
	@InjectView(R.id.doi_label)
	public TextView doiLabel;
	@InjectView(R.id.doi)
	public EditText doi;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apa_journal_reference_layout);
		ButterKnife.inject(this);
		setUpReferenceActivity();

		toolbar.setTitle(getString(R.string.apa_journal_reference));

		if(currentReference == null)
		{
			currentReference = new ReferenceItem(ReferenceItem.JOURNAL_REFERENCE);
			referenceList.referenceList.add(currentReference);
		}

		journalTitleLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		journalTitleLabel.setOnClickListener(new LabelClickListener());

		volumeNoLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		volumeNoLabel.setOnClickListener(new LabelClickListener());

		issueLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		issueLabel.setOnClickListener(new LabelClickListener());

		pageNoLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		pageNoLabel.setOnClickListener(new LabelClickListener());

		doiLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		doiLabel.setOnClickListener(new LabelClickListener());

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
			if(!TextUtils.isEmpty(currentReference.journalTitle))
				journalTitle.setText(currentReference.journalTitle);
			if(!TextUtils.isEmpty(currentReference.volumeNo))
				volumeNumber.setText(currentReference.volumeNo);
			if(!TextUtils.isEmpty(currentReference.issue))
				issue.setText(currentReference.issue);
			if(!TextUtils.isEmpty(currentReference.pageNo))
				pageNo.setText(currentReference.pageNo);
			if(!TextUtils.isEmpty(currentReference.doi))
				doi.setText(currentReference.doi);
		}
	}
}
