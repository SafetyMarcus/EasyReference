package com.au.easyreference.app.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.au.easyreference.app.R;

/**
 * @author Marcus Hooper
 */
public class APABookChapterReferenceDialogFragment extends BaseAPAReferenceDialogFragment
{
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.apa_book_chapter_reference_layout, container, false);

		return view;
	}
}
