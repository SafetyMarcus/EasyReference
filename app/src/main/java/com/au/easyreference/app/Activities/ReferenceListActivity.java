package com.au.easyreference.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.Events.ResultSelectedEvent;
import com.au.easyreference.app.Fragments.APABookReferenceDialogFragment;
import com.au.easyreference.app.Fragments.APAJournalReferenceDialogFragment;
import com.au.easyreference.app.Fragments.SearchDialog;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.References.ReferenceList;
import com.au.easyreference.app.References.ReferenceListAdapter;
import com.au.easyreference.app.Utils.ERApplication;
import com.au.easyreference.app.Utils.HelperFunctions;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class ReferenceListActivity extends ActionBarActivity
{
	public static final int REQUEST_REFERENCE_ITEM = 1111;

	public static final String KEY_TYPE = "type";
	public static final String KEY_ID = "id";

	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;
	@InjectView(R.id.references_list_view)
	protected ListView referencesListView;
	@InjectView(R.id.new_list_title)
	protected EditText title;

	public ReferenceListAdapter adapter;
	public int type;
	public ReferenceList referenceList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		boolean is21Plus = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.reference_list_dialog_fragment);
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

		if(savedInstanceState == null)
			referenceList.referenceList.add(new ReferenceItem(ReferenceItem.NEW));

		adapter = new ReferenceListAdapter(this, R.layout.reference_item, referenceList, getLayoutInflater());
		referencesListView.setAdapter(adapter);
		referencesListView.setOnItemClickListener(new ReferenceClickedListener());
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
				getFragmentManager().beginTransaction().add(new SearchDialog(), null).commit();
		}
		return super.onOptionsItemSelected(item);
	}

	public void addReferenceItem(ReferenceItem newReference)
	{
		referenceList.referenceList.remove(referenceList.referenceList.size() - 1);
		referenceList.referenceList.add(newReference);
		referenceList.referenceList.add(new ReferenceItem(ReferenceItem.NEW));
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onBackPressed()
	{
		if(referenceList.referenceList.size() > 1)
			referenceList.referenceList.remove(referenceList.referenceList.size() - 1);
		else
			referenceList.referenceList.clear();

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

	@Subscribe
	public void onResultSelected(ResultSelectedEvent event)
	{
		ReferenceItem newReference = new ReferenceItem(event.result);
		addReferenceItem(newReference);
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

	public class ReferenceClickedListener implements AdapterView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			ReferenceItem referenceItem = adapter.getItem(position);

			if(referenceItem != null && referenceItem.type != ReferenceItem.NEW)
			{
				Fragment dialog = null;
				if(referenceItem.type == ReferenceItem.BOOK_REFERENCE)
					dialog = new APABookReferenceDialogFragment();
				else if(referenceItem.type == ReferenceItem.JOURNAL_REFERENCE)
					dialog = new APAJournalReferenceDialogFragment();

				if(dialog != null)
				{
					Bundle args = new Bundle();
					args.putString(APABookReferenceDialogFragment.KEY_LIST_ID, referenceList.id);
					args.putString(APABookReferenceDialogFragment.KEY_ID, referenceItem.id);
					dialog.setArguments(args);

					DialogActivity.showDialog(getActivity(), dialog, REQUEST_REFERENCE_ITEM);
				}
			}
			else
			{
				adapter.showOptions = true;
				adapter.notifyDataSetChanged();
			}
		}
	}
}
