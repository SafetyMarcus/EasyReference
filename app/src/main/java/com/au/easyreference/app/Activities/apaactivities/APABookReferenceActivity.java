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
public class APABookReferenceActivity extends BaseAPAReferenceActivity
{
	@Bind(R.id.location_label)
	public TextView locationLabel;
	@Observe(R.id.location)
	public State<String> location;
	@Bind(R.id.publisher_label)
	public TextView publisherLabel;
	@Observe(R.id.publisher)
	public State<String> publisher;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apa_book_reference_layout);
		ButterKnife.bind(this);

		//Setup reference activity instantiates current reference
		setUpReferenceActivity(ReferenceItem.BOOK_REFERENCE);
		location = currentReference.location;
		publisher = currentReference.publisher;
		APABookReferenceActivityViewBinding.watch(this);

		toolbar.setTitle(getString(R.string.apa_book_reference));

		locationLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		locationLabel.setOnClickListener(new LabelClickListener());

		publisherLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		publisherLabel.setOnClickListener(new LabelClickListener());
	}
}
