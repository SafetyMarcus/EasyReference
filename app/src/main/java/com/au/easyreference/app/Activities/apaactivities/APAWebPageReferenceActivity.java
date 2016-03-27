package com.au.easyreference.app.activities.apaactivities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.BaseAPAReferenceActivity;
import com.au.easyreference.app.references.ReferenceItem;
import com.easygoingapps.ThePolice;
import com.easygoingapps.annotations.Observe;
import com.easygoingapps.utils.State;

/**
 * @author Marcus Hooper
 */
public class APAWebPageReferenceActivity extends BaseAPAReferenceActivity
{
	@Observe(R.id.url)
	public State<String> url;
	@Bind(R.id.url_label)
	protected TextView urlLabel;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apa_web_page_reference_layout);
		ButterKnife.bind(this);
		setUpReferenceActivity(ReferenceItem.BOOK_REFERENCE);
		url = currentReference.url;
		ThePolice.watch(this);

		toolbar.setTitle(getString(R.string.apa_web_reference));
		urlLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		urlLabel.setOnClickListener(new LabelClickListener());
	}
}
