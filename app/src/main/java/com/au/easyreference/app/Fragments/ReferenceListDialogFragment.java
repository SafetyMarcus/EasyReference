package com.au.easyreference.app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
	@InjectView(R.id.new_list_title)
	protected EditText title;
	@InjectView(R.id.title_label)
	protected TextView titleLabel;
	@InjectView(R.id.cancel)
	protected Button cancel;
	@InjectView(R.id.save)
	protected Button save;

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

		adapter = new ReferenceListAdapter(getActivity(), R.layout.reference_item, referenceItems, getActivity().getLayoutInflater(), type);
		referencesListView.setAdapter(adapter);
		referencesListView.setOnItemClickListener(new ReferenceClickedListener());

		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
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
					referenceList.title = title.getText().toString();
					referenceList.referenceList = referenceItems;
					referenceList.saveToFile(referenceList, getActivity().getApplication());
				}
			}
		});

		cancel. setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
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
