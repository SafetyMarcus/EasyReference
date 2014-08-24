package com.au.easyreference.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.Fragments.APAReferenceDialogFragment;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.References.ReferenceList;
import com.au.easyreference.app.References.ReferenceListAdapter;
import com.au.easyreference.app.Utils.ERApplication;
import com.au.easyreference.app.Utils.HelperFunctions;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class ReferenceListActivity extends Activity
{
	public static final String KEY_TYPE = "type";
	public static final String KEY_ID = "id";

	@InjectView(R.id.references_list_view)
	protected ListView referencesListView;
	@InjectView(R.id.new_list_title)
	protected EditText title;
	@InjectView(R.id.title_label)
	protected TextView titleLabel;

	public ArrayList<ReferenceItem> referenceItems;
	public ReferenceListAdapter adapter;
	public int type;
	public ReferenceList referenceList;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		referenceItems = new ArrayList<ReferenceItem>();

		setContentView(R.layout.reference_list_dialog_fragment);
		ButterKnife.inject(this);

		Intent intent = getIntent();
		if(intent != null)
		{
			type = intent.getIntExtra(KEY_TYPE, 0);
			String id = intent.getStringExtra(KEY_ID);

			if(id != null)
			{
				referenceList = HelperFunctions.getReferenceListForId(id);
				type = referenceList.referenceType;
				referenceItems = referenceList.referenceList;
				title.setText(referenceList.title);
			}

			if(referenceList == null)
			{
				if(type == ReferenceList.APA)
					titleLabel.setText(getString(R.string.new_apa_list));
				else if(type == ReferenceList.HARVARD)
					titleLabel.setText(getString(R.string.new_harvard_list));
			}
			else
				titleLabel.setText(referenceList.title);
		}

		referenceItems.add(new ReferenceItem(ReferenceItem.NEW));

		adapter = new ReferenceListAdapter(this, R.layout.reference_item, referenceItems, getLayoutInflater(), type);
		referencesListView.setAdapter(adapter);
		referencesListView.setOnItemClickListener(new ReferenceClickedListener());
	}

	public APAReferenceDialogFragment.APAReferenceListener apaListener = new APAReferenceDialogFragment.APAReferenceListener()
	{
		@Override
		public void onReferenceCreated(ReferenceItem newReference)
		{
			boolean updating = false;
			for(ReferenceItem reference : ERApplication.allReferences)
			{
				if(reference.id.equalsIgnoreCase(newReference.id))
					updating = true;
			}

			if(!updating)
			{
				referenceItems.remove(referenceItems.size() - 1);
				referenceItems.add(newReference);
				referenceItems.add(new ReferenceItem(ReferenceItem.NEW));
				ERApplication.allReferences.add(newReference);
			}
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onBackPressed()
	{
		if(referenceItems.size() > 1)
			referenceItems.remove(referenceItems.size() - 1);
		else
			referenceItems.clear();
		if(referenceList == null)
		{
			referenceList = new ReferenceList(title.getText().toString(), type, referenceItems);
			referenceList.saveToFile(referenceList, getApplication());
			ERApplication.referenceLists.add(referenceList);
		}
		else
		{
			referenceList.title = title.getText().toString();
			referenceList.referenceList = referenceItems;
			referenceList.saveToFile(referenceList, getApplication());
		}
		super.onBackPressed();
	}

	@Override
	public void onResume()
	{
		super.onResume();

	}

	public class ReferenceClickedListener implements AdapterView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			APAReferenceDialogFragment dialog = new APAReferenceDialogFragment();

			Bundle args = new Bundle();
			if(adapter.getItem(position) != null && adapter.getItem(position).type != ReferenceItem.NEW)
			{
				args.putString(APAReferenceDialogFragment.KEY_ID, adapter.getItem(position).id);
				dialog.setArguments(args);
			}

			dialog.show(getFragmentManager(), null);
		}
	}
}
