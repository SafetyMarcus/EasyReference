package com.au.easyreference.app.activities.apaactivities;

import android.graphics.PorterDuff;
import android.os.Bundle;
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
public class APABookChapterReferenceActivity extends BaseAPAReferenceActivity
{
	@Bind(R.id.book_title_label)
	public TextView bookTitleLabel;
	@Bind(R.id.book_title)
	public EditText bookTitle;
	@Bind(R.id.book_subtitle_label)
	public TextView bookSubtitleLabel;
	@Bind(R.id.book_subtitle)
	public EditText bookSubtitle;
	@Bind(R.id.pages_of_chapter_label)
	public TextView pagesOfChapterLabel;
	@Bind(R.id.pages_of_chapter)
	public EditText pagesOfChapter;
	@Bind(R.id.editors_label)
	public TextView editorsLabel;
	@Bind(R.id.editors)
	public EditText editors;

	@Bind(R.id.location_label)
	public TextView locationLabel;
	@Bind(R.id.location)
	public EditText location;
	@Bind(R.id.publisher)
	public EditText publisher;
	@Bind(R.id.publisher_label)
	public TextView publisherLabel;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apa_book_chapter_reference_layout);
		ButterKnife.bind(this);
		setUpReferenceActivity(ReferenceItem.BOOK_CHAPTER);

		toolbar.setTitle(getString(R.string.apa_book_chapter_reference));

		bookTitleLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		bookTitleLabel.setOnClickListener(new LabelClickListener());

		bookSubtitleLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		bookSubtitleLabel.setOnClickListener(new LabelClickListener());

		pagesOfChapterLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		pagesOfChapterLabel.setOnClickListener(new LabelClickListener());

		editorsLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		editorsLabel.setOnClickListener(new LabelClickListener());

		locationLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		locationLabel.setOnClickListener(new LabelClickListener());

		publisherLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		publisherLabel.setOnClickListener(new LabelClickListener());

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

	public void setUpView()
	{
		if(currentReference != null)
		{
			if(currentReference.bookTitle != null && currentReference.bookTitle.length() > 0)
				bookTitle.setText(currentReference.bookTitle);
			if(currentReference.bookSubtitle != null && currentReference.bookSubtitle.length() > 0)
				bookSubtitle.setText(currentReference.bookSubtitle);
			if(currentReference.editors != null && currentReference.editors.length() > 0)
				editors.setText(currentReference.editors);
			if(currentReference.pagesOfChapter != null && currentReference.pagesOfChapter.length() > 0)
				pagesOfChapter.setText(currentReference.pagesOfChapter);
		}
	}

	@Override
	public void save()
	{
		super.save();
		currentReference.bookTitle = bookTitle.getText().toString();
		currentReference.bookSubtitle = bookSubtitle.getText().toString();
		currentReference.editors = editors.getText().toString();
		currentReference.pagesOfChapter = pagesOfChapter.getText().toString();
	}
}
