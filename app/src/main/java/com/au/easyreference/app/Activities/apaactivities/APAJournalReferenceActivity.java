package com.au.easyreference.app.activities.apaactivities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.BaseAPAReferenceActivity;
import com.au.easyreference.app.references.ReferenceItem;

/**
 * @author Marcus Hooper
 */
public class APAJournalReferenceActivity extends BaseAPAReferenceActivity
{
	@Bind(R.id.journal_title_label)
	public TextView journalTitleLabel;
	@Bind(R.id.journal_title)
	public EditText journalTitle;
	@Bind(R.id.volume_number_label)
	public TextView volumeNoLabel;
	@Bind(R.id.volume_number)
	public EditText volumeNumber;
	@Bind(R.id.issue_label)
	public TextView issueLabel;
	@Bind(R.id.issue)
	public EditText issue;
	@Bind(R.id.page_no_label)
	public TextView pageNoLabel;
	@Bind(R.id.page_no)
	public EditText pageNo;
	@Bind(R.id.doi_label)
	public TextView doiLabel;
	@Bind(R.id.doi)
	public EditText doi;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apa_journal_reference_layout);
		ButterKnife.bind(this);
		setUpReferenceActivity(ReferenceItem.JOURNAL_REFERENCE);

		toolbar.setTitle(getString(R.string.apa_journal_reference));

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
			setUpView();
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

	public void setUpView()
	{
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
