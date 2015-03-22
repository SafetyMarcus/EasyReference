package com.au.easyreference.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.MainAdapter;
import com.au.easyreference.app.R;
import com.au.easyreference.app.utils.ERApplication;

public class MainActivity extends BaseActivity
{
	@InjectView(R.id.old_references_list)
	protected ListView referenceLists;
	@InjectView(R.id.plus_button)
	protected ImageView plusButton;

	private MainAdapter referenceListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		ButterKnife.inject(this);

		toolbar.setBackgroundColor(getResources().getColor(R.color.easy_reference_red));
		setSupportActionBar(toolbar);

		if(savedInstanceState == null)
			((ERApplication) getApplication()).retrieveReferencesService();

		referenceLists.addFooterView(getLayoutInflater().inflate(R.layout.footer, referenceLists, false), null, false);
		referenceLists.setDivider(null);
		referenceLists.setEmptyView(findViewById(android.R.id.empty));

		referenceListAdapter = new MainAdapter(this, ERApplication.referenceLists);
		referenceLists.setAdapter(referenceListAdapter);

		plusButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent referenceIntent = new Intent(MainActivity.this, ReferenceListActivity.class);
				startActivityForVersion(MainActivity.this, referenceIntent);
			}
		});

		plusButton.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		referenceListAdapter.notifyDataSetChanged();
	}
}