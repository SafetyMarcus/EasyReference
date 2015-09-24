package com.au.easyreference.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.au.easyreference.app.MainAdapter;
import com.au.easyreference.app.R;
import com.au.easyreference.app.ui.FloatInAnimation;
import com.au.easyreference.app.ui.SlideInBottomEffect;
import com.au.easyreference.app.utils.ERApplication;
import com.twotoasters.jazzylistview.JazzyListView;

public class MainActivity extends BaseActivity
{
	@Bind(R.id.references_list)
	public JazzyListView referenceLists;
	@Bind(R.id.plus_button)
	public ImageView plusButton;

	private MainAdapter referenceListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		if(savedInstanceState == null)
			((ERApplication) getApplication()).retrieveReferencesService();

		referenceListAdapter = new MainAdapter(this, ERApplication.referenceLists);
		referenceLists.setTransitionEffect(new SlideInBottomEffect(300));
		referenceLists.setAdapter(referenceListAdapter);
		referenceLists.setEmptyView(findViewById(android.R.id.empty));
		referenceLists.addFooterView(getLayoutInflater().inflate(R.layout.footer, referenceLists, false), null, false);
		referenceLists.setFooterDividersEnabled(false);

		plusButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent referenceIntent = new Intent(MainActivity.this, ReferenceListActivity.class);
				startActivityForVersion(referenceIntent, plusButton);
			}
		});

		plusButton.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
		new FloatInAnimation(plusButton).animate();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		referenceListAdapter.notifyDataSetChanged();
	}
}