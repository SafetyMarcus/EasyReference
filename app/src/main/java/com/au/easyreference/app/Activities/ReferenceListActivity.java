package com.au.easyreference.app.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.apaactivities.APABookChapterReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APABookReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APAJournalReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APAWebPageReferenceActivity;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;
import com.au.easyreference.app.references.ReferenceListAdapter;
import com.au.easyreference.app.ui.FloatInAnimation;
import com.au.easyreference.app.ui.FloatOutAnimation;
import com.au.easyreference.app.ui.SlideInBottomEffect;
import com.au.easyreference.app.utils.ERApplication;
import com.au.easyreference.app.utils.HelperFunctions;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class ReferenceListActivity extends BaseActivity
{
	public static final String KEY_TYPE = "type";
	public static final String KEY_ID = "id";

	@Bind(R.id.toolbar)
	protected Toolbar toolbar;
	@Bind(R.id.references_list_view)
	protected JazzyListView referencesListView;
	@Bind(R.id.list_title)
	protected EditText title;

	@Bind(R.id.plus_button)
	protected ImageView plusButton;
	@Bind(R.id.selection_background)
	protected View selectionView;

	public ReferenceListAdapter adapter;
	public int type;
	public ReferenceList referenceList;

	boolean hasAnimatedOut;
	boolean expandedOptions;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reference_list_activity);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		if(intent != null)
		{
			type = intent.getIntExtra(KEY_TYPE, 0);
			String id = intent.getStringExtra(KEY_ID);

			if(id != null)
			{
				referenceList = HelperFunctions.getReferenceListForId(id);
				if(referenceList != null)
					type = referenceList.referenceType;
			}
			else
			{
				referenceList = new ReferenceList("", type, new ArrayList<ReferenceItem>());
				referenceList.saveToFile(getApplication());
				ERApplication.referenceLists.add(referenceList);
			}

			if(referenceList != null)
				title.setText(referenceList.title);
		}

		adapter = new ReferenceListAdapter(this, referenceList, getLayoutInflater());
		referencesListView.setTransitionEffect(new SlideInBottomEffect(300));
		referencesListView.setAdapter(adapter);
		referencesListView.setEmptyView(findViewById(android.R.id.empty));
		referencesListView.addFooterView(getLayoutInflater().inflate(R.layout.footer, referencesListView, false), null, false);
		referencesListView.setFooterDividersEnabled(false);

		plusButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AnimatorSet set = new AnimatorSet();
				float x, y;
				if(!expandedOptions)
				{
					View window = getWindow().getDecorView();
					x = -(plusButton.getX() - (window.getWidth() / 2)) - (plusButton.getWidth()/2);
					y = -(plusButton.getY() - (window.getHeight() / 2)) - (plusButton.getHeight());
				}
				else
				{
					x = 0;
					y = 0;
				}

				ObjectAnimator alpha = ObjectAnimator.ofFloat(selectionView, "alpha", expandedOptions ? 0 : 1);
				ObjectAnimator rotate = ObjectAnimator.ofFloat(plusButton, "rotation", expandedOptions ? 0 : 45);
				ObjectAnimator xAnimator = ObjectAnimator.ofFloat(plusButton, "translationX", x);
				ObjectAnimator yAnimator = ObjectAnimator.ofFloat(plusButton, "translationY", y);
				set.playTogether(alpha, rotate, xAnimator, yAnimator);
				set.start();

				expandedOptions = !expandedOptions;
			}
		});
	}

	public void showReference(final ReferenceItem referenceItem)
	{
		Class intentClass = null;
		if(referenceItem.type == ReferenceItem.BOOK_REFERENCE)
			intentClass = APABookReferenceActivity.class;
		else if(referenceItem.type == ReferenceItem.JOURNAL_REFERENCE)
			intentClass = APAJournalReferenceActivity.class;
		else if(referenceItem.type == ReferenceItem.BOOK_CHAPTER)
			intentClass = APABookChapterReferenceActivity.class;
		else if(referenceItem.type == ReferenceItem.WEB_PAGE)
			intentClass = APAWebPageReferenceActivity.class;

		if(intentClass != null)
		{
			hasAnimatedOut = true;
			new FloatOutAnimation(plusButton).animate();

			final Class finalIntentClass = intentClass;
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					Intent intent = new Intent(ReferenceListActivity.this, finalIntentClass);

					intent.putExtra(APABookReferenceActivity.KEY_LIST_ID, referenceList.id);
					intent.putExtra(APABookReferenceActivity.KEY_ID, referenceItem.id);

					startActivityForVersion(intent);
				}
			}, 300);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed()
	{
		referenceList.title = title.getText().toString();
		referenceList.saveToFile(getApplication());

		super.onBackPressed();
	}

	@Override
	public void onPause()
	{
		ERApplication.BUS.unregister(this);
		referenceList.saveToFile(getApplication());
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged();
		ERApplication.BUS.register(this);

		if(hasAnimatedOut)
		{
			hasAnimatedOut = false;
			new FloatInAnimation(plusButton).animate();
		}
	}
}
