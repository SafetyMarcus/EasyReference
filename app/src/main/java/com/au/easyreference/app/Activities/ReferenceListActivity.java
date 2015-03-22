package com.au.easyreference.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.fragments.ContainerDialogFragment;
import com.au.easyreference.app.fragments.TypeDialog;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;
import com.au.easyreference.app.references.ReferenceListAdapter;
import com.au.easyreference.app.utils.ERApplication;
import com.au.easyreference.app.utils.HelperFunctions;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class ReferenceListActivity extends BaseActivity
{
	public static final String KEY_TYPE = "type";
	public static final String KEY_ID = "id";

	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;
	@InjectView(R.id.references_list_view)
	protected ListView referencesListView;
	@InjectView(R.id.list_title)
	protected EditText title;
	@InjectView(R.id.plus_button)
	protected ImageView plusButton;

	public ReferenceListAdapter adapter;
	public int type;
	public ReferenceList referenceList;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.reference_list_activity);
		super.onCreate(savedInstanceState);
		ButterKnife.inject(this);

		setSupportActionBar(toolbar);
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

		adapter = new ReferenceListAdapter(this, referenceList, getLayoutInflater());
		referencesListView.setAdapter(adapter);
		referencesListView.setEmptyView(findViewById(android.R.id.empty));

		plusButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Bundle args = new Bundle();
				args.putString(ContainerDialogFragment.TYPE_KEY, TypeDialog.class.toString());

				ContainerDialogFragment container = new ContainerDialogFragment();
				container.setArguments(args);
				container.setCloseListener(closeListener);
				container.show(getFragmentManager(), "Container");
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

				ContainerDialogFragment container = new ContainerDialogFragment();
				container.setArguments(args);
				container.setCloseListener(closeListener);
				container.show(getFragmentManager(), "Container");
		}
		return super.onOptionsItemSelected(item);
	}

	private ContainerDialogFragment.CloseListener closeListener = new ContainerDialogFragment.CloseListener()
	{
		@Override
		public void onClose(Object result)
		{
			if(result instanceof Integer)
				addReferenceItem(new ReferenceItem((Integer) result));
			else if(result instanceof JSONObject)
				addReferenceItem(new ReferenceItem((JSONObject) result));
		}
	};

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

	@Override
	public void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged();
		ERApplication.BUS.register(this);
	}
}
