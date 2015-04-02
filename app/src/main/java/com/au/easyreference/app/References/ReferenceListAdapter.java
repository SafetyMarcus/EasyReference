package com.au.easyreference.app.references;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.ReferenceListActivity;
import com.au.easyreference.app.ui.ShowOptionsAdapter;
import com.au.easyreference.app.utils.HelperFunctions;

import java.lang.ref.WeakReference;

/**
 * @author Marcus Hooper
 */
public class ReferenceListAdapter extends ShowOptionsAdapter
{
	private LayoutInflater inflater;
	public ReferenceList referenceList;
	public int selected = -1;
	private WeakReference<ReferenceListActivity> activity;

	public ReferenceListAdapter(ReferenceListActivity context, ReferenceList referenceList, LayoutInflater inflater)
	{
		super(context, R.layout.reference_item);
		this.inflater = inflater;
		this.referenceList = referenceList;
		activity = new WeakReference<>(context);
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
		ReferenceItemHolder holder;

		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.reference_item, parent, false);
			holder = new ReferenceItemHolder(convertView);
			convertView.setTag(holder);
		}
		else
			holder = (ReferenceItemHolder) convertView.getTag();

		holder.information.setVisibility(View.VISIBLE);

		String title = HelperFunctions.getReferenceString(currentReference);
		if(title.length() > 0)
		{
			SpannableStringBuilder builder = new SpannableStringBuilder(title);
			if(currentReference.hasItalics() && !currentReference.isWebPage())
				builder.setSpan(new StyleSpan(Typeface.ITALIC), currentReference.italicsStart, currentReference.italicsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			holder.information.setText(builder);
		}
		else
			holder.information.setText(activity.get().getString(R.string.tap_to_edit_reference));

		holder.edit.getDrawable().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
		holder.delete.getDrawable().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

		convertView.setOnClickListener(new OnEditClickListener(position));
		holder.edit.setOnClickListener(new OnEditClickListener(position));
		holder.delete.setOnClickListener(new OnDeleteClickListener(position));

		if(selected == position && holder.optionsLayout.getVisibility() != View.VISIBLE)
			showOptions(holder.optionsLayout);
		else if(selected != position && holder.optionsLayout.getVisibility() == View.VISIBLE)
			hideOptions(holder.optionsLayout);

		return convertView;
	}

	public class ReferenceItemHolder
	{
		@InjectView(R.id.reference_title)
		public TextView information;
		@InjectView(R.id.edit)
		public ImageView edit;
		@InjectView(R.id.delete)
		public ImageView delete;
		@InjectView(R.id.options_layout)
		public LinearLayout optionsLayout;

		public ReferenceItemHolder(View view)
		{
			ButterKnife.inject(this, view);
		}
	}

	private class OnEditClickListener implements View.OnClickListener
	{
		private int position;

		private OnEditClickListener(int position)
		{
			this.position = position;
		}

		@Override
		public void onClick(View v)
		{
			if(selected != position)
			{
				selected = position;
				notifyDataSetChanged();
			}
			else
			{
				ReferenceItem referenceItem = getItem(position);
				activity.get().showReference(referenceItem);
			}
		}
	}

	private class OnDeleteClickListener implements View.OnClickListener
	{
		private int position;

		private OnDeleteClickListener(int position)
		{
			this.position = position;
		}

		@Override
		public void onClick(View v)
		{
			referenceList.referenceList.remove(position);
			selected = -1;
			notifyDataSetChanged();
		}
	}
}