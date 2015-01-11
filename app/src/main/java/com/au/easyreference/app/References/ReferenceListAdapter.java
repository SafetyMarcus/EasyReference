package com.au.easyreference.app.References;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.Utils.HelperFunctions;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

/**
 * @author Marcus Hooper
 */
public class ReferenceListAdapter extends ArrayAdapter implements UndoAdapter
{
	private LayoutInflater inflater;
	private int layoutResourceId;
	public ReferenceList referenceList;

	public ReferenceListAdapter(Context context, int resource, ReferenceList referenceList, LayoutInflater inflater)
	{
		super(context, resource);
		this.inflater = inflater;
		this.referenceList = referenceList;
		layoutResourceId = resource;
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
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ReferenceItem currentReference = getItem(position);

		if(convertView == null)
			convertView = inflater.inflate(layoutResourceId, parent, false);

		TextView information = (TextView) convertView.findViewById(R.id.reference_information);
		information.setVisibility(View.VISIBLE);

		String title = "";
		if(currentReference.type == ReferenceItem.BOOK_REFERENCE)
			title = HelperFunctions.getAPABookReferenceString(currentReference);
		else if(currentReference.type == ReferenceItem.JOURNAL_REFERENCE)
			title = HelperFunctions.getAPAJournalReferenceString(currentReference);
		else if(currentReference.type == ReferenceItem.BOOK_CHAPTER)
			title = HelperFunctions.getAPABookChapterReferenceString(currentReference, getContext());
		else if(currentReference.type == ReferenceItem.WEB_PAGE)
			title = HelperFunctions.getAPAWebPageReferenceString(currentReference);

		information.setText(title.length() > 0 ? title : information.getResources().getString(R.string.no_title));

		return convertView;
	}

	@NonNull
	@Override
	public View getUndoView(int i, @Nullable View convertView, @NonNull ViewGroup parent)
	{
		if(convertView == null)
			convertView = inflater.inflate(R.layout.undo_view, parent, false);

		convertView.getLayoutParams().height = 96;

		return convertView;
	}


	@NonNull
	@Override
	public View getUndoClickView(@NonNull View convertView)
	{
		return convertView.findViewById(R.id.undo_button);
	}
}
