package com.au.easyreference.app.activities.apaactivities;

import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.BaseAPAReferenceActivity;
import com.au.easyreference.app.databinding.ApaBookReferenceLayoutBinding;
import com.au.easyreference.app.references.ReferenceItem;

/**
 * @author Marcus Hooper
 */
public class APABookReferenceActivity extends BaseAPAReferenceActivity
{
	@Bind(R.id.location_label)
	public TextView locationLabel;
	@Bind(R.id.location)
	public EditText location;
	@Bind(R.id.publisher_label)
	public TextView publisherLabel;
	@Bind(R.id.publisher)
	public EditText publisher;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ApaBookReferenceLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.apa_book_reference_layout);
		ButterKnife.bind(this);

		//Setup reference activity instantiates current reference
		setUpReferenceActivity(ReferenceItem.BOOK_REFERENCE);
		binding.setCurrentReference(currentReference);

		toolbar.setTitle(getString(R.string.apa_book_reference));

		locationLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		locationLabel.setOnClickListener(new LabelClickListener());

		publisherLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		publisherLabel.setOnClickListener(new LabelClickListener());
	}

	@Override
	public void save()
	{
		super.save();
		currentReference.location = location.getText().toString();
		currentReference.publisher = publisher.getText().toString();
	}
}
