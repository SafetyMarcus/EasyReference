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
public class APABookChapterReferenceDialogFragment extends BaseAPAReferenceDialogFragment
{
	@InjectView(R.id.book_title)
	public EditText bookTitle;
	@InjectView(R.id.book_subtitle)
	public EditText bookSubtitle;
	@InjectView(R.id.editors)
	public EditText editors;
	@InjectView(R.id.location)
	public EditText location;
	@InjectView(R.id.publisher)
	public EditText publisher;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View layout = inflater.inflate(R.layout.apa_book_chapter_reference_layout, container, false);
		ButterKnife.inject(this, layout);

		super.onCreateView(inflater, container, savedInstanceState);
		((DialogActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.apa_book_chapter_reference));
		((DialogActivity) getActivity()).toolbar.setTitle(getString(R.string.apa_book_chapter_reference));

		setHasOptionsMenu(false);

		if(currentReference == null)
		{
			currentReference = new ReferenceItem(ReferenceItem.BOOK_CHAPTER);
			referenceList.referenceList.add(currentReference);
		}

		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				save();
				getActivity().onBackPressed();
			}
		});

		Bundle args = getArguments();

		if(args != null && args.containsKey(KEY_ID))
			setUpView(args.getString(KEY_ID));

		return layout;
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
		}
	}

	@Override
	public void save()
	{
		super.save();
		currentReference.bookTitle = bookTitle.getText().toString();
		currentReference.bookSubtitle = bookSubtitle.getText().toString();
		currentReference.editors = editors.getText().toString();
		currentReference.location = location.getText().toString();
		currentReference.publisher = publisher.getText().toString();
	}
}
