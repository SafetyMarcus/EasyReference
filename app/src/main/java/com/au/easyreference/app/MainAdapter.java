package com.au.easyreference.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.references.ReferenceList;
import com.au.easyreference.app.utils.HelperFunctions;
import com.au.easyreference.app.utils.PDFGenerator;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class MainAdapter extends ArrayAdapter<ReferenceList> implements UndoAdapter
{
	private LayoutInflater inflater;
	private ArrayList<ReferenceList> list;
	private int layoutResourceId;
	private WeakReference<Activity> activity;

	public MainAdapter(Activity context, int resource, ArrayList<ReferenceList> referenceLists)
	{
		super(context, resource, referenceLists);
		this.inflater = context.getLayoutInflater();
		this.list = referenceLists;
		layoutResourceId = resource;
		activity = new WeakReference<>(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View layout = convertView;
		OldReferenceHolder holder;

		if(layout == null)
		{
			layout = inflater.inflate(layoutResourceId, parent, false);
			holder = new OldReferenceHolder(layout);

			layout.setTag(holder);
		}
		else
			holder = (OldReferenceHolder) layout.getTag();

		String title = list.get(position).title;
		holder.title.setText(TextUtils.isEmpty(title) ? holder.title.getResources().getString(R.string.no_title) : title);
		holder.subtext.setText(HelperFunctions.getReferenceListTypeString(list.get(position).referenceType, getContext()));
		holder.export.getDrawable().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
		holder.export.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				sendPDF(getItem(position));
			}
		});

		return layout;
	}

	private void sendPDF(ReferenceList referenceList)
	{
		PDFGenerator pdfGenerator = new PDFGenerator();
		try
		{
			File pdf = new File(pdfGenerator.generate(referenceList, activity.get().getApplication()));
			Uri uri = Uri.fromFile(pdf);

			Intent intent;

			intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
			intent.putExtra(Intent.EXTRA_SUBJECT, referenceList.title);
			intent.putExtra(Intent.EXTRA_TEXT, activity.get().getString(R.string.email_text));

			intent.setType("message/rfc822");
			activity.get().startActivity(Intent.createChooser(intent, "Email"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
		return convertView.findViewById(R.id.undo_view);
	}

	public class OldReferenceHolder
	{
		@InjectView(R.id.reference_information)
		public TextView title;
		@InjectView(R.id.reference_subtext)
		public TextView subtext;
		@InjectView(R.id.export)
		public ImageView export;

		public OldReferenceHolder(View view)
		{
			ButterKnife.inject(this, view);
		}
	}
}
