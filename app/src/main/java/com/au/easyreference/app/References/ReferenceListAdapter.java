package com.au.easyreference.app.References;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.Activities.DialogActivity;
import com.au.easyreference.app.Fragments.APABookReferenceDialogFragment;
import com.au.easyreference.app.Fragments.APAJournalReferenceDialogFragment;
import com.au.easyreference.app.Fragments.BaseAPAReferenceDialogFragment;
import com.au.easyreference.app.R;
import com.au.easyreference.app.Utils.HelperFunctions;

import java.lang.ref.WeakReference;

/**
 * @author Marcus Hooper
 */
public class ReferenceListAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	private int layoutResourceId;
	public boolean showOptions = false;
	public ReferenceList referenceList;
	private WeakReference<Activity> activity;

	public ReferenceListAdapter(Activity context, int resource, ReferenceList referenceList, LayoutInflater inflater)
	{
		super();
		this.inflater = inflater;
		this.referenceList = referenceList;
		layoutResourceId = resource;
		activity = new WeakReference<Activity>(context);
	}

	@Override
	public int getCount()
	{
		return referenceList.referenceList.size();
	}

	@Override
	public ReferenceItem getItem(int i)
	{
		return referenceList.referenceList.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ReferenceItem currentReference = getItem(position);
		View layout = convertView;
		ReferenceHolder holder;

		if(layout == null)
		{
			layout = inflater.inflate(layoutResourceId, parent, false);
			holder = new ReferenceHolder(layout, activity.get());

			layout.setTag(holder);
		}
		else
			holder = (ReferenceHolder) layout.getTag();

		holder.bookButton.setVisibility(View.GONE);
		holder.journalButton.setVisibility(View.GONE);
		holder.information.setVisibility(View.VISIBLE);

		if(currentReference.type != ReferenceItem.NEW || !showOptions)
		{
			if(currentReference.type != ReferenceItem.NEW)
			{
				if(currentReference.type == ReferenceItem.BOOK_REFERENCE)
					holder.information.setText(HelperFunctions.getAPABookReferenceString(currentReference));
				else if(currentReference.type == ReferenceItem.JOURNAL_REFERENCE)
					holder.information.setText(HelperFunctions.getAPAJournalReferenceString(currentReference));
			}
			else
				holder.information.setText(holder.information.getResources().getString(R.string.add_new));
		}
		else
		{
			holder.information.setVisibility(View.GONE);
			holder.bookButton.setVisibility(View.VISIBLE);
			holder.journalButton.setVisibility(View.VISIBLE);
		}

		return layout;
	}

	public class ReferenceHolder
	{
		@InjectView(R.id.reference_information)
		public TextView information;
		@InjectView(R.id.book_button)
		public Button bookButton;
		@InjectView(R.id.journal_button)
		public Button journalButton;

		public ReferenceHolder(View view, final Activity activity)
		{
			ButterKnife.inject(this, view);

			bookButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Fragment fragment = new APABookReferenceDialogFragment();
					Bundle args = new Bundle();
					args.putString(BaseAPAReferenceDialogFragment.KEY_LIST_ID, referenceList.id);
					fragment.setArguments(args);

					DialogActivity.showDialog(activity, fragment);
					showOptions = false;
					notifyDataSetChanged();
				}
			});

			journalButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					Fragment fragment = new APAJournalReferenceDialogFragment();
					Bundle args = new Bundle();
					args.putString(BaseAPAReferenceDialogFragment.KEY_LIST_ID, referenceList.id);
					fragment.setArguments(args);

					DialogActivity.showDialog(activity, fragment);
					showOptions = false;
					notifyDataSetChanged();
				}
			});
		}
	}
}
