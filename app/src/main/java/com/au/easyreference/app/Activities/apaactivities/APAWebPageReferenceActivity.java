package com.au.easyreference.app.activities.apaactivities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.activities.BaseAPAReferenceActivity;
import com.au.easyreference.app.R;
import com.au.easyreference.app.references.ReferenceItem;

/**
 * @author Marcus Hooper
 */
public class APAWebPageReferenceActivity extends BaseAPAReferenceActivity
{
	@InjectView(R.id.url)
	protected EditText url;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.apa_web_page_reference_layout);
		ButterKnife.inject(this);

		toolbar.setTitle(getString(R.string.apa_book_reference));
		super.onCreate(savedInstanceState);

		if(currentReference == null)
		{
			currentReference = new ReferenceItem(ReferenceItem.BOOK_REFERENCE);
			referenceList.referenceList.add(currentReference);
		}

		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				save();
				onBackPressed();
			}
		});

		Bundle args = getIntent().getExtras();

		if(args != null && args.containsKey(KEY_ID))
			setUpView(args.getString(KEY_ID));
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
