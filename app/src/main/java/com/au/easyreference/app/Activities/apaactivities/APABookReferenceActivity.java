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
public class APABookReferenceActivity extends BaseAPAReferenceActivity
{
	@InjectView(R.id.location_label)
	public TextView locationLabel;
	@InjectView(R.id.location)
	public EditText location;
	@InjectView(R.id.publisher_label)
	public TextView publisherLabel;
	@InjectView(R.id.publisher)
	public EditText publisher;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.apa_book_reference_layout);
		ButterKnife.inject(this);

		toolbar.setTitle(getString(R.string.apa_book_reference));
		super.onCreate(savedInstanceState);

		if(currentReference == null)
		{
			currentReference = new ReferenceItem(ReferenceItem.BOOK_REFERENCE);
			referenceList.referenceList.add(currentReference);
		}

		locationLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.easy_reference_red), PorterDuff.Mode.SRC_IN);
		locationLabel.setOnClickListener(new LabelClickListener());

		publisherLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.easy_reference_red), PorterDuff.Mode.SRC_IN);
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
	public void save()
	{
		super.save();
		currentReference.location = location.getText().toString();
		currentReference.publisher = publisher.getText().toString();
	}

	public void setUpView(String id)
	{
		super.setUpView(id);
		if(currentReference != null)
		{
			if(currentReference.location != null && currentReference.location.length() > 0)
				location.setText(currentReference.location);
			if(currentReference.publisher != null && currentReference.publisher.length() > 0)
				publisher.setText(currentReference.publisher);
		}
	}
}
