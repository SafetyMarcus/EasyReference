package com.au.easyreference.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.MainAdapter;
import com.au.easyreference.app.R;
import com.au.easyreference.app.Utils.ERApplication;
import com.au.easyreference.app.Utils.HelperFunctions;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

import java.io.File;


public class MainActivity extends ActionBarActivity
{
	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;
	@InjectView(R.id.old_references_list)
	protected DynamicListView referenceLists;
	@InjectView(R.id.empty_references)
	protected TextView emptyView;
	@InjectView(R.id.plus_button)
	protected ImageView plusButton;

	private Activity activity;

	private MainAdapter referenceListAdapter;
	//	private ArrayAdapter<String> referenceTypeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		boolean is21Plus = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

		setContentView(R.layout.main_view);
		super.onCreate(savedInstanceState);
		activity = this;

		ButterKnife.inject(this);

		toolbar.setBackgroundColor(getResources().getColor(R.color.easy_reference_red));
		setSupportActionBar(toolbar);
		if(is21Plus)
			getWindow().setStatusBarColor(getResources().getColor(R.color.dark_red));

		if(savedInstanceState == null)
			((ERApplication) getApplication()).retrieveReferencesService();

		referenceLists.addFooterView(getLayoutInflater().inflate(R.layout.footer, referenceLists, false), null, false);
		referenceLists.setDivider(null);

		/*
		ArrayList<String> referenceTypes = new ArrayList<String>(2);
		referenceTypes.add("APA Reference List");
		referenceTypes.add("Harvard Reference List");
		referenceTypeAdapter = new ArrayAdapter<String>(this, R.layout.list_item, referenceTypes);
		referenceTypesList.setAdapter(referenceTypeAdapter);
		referenceTypesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
			{
				Intent referenceIntent = new Intent(activity, ReferenceListActivity.class);

				switch(position)
				{
					case 1:
						referenceIntent.putExtra(ReferenceListActivity.KEY_TYPE, ReferenceList.APA);
						break;
					case 2:
						referenceIntent.putExtra(ReferenceListActivity.KEY_TYPE, ReferenceList.HARVARD);
						break;
				}

				startActivity(referenceIntent);
			}
		});
		*/

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
				Intent referenceIntent = new Intent(activity, ReferenceListActivity.class);
				referenceIntent.putExtra(ReferenceListActivity.KEY_ID, ERApplication.referenceLists.get(i).id);
				startActivity(referenceIntent);
			}
		});

		plusButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent referenceIntent = new Intent(activity, ReferenceListActivity.class);
				startActivity(referenceIntent);
			}
		});

		plusButton.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
		referenceLists.setEmptyView(emptyView);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		referenceListAdapter.notifyDataSetChanged();
	}
}
