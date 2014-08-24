package com.au.easyreference.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.Animations.BottomSheetAnimation;
import com.au.easyreference.app.R;
import com.au.easyreference.app.ReferenceListAdapter;
import com.au.easyreference.app.References.ReferenceList;
import com.au.easyreference.app.Utils.ERApplication;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity
{
	@InjectView(R.id.old_references_list)
	protected ListView referenceLists;
	@InjectView(R.id.references_types_list)
	protected ListView referenceTypesList;
	@InjectView(R.id.empty_references)
	protected TextView emptyView;
	@InjectView(R.id.plus_button)
	protected ImageView plusButton;
	@InjectView(R.id.references_type_sheet)
	protected LinearLayout sheet;
	@InjectView(R.id.shadow)
	protected ImageView shadow;

	private Activity activity;

	private ReferenceListAdapter referenceListAdapter;
	private ArrayAdapter<String> referenceTypeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.main_view);
		super.onCreate(savedInstanceState);
		activity = this;

		ButterKnife.inject(this);

		ArrayList<String> referenceTypes = new ArrayList<String>(2);
		referenceTypes.add("APA Reference List");
		referenceTypes.add("Harvard Reference List");
		referenceTypeAdapter = new ArrayAdapter<String>(this, R.layout.reference_item, referenceTypes);
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

		referenceListAdapter = new ReferenceListAdapter(this, R.layout.reference_item, ERApplication.referenceLists);
		referenceLists.setAdapter(referenceListAdapter);
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
				if(plusButton.getRotation() == 0)
					animateSheetIn(true);
				else
					animateSheetOut();
			}
		});

		sheet.post(new Runnable()
		{
			@Override
			public void run()
			{
				sheet.setTranslationY(sheet.getHeight());
				sheet.setVisibility(View.VISIBLE);
			}
		});

		shadow.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent)
			{
				if(plusButton.getRotation() != 0)
				{
					animateSheetOut();
					return true;
				}

				return false;
			}
		});

		if(ERApplication.referenceLists.size() == 0)
		{
			referenceLists.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}
	}

	private void animateSheetIn(boolean animateShadow)
	{
		float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 52, getResources().getDisplayMetrics());

		BottomSheetAnimation.animateSheetUp(sheet);
		BottomSheetAnimation.animateButtonUp(plusButton, sheet.getHeight() - dp - (dp / 2));

		if(animateShadow)
			BottomSheetAnimation.animateShadowIn(shadow);
	}

	private void animateSheetOut()
	{
		referenceLists.smoothScrollToPosition(0);
		BottomSheetAnimation.animateSheetDown(sheet);
		BottomSheetAnimation.animateButtonDown(plusButton);
		BottomSheetAnimation.animateShadowOut(shadow);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		referenceListAdapter.notifyDataSetChanged();
	}
}
