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
import com.au.easyreference.app.databinding.ApaWebPageReferenceLayoutBinding;
import com.au.easyreference.app.references.ReferenceItem;

/**
 * @author Marcus Hooper
 */
public class APAWebPageReferenceActivity extends BaseAPAReferenceActivity
{
	@Bind(R.id.url_label)
	protected TextView urlLabel;
	@Bind(R.id.url)
	protected EditText url;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ApaWebPageReferenceLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.apa_web_page_reference_layout);
		ButterKnife.bind(this);
		setUpReferenceActivity(ReferenceItem.BOOK_REFERENCE);
		binding.setCurrentReference(currentReference);

		toolbar.setTitle(getString(R.string.apa_web_reference));

		urlLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		urlLabel.setOnClickListener(new LabelClickListener());
	}

	@Override
	public void save()
	{
		super.save();
		currentReference.url = url.getText().toString();
	}
}
