package com.au.easyreference.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.activities.MainActivity;
import com.au.easyreference.app.activities.ReferenceListActivity;
import com.au.easyreference.app.references.ReferenceList;
import com.au.easyreference.app.ui.ShowOptionsAdapter;
import com.au.easyreference.app.utils.ERApplication;
import com.au.easyreference.app.utils.HelperFunctions;
import com.au.easyreference.app.utils.PDFGenerator;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class MainAdapter extends ShowOptionsAdapter
{
	private LayoutInflater inflater;
	private ArrayList<ReferenceList> lists;
	private WeakReference<MainActivity> activity;
	private int selected = -1;

	public MainAdapter(MainActivity context, ArrayList<ReferenceList> referenceLists)
	{
		super(context, R.layout.reference_list_item, referenceLists);
		this.inflater = context.getLayoutInflater();
		this.lists = referenceLists;
		activity = new WeakReference<>(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View layout = convertView;
		ReferenceListHolder holder;

		if(layout == null)
		{
			layout = inflater.inflate(R.layout.reference_list_item, parent, false);
			holder = new ReferenceListHolder(layout);

			layout.setTag(holder);
		}
		else
			holder = (ReferenceListHolder) layout.getTag();

		String title = lists.get(position).title;
		holder.title.setText(TextUtils.isEmpty(title) ? holder.title.getResources().getString(R.string.no_title) : title);
		holder.subtext.setText(HelperFunctions.getReferenceListTypeString(lists.get(position).referenceType, getContext()));

		holder.edit.getDrawable().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
		holder.export.getDrawable().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
		holder.delete.getDrawable().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

		OnEditClickListener listener = new OnEditClickListener(position);
		layout.setOnClickListener(listener);
		holder.edit.setOnClickListener(listener);

		holder.export.setOnClickListener(new OnExportClickListener(position));
		holder.delete.setOnClickListener(new OnDeleteClickListener(position));

		if(selected == position && holder.optionsLayout.getVisibility() != View.VISIBLE)
			showOptions(holder.optionsLayout);
		else if(selected != position && holder.optionsLayout.getVisibility() == View.VISIBLE)
			hideOptions(holder.optionsLayout);

		return layout;
	}

	public class ReferenceListHolder
	{
		@InjectView(R.id.reference_title)
		public TextView title;
		@InjectView(R.id.reference_subtext)
		public TextView subtext;
		@InjectView(R.id.edit)
		public ImageView edit;
		@InjectView(R.id.export)
		public ImageView export;
		@InjectView(R.id.delete)
		public ImageView delete;
		@InjectView(R.id.options_layout)
		public LinearLayout optionsLayout;

		public ReferenceListHolder(View view)
		{
			ButterKnife.inject(this, view);
		}
	}

	public class OnExportClickListener implements View.OnClickListener
	{
		private int position;

		public OnExportClickListener(int position)
		{
			this.position = position;
		}

		@Override
		public void onClick(View v)
		{
			ReferenceList referenceList = lists.get(position);
			PDFGenerator pdfGenerator = new PDFGenerator();
			try
			{
				File pdf = new File(pdfGenerator.generate(referenceList));
				Uri uri = Uri.fromFile(pdf);

				Intent intent;

				intent = new Intent(Intent.ACTION_SEND);
				intent.putExtra(Intent.EXTRA_STREAM, uri);
				intent.putExtra(Intent.EXTRA_SUBJECT, referenceList.title);

				String body = activity.get().getString(R.string.email_text)
						+ "<a href=\"https://play.google.com/store/apps/details?id=com.au.easyreference.app&hl=en\">"
						+ ' ' + activity.get().getString(R.string.app_name) + "</a>";

				intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));

				intent.setType("message/rfc822");
				activity.get().startActivity(Intent.createChooser(intent, "Email"));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public class OnEditClickListener implements View.OnClickListener
	{
		private int position;

		public OnEditClickListener(int position)
		{
			this.position = position;
		}

		@Override
		public void onClick(View v)
		{
			if(selected == -1 || selected != position)
			{
				selected = position;
				notifyDataSetChanged();
			}
			else
			{
				Intent referenceIntent = new Intent(activity.get(), ReferenceListActivity.class);
				referenceIntent.putExtra(ReferenceListActivity.KEY_ID, ERApplication.referenceLists.get(position).id);
				activity.get().startActivityForVersion(referenceIntent, activity.get().plusButton);
			}
		}
	}

	public class OnDeleteClickListener implements View.OnClickListener
	{
		private int position;

		public OnDeleteClickListener(int position)
		{
			this.position = position;
		}

		@Override
		public void onClick(View v)
		{
			selected = -1;
			new File(HelperFunctions.getReferenceListPath(ERApplication.referenceLists.get(position).id, activity.get().getApplication())).delete();
			ERApplication.referenceLists.remove(position);
			notifyDataSetChanged();
		}
	}
}
