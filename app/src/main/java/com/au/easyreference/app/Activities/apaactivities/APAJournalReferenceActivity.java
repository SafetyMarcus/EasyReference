package com.au.easyreference.app.activities.apaactivities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.BaseAPAReferenceActivity;
import com.au.easyreference.app.references.ReferenceItem;
import com.easygoingapps.annotations.Observe;
import utils.State;

/**
 * @author Marcus Hooper
 */
public class APAJournalReferenceActivity extends BaseAPAReferenceActivity
{
	@Observe(R.id.journal_title)
	public State<String> journalTitle;
	@Observe(R.id.volume_number)
	public State<String> volumeNumber;
	@Observe(R.id.issue)
	public State<String> issue;
	@Observe(R.id.page_no)
	public State<String> pageNo;
	@Observe(R.id.doi)
	public State<String> doi;

	@Bind(R.id.journal_title_label)
	public TextView journalTitleLabel;
	@Bind(R.id.volume_number_label)
	public TextView volumeNoLabel;
	@Bind(R.id.issue_label)
	public TextView issueLabel;
	@Bind(R.id.page_no_label)
	public TextView pageNoLabel;
	@Bind(R.id.doi_label)
	public TextView doiLabel;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apa_journal_reference_layout);
		ButterKnife.bind(this);
		setUpReferenceActivity(ReferenceItem.JOURNAL_REFERENCE);

		journalTitle = currentReference.journalTitle;
		volumeNumber = currentReference.volumeNo;
		issue = currentReference.issue;
		pageNo = currentReference.pageNo;
		doi = currentReference.doi;
		APAJournalReferenceActivityViewBinding.watch(this);

		toolbar.setTitle(getString(R.string.apa_journal_reference));

		journalTitleLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		journalTitleLabel.setOnClickListener(new LabelClickListener());

		volumeNoLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		volumeNoLabel.setOnClickListener(new LabelClickListener());

		issueLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		issueLabel.setOnClickListener(new LabelClickListener());

		pageNoLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		pageNoLabel.setOnClickListener(new LabelClickListener());

		doiLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		doiLabel.setOnClickListener(new LabelClickListener());
	}
}
