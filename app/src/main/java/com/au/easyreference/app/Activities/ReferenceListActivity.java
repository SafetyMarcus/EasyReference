package com.au.easyreference.app.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.apaactivities.APABookChapterReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APABookReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APAJournalReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APAWebPageReferenceActivity;
import com.au.easyreference.app.events.SearchResultEvent;
import com.au.easyreference.app.events.TypeResultEvent;
import com.au.easyreference.app.fragments.ContainerDialogFragment;
import com.au.easyreference.app.fragments.SearchDialog;
import com.au.easyreference.app.fragments.TypeDialog;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;
import com.au.easyreference.app.references.ReferenceListAdapter;
import com.au.easyreference.app.utils.ERApplication;
import com.au.easyreference.app.utils.HelperFunctions;
import com.au.easyreference.app.utils.Result;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SwipeUndoAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class ReferenceListActivity extends ActionBarActivity
{
	public static final int REQUEST_SEARCH = 1111;
	public static final int REQUEST_TYPE = 1112;

	public static final String KEY_TYPE = "type";
	public static final String KEY_ID = "id";

	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;
	@InjectView(R.id.references_list_view)
	protected DynamicListView referencesListView;
	@InjectView(R.id.new_list_title)
	protected EditText title;
	@InjectView(R.id.plus_button)
	protected ImageView plusButton;

	public ReferenceListAdapter adapter;
	public int type;
	public ReferenceList referenceList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		boolean is21Plus = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.reference_list_activity);
		ButterKnife.inject(this);

		toolbar.setBackgroundColor(getResources().getColor(R.color.easy_reference_red));
		setSupportActionBar(toolbar);
		if(is21Plus)
			getWindow().setStatusBarColor(getResources().getColor(R.color.dark_red));
		toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back_white));
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});

		Intent intent = getIntent();
		if(intent != null)
		{
			type = intent.getIntExtra(KEY_TYPE, 0);
			String id = intent.getStringExtra(KEY_ID);

			if(id != null)
			{
				referenceList = HelperFunctions.getReferenceListForId(id);
				type = referenceList.referenceType;
			}
			else
			{
				referenceList = new ReferenceList("", type, new ArrayList<ReferenceItem>());
				referenceList.saveToFile(getApplication());
				ERApplication.referenceLists.add(referenceList);
			}

			title.setText(referenceList.title);
		}

		adapter = new ReferenceListAdapter(this, R.layout.reference_item, referenceList, getLayoutInflater());
		SwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(adapter, this, new OnDismissCallback()
		{
			@Override
			public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] ints)
			{
				for(int position : ints)
					referenceList.referenceList.remove(position);
			}
		});
		swipeUndoAdapter.setAbsListView(referencesListView);
		referencesListView.setAdapter(swipeUndoAdapter);
		referencesListView.enableSimpleSwipeUndo();
		referencesListView.setOnItemClickListener(new ReferenceClickedListener());
		referencesListView.setEmptyView(findViewById(android.R.id.empty));

		plusButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Bundle args = new Bundle();
				args.putString(ContainerDialogFragment.TYPE_KEY, TypeDialog.class.toString());

				if(getResources().getBoolean(R.bool.is_tablet))
				{
					ContainerDialogFragment container = new ContainerDialogFragment();
					container.setArguments(args);
					container.show(getFragmentManager(), "Container");
				}
				else
				{
					Intent intent = new Intent(ReferenceListActivity.this, ContainerActivity.class);
					intent.putExtras(args);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, 0, 0, getString(R.string.search)).setIcon(R.drawable.actionbar_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
			case 0:
				Bundle args = new Bundle();
				args.putString(ContainerDialogFragment.TYPE_KEY, TypeDialog.class.toString());
				args.putBoolean(TypeDialog.SEARCH, true);

				if(getResources().getBoolean(R.bool.is_tablet))
				{
					ContainerDialogFragment container = new ContainerDialogFragment();
					container.setArguments(args);
					container.show(getFragmentManager(), "Container");
				}
				else
				{
					Intent intent = new Intent(ReferenceListActivity.this, ContainerActivity.class);
					intent.putExtras(args);
					startActivity(intent);
				}
		}
		return super.onOptionsItemSelected(item);
	}

	public void addReferenceItem(ReferenceItem newReference)
	{
		referenceList.referenceList.add(newReference);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onBackPressed()
	{
		referenceList.title = title.getText().toString();
		referenceList.saveToFile(getApplication());
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}

	@Override
	public void onPause()
	{
		ERApplication.BUS.unregister(this);
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged();
		ERApplication.BUS.register(this);
	}

	@Subscribe
	public void searchResult(SearchResultEvent event)
	{
		addReferenceItem(new ReferenceItem(event.result));
	}

	@Subscribe
	public void typeResult(TypeResultEvent event)
	{
		addReferenceItem(new ReferenceItem(event.type));
	}

	public class ReferenceClickedListener implements AdapterView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			ReferenceItem referenceItem = adapter.getItem(position);

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
				Intent intent = new Intent(ReferenceListActivity.this, intentClass);

				intent.putExtra(APABookReferenceActivity.KEY_LIST_ID, referenceList.id);
				intent.putExtra(APABookReferenceActivity.KEY_ID, referenceItem.id);

				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		}
	}
}
