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
public class APAWebPageReferenceActivity extends BaseAPAReferenceActivity
{
	@InjectView(R.id.url_label)
	protected TextView urlLabel;
	@InjectView(R.id.url)
	protected EditText url;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apa_web_page_reference_layout);
		ButterKnife.inject(this);
		setUpReferenceActivity();

		toolbar.setTitle(getString(R.string.apa_web_reference));

		if(currentReference == null)
		{
			currentReference = new ReferenceItem(ReferenceItem.BOOK_REFERENCE);
			referenceList.referenceList.add(currentReference);
		}

		urlLabel.getCompoundDrawables()[2].setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_IN);
		urlLabel.setOnClickListener(new LabelClickListener());

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
		currentReference.url = url.getText().toString();
	}

	public void setUpView(String id)
	{
		super.setUpView(id);
		if(currentReference != null)
		{
			if(currentReference.url != null && currentReference.url.length() > 0)
				url.setText(currentReference.url);
		}
	}
}
