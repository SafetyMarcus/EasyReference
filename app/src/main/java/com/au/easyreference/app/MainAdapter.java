package com.au.easyreference.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.activities.MainActivity;
import com.au.easyreference.app.activities.ReferenceListActivity;
import com.au.easyreference.app.references.ReferenceList;
import com.au.easyreference.app.utils.ERApplication;
import com.au.easyreference.app.utils.HelperFunctions;
import com.au.easyreference.app.utils.PDFGenerator;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class MainAdapter extends ArrayAdapter<ReferenceList>
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
		OldReferenceHolder holder;

		if(layout == null)
		{
			layout = inflater.inflate(R.layout.reference_list_item, parent, false);
			holder = new OldReferenceHolder(layout);

			layout.setTag(holder);
		}
		else
			holder = (OldReferenceHolder) layout.getTag();

		String title = lists.get(position).title;
		holder.title.setText(TextUtils.isEmpty(title) ? holder.title.getResources().getString(R.string.no_title) : title);
		holder.subtext.setText(HelperFunctions.getReferenceListTypeString(lists.get(position).referenceType, getContext()));

		holder.edit.getDrawable().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
		holder.export.getDrawable().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
		holder.delete.getDrawable().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

		layout.setOnClickListener(new OnRowClickListener(position));
		holder.edit.setOnClickListener(new OnRowClickListener(position));
		holder.export.setOnClickListener(new OnExportClickListener(position));
		holder.delete.setOnClickListener(new OnDeleteClickListener(position));

		if(selected == position)
			showOptions(holder.optionsLayout);
		else if(holder.optionsLayout.getVisibility() == View.VISIBLE)
			hideOptions(holder.optionsLayout);

		return layout;
	}

	private void showOptions(final View viewToShow)
	{
		if(!ERApplication.is21Plus)
		{
			viewToShow.setVisibility(View.VISIBLE);
			return;
		}

		// get the center for the clipping circle
		viewToShow.setVisibility(View.VISIBLE);
		int cx = viewToShow.getWidth() / 2;
		int cy = viewToShow.getHeight() / 2;

		// get the final radius for the clipping circle
		int finalRadius = Math.max(viewToShow.getWidth(), viewToShow.getHeight());

		// create the animator for this view (the start radius is zero)
		Animator anim = ViewAnimationUtils.createCircularReveal(viewToShow, cx, cy, 0, finalRadius);
		anim.setDuration(1000);

		// make the view visible and start the animation
		anim.start();
	}

	private void hideOptions(final View viewToHide)
	{
		if(!ERApplication.is21Plus)
		{
			viewToHide.setVisibility(View.INVISIBLE);
			return;
		}

		// get the center for the clipping circle
		int cx = viewToHide.getWidth() / 2;
		int cy = viewToHide.getHeight() / 2;

		// get the initial radius for the clipping circle
		int initialRadius = viewToHide.getWidth();

		// create the animation (the final radius is zero)
		Animator anim = ViewAnimationUtils.createCircularReveal(viewToHide, cx, cy, initialRadius, 0);

		// make the view invisible when the animation is done
		anim.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				super.onAnimationEnd(animation);
				viewToHide.setVisibility(View.INVISIBLE);
			}
		});

		// start the animation
		anim.start();
	}

	public class OldReferenceHolder
	{
		@InjectView(R.id.reference_information)
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

		public OldReferenceHolder(View view)
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
	}

	public class OnRowClickListener implements View.OnClickListener
	{
		private int position;

		public OnRowClickListener(int position)
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
				activity.get().startActivityForVersion(activity.get(), referenceIntent);
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
			new File(HelperFunctions.getReferenceListPath(ERApplication.referenceLists.get(position).id, activity.get().getApplication())).delete();
			ERApplication.referenceLists.remove(position);
			notifyDataSetChanged();
		}
	}
}
