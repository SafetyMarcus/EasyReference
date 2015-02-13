package com.au.easyreference.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.MainAdapter;
import com.au.easyreference.app.R;
import com.au.easyreference.app.utils.ERApplication;
import com.au.easyreference.app.utils.HelperFunctions;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

import java.io.File;

public class MainActivity extends BaseActivity
{
	@InjectView(R.id.old_references_list)
	protected DynamicListView referenceLists;
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

		referenceListAdapter = new MainAdapter(this, R.layout.reference_list_item, ERApplication.referenceLists);
		SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(referenceListAdapter, this, new OnDismissCallback()
		{
			@Override
			public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] reverseSortedPositions)
			{
				for(int position : reverseSortedPositions)
				{
					new File(HelperFunctions.getReferenceListPath(ERApplication.referenceLists.get(position).id, getApplication())).delete();
					ERApplication.referenceLists.remove(position);
					referenceListAdapter.notifyDataSetChanged();
				}
			}
		});

		swipeUndoAdapter.setAbsListView(referenceLists);
		referenceLists.setAdapter(swipeUndoAdapter);
		referenceLists.enableSimpleSwipeUndo();
		referenceLists.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				Intent referenceIntent = new Intent(MainActivity.this, ReferenceListActivity.class);
				referenceIntent.putExtra(ReferenceListActivity.KEY_ID, ERApplication.referenceLists.get(i).id);
				startActivityForVersion(MainActivity.this, referenceIntent);
			}
		});

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