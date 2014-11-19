package com.au.easyreference.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.ReferenceListAdapter;
import com.au.easyreference.app.References.ReferenceList;
import com.au.easyreference.app.Utils.ERApplication;
import com.au.easyreference.app.Utils.HelperFunctions;
import com.au.easyreference.app.Utils.PDFGenerator;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

import java.io.File;
import java.util.ArrayList;


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

	private ReferenceListAdapter referenceListAdapter;
	private SimpleSwipeUndoAdapter swipeUndoAdapter;
	private ArrayAdapter<String> referenceTypeAdapter;

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

		plusButton.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

		if(savedInstanceState == null)
			((ERApplication) getApplication()).retrieveReferencesService();

		ArrayList<String> referenceTypes = new ArrayList<String>(2);
		referenceTypes.add("APA Reference List");
		referenceTypes.add("Harvard Reference List");
		referenceTypeAdapter = new ArrayAdapter<String>(this, R.layout.list_item, referenceTypes);
		/*
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

		referenceListAdapter = new ReferenceListAdapter(this, R.layout.reference_list_item, ERApplication.referenceLists);
		swipeUndoAdapter = new SimpleSwipeUndoAdapter(referenceListAdapter, this, new OnDismissCallback()
		{
			@Override
			public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] reverseSortedPositions)
			{
				for(int position : reverseSortedPositions)
				{
					HelperFunctions.getReferenceListForId(ERApplication.referenceLists.get(position).id);
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
			}
		});

		plusButton.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
		referenceLists.setEmptyView(emptyView);
	}

	private void sendDF(ReferenceList referenceList)
	{
		PDFGenerator pdfGenerator = new PDFGenerator();
		try
		{
			File pdf = new File(pdfGenerator.generate(referenceList));
			pdf.mkdirs();

			Uri uri = Uri.fromFile(pdf);

			Intent intent;

			intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_STREAM, uri);

			intent.setType("message/rfc822");
			startActivity(Intent.createChooser(intent, "Email"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		referenceListAdapter.notifyDataSetChanged();
	}
}
