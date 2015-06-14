package com.au.easyreference.app.activities.apaactivities;

import android.graphics.PorterDuff;
import android.os.Bundle;
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
public class APABookChapterReferenceActivity extends BaseAPAReferenceActivity
{
	@InjectView(R.id.book_title_label)
	public TextView bookTitleLabel;
	@InjectView(R.id.book_title)
	public EditText bookTitle;
	@InjectView(R.id.book_subtitle_label)
	public TextView bookSubtitleLabel;
	@InjectView(R.id.book_subtitle)
	public EditText bookSubtitle;
	@InjectView(R.id.pages_of_chapter_label)
	public TextView pagesOfChapterLabel;
	@InjectView(R.id.pages_of_chapter)
	public EditText pagesOfChapter;
	@InjectView(R.id.editors_label)
	public TextView editorsLabel;
	@InjectView(R.id.editors)
	public EditText editors;

	@InjectView(R.id.location_label)
	public TextView locationLabel;
	@InjectView(R.id.location)
	public EditText location;
	@InjectView(R.id.publisher)
	public EditText publisher;
	@InjectView(R.id.publisher_label)
	public TextView publisherLabel;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apa_book_chapter_reference_layout);
		ButterKnife.inject(this);
		setUpReferenceActivity();

		toolbar.setTitle(getString(R.string.apa_book_chapter_reference));

		if(currentReference == null)
		{
			currentReference = new ReferenceItem(ReferenceItem.BOOK_CHAPTER);
			referenceList.referenceList.add(currentReference);
		}

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
			setUpView(args.getString(KEY_ID));
	}

	@Override
	public void onBackPressed()
	{
		save();
		super.onBackPressed();
	}

	@Override
	public void setUpView(String id)
	{
		super.setUpView(id);

		if(currentReference != null)
		{
			if(currentReference.location != null && currentReference.location.length() > 0)
				location.setText(currentReference.location);
			if(currentReference.publisher != null && currentReference.publisher.length() > 0)
				publisher.setText(currentReference.publisher);
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
		currentReference.location = location.getText().toString();
		currentReference.publisher = publisher.getText().toString();
	}
}
