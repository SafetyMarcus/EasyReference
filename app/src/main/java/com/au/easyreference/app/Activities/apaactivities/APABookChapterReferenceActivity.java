package com.au.easyreference.app.activities.apaactivities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.BaseAPAReferenceActivity;
import com.au.easyreference.app.references.ReferenceItem;
import com.easygoingapps.ThePolice;
import com.easygoingapps.annotations.Observe;
import com.easygoingapps.utils.State;

/**
 * @author Marcus Hooper
 */
public class APABookChapterReferenceActivity extends BaseAPAReferenceActivity
{
	@Observe(R.id.book_title)
	public State<String> bookTitle;
	@Observe(R.id.book_subtitle)
	public State<String> bookSubtitle;
	@Observe(R.id.pages_of_chapter)
	public State<String> pagesOfChapter;
	@Observe(R.id.editors)
	public State<String> editors;

	@Bind(R.id.book_title_label)
	public TextView bookTitleLabel;
	@Bind(R.id.book_subtitle_label)
	public TextView bookSubtitleLabel;
	@Bind(R.id.pages_of_chapter_label)
	public TextView pagesOfChapterLabel;
	@Bind(R.id.editors_label)
	public TextView editorsLabel;

	@Bind(R.id.location_label)
	public TextView locationLabel;
	@Bind(R.id.publisher_label)
	public TextView publisherLabel;

	@Observe(R.id.location)
	public State<String> location;
	@Observe(R.id.publisher)
	public State<String> publisher;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apa_book_chapter_reference_layout);
		ButterKnife.bind(this);
		setUpReferenceActivity(ReferenceItem.BOOK_CHAPTER);
		bookTitle = currentReference.bookTitle;
		bookSubtitle = currentReference.bookSubtitle;
		pagesOfChapter = currentReference.pagesOfChapter;
		editors = currentReference.editors;
		location = currentReference.location;
		publisher = currentReference.publisher;
		ThePolice.watch(this);

		toolbar.setTitle(getString(R.string.apa_book_chapter_reference));

		bookTitleLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		bookTitleLabel.setOnClickListener(new LabelClickListener());

		bookSubtitleLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		bookSubtitleLabel.setOnClickListener(new LabelClickListener());

		pagesOfChapterLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		pagesOfChapterLabel.setOnClickListener(new LabelClickListener());

		editorsLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		editorsLabel.setOnClickListener(new LabelClickListener());

		locationLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		locationLabel.setOnClickListener(new LabelClickListener());

		publisherLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		publisherLabel.setOnClickListener(new LabelClickListener());
	}
}
