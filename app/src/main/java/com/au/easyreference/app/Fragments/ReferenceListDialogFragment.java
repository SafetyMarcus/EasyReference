package com.au.easyreference.app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.References.ReferenceList;
import com.au.easyreference.app.References.ReferenceListAdapter;
import com.au.easyreference.app.Utils.ERApplication;
import com.au.easyreference.app.Utils.HelperFunctions;

import java.io.File;
import java.util.ArrayList;

import static android.widget.AdapterView.OnItemClickListener;

/**
 * @author Marcus Hooper
 */
public class ReferenceListDialogFragment extends DialogFragment
{
	public static final String KEY_TYPE = "type";
	public static final String KEY_ID = "id";

	@InjectView(R.id.references_list_view)
	protected ListView referencesListView;

	public ArrayList<ReferenceItem> referenceItems;
	public ReferenceListAdapter adapter;
	public int type;
	public ReferenceList referenceList;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		referenceItems = new ArrayList<ReferenceItem>();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View layout = getActivity().getLayoutInflater().inflate(R.layout.reference_list_dialog_fragment, null);
		ButterKnife.inject(this, layout);

		builder.setView(layout);

		final View titleView = getActivity().getLayoutInflater().inflate(R.layout.new_list_title, null);
		final EditText title = (EditText) titleView.findViewById(R.id.new_list_title);

		builder.setCustomTitle(titleView);

		Bundle args = getArguments();
		if(args != null)
		{
			type = args.getInt(KEY_TYPE);
			String id = args.getString(KEY_ID);

			if(id != null)
			{
				referenceList = HelperFunctions.getReferenceListForId(id);
				type = referenceList.referenceType;
				referenceItems = referenceList.referenceList;
				title.setText(referenceList.reference);
			}

			if(type == ReferenceList.APA)
				title.setHint(getString(R.string.new_apa_list));
			else if(type == ReferenceList.HARVARD)
				title.setHint(getString(R.string.new_harvard_list));
		}

		referenceItems.add(new ReferenceItem(ReferenceItem.NEW));

		adapter = new ReferenceListAdapter(getActivity(), R.layout.reference_item, referenceItems, getActivity().getLayoutInflater(), type);
		referencesListView.setAdapter(adapter);
		referencesListView.setOnItemClickListener(new ReferenceClickedListener());

		builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				referenceItems.remove(referenceItems.size() - 1);
				if(referenceList == null)
				{
					referenceList = new ReferenceList(title.getText().toString(), type, referenceItems);
					referenceList.saveToFile(referenceList, getActivity().getApplication());
					ERApplication.referenceLists.add(referenceList);
				}
				else
				{
					referenceList.reference = title.getText().toString();
					referenceList.referenceList = referenceItems;
					referenceList.saveToFile(referenceList, getActivity().getApplication());
				}
			}
		});
		builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dismiss();
			}
		});

		if(referenceList != null)
			builder.setNeutralButton(getString(R.string.delete), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int i)
				{
					ERApplication.referenceLists.remove(referenceList);

					String path = HelperFunctions.getReferenceListPath(referenceList.id, getActivity().getApplication());
					if(path != null)
						new File(HelperFunctions.getReferenceListPath(referenceList.id, getActivity().getApplication())).delete();

					dismiss();
				}
			});

		return builder.create();
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
	public void onResume()
	{
		super.onResume();

	}

	public class ReferenceClickedListener implements OnItemClickListener
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

			dialog.show(getActivity().getFragmentManager(), null);
		}
	}
}
