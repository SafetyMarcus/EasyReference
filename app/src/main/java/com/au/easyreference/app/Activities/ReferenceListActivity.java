package com.au.easyreference.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.au.easyreference.app.Fragments.APABookChapterReferenceDialogFragment;
import com.au.easyreference.app.Fragments.APABookReferenceDialogFragment;
import com.au.easyreference.app.Fragments.APAJournalReferenceDialogFragment;
import com.au.easyreference.app.Fragments.SearchDialog;
import com.au.easyreference.app.Fragments.TypeDialog;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.References.ReferenceList;
import com.au.easyreference.app.References.ReferenceListAdapter;
import com.au.easyreference.app.Utils.ERApplication;
import com.au.easyreference.app.Utils.HelperFunctions;
import com.au.easyreference.app.Utils.Result;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SwipeUndoAdapter;

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
	@InjectView(R.id.empty_view)
	protected View emptyView;

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
		referencesListView.setEmptyView(emptyView);

		plusButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				DialogActivity.showDialog(getActivity(), new TypeDialog(), REQUEST_TYPE);
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
				TypeDialog dialog = new TypeDialog();
				Bundle args = new Bundle();
				args.putBoolean(TypeDialog.SEARCH, true);
				dialog.setArguments(args);

				DialogActivity.showDialog(getActivity(), dialog, REQUEST_SEARCH);
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
	}

	@Override
	public void onPause()
	{
		ERApplication.BUS.unregister(this);
		super.onPause();
	}

	private Activity getActivity()
	{
		return this;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged();
		ERApplication.BUS.register(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode == RESULT_OK)
		{
			switch(requestCode)
			{
				case REQUEST_SEARCH:
					ReferenceItem newReference = new ReferenceItem((Result) data.getParcelableExtra(SearchDialog.RESULT));
					addReferenceItem(newReference);
					break;

				case REQUEST_TYPE:
					ReferenceItem reference = new ReferenceItem(data.getIntExtra(TypeDialog.TYPE, 0));
					addReferenceItem(reference);
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public class ReferenceClickedListener implements AdapterView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			ReferenceItem referenceItem = adapter.getItem(position);

			Fragment dialog = null;
			if(referenceItem.type == ReferenceItem.BOOK_REFERENCE)
				dialog = new APABookReferenceDialogFragment();
			else if(referenceItem.type == ReferenceItem.JOURNAL_REFERENCE)
				dialog = new APAJournalReferenceDialogFragment();
			else if(referenceItem.type == ReferenceItem.BOOK_CHAPTER)
				dialog = new APABookChapterReferenceDialogFragment();

			if(dialog != null)
			{
				Bundle args = new Bundle();
				args.putString(APABookReferenceDialogFragment.KEY_LIST_ID, referenceList.id);
				args.putString(APABookReferenceDialogFragment.KEY_ID, referenceItem.id);
				dialog.setArguments(args);

				DialogActivity.showDialog(getActivity(), dialog);
			}
		}
	}
}
