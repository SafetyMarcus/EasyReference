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

/**
 * @author Marcus Hooper
 */
public class APABookChapterReferenceDialogFragment extends BaseAPAReferenceDialogFragment
{
	@InjectView(R.id.book_title)
	public EditText book_title;
	@InjectView(R.id.book_subtitle)
	public EditText book_subtitle;
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
		View view = inflater.inflate(R.layout.apa_book_chapter_reference_layout, container, false);
		ButterKnife.inject(this, view);

		super.onCreateView(inflater, container, savedInstanceState);
		((DialogActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.apa_book_chapter_reference));
		((DialogActivity) getActivity()).toolbar.setTitle(getString(R.string.apa_book_chapter_reference));

		setHasOptionsMenu(true);

		return view;
	}
}
