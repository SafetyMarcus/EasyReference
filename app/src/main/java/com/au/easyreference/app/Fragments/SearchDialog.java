package com.au.easyreference.app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;

/**
 * @author Marcus Hooper
 */
public class SearchDialog extends DialogFragment
{
	@InjectView(R.id.doi_tab)
	TextView doiTab;
	@InjectView(R.id.doi_highlight)
	ImageView doiHighight;

	@InjectView(R.id.isbn_tab)
	TextView isbnTab;
	@InjectView(R.id.isbn_highlight)
	ImageView isbnHighight;

	@InjectView(R.id.issn_tab)
	TextView issnTab;
	@InjectView(R.id.issn_highlight)
	ImageView issnHighight;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(getString(R.string.search));
		View layout = getActivity().getLayoutInflater().inflate(R.layout.search_dialog, null);
		ButterKnife.inject(this, layout);
		builder.setView(layout);

		doiTab.setOnClickListener(new TabClickListener());
		issnTab.setOnClickListener(new TabClickListener());
		isbnTab.setOnClickListener(new TabClickListener());

		return builder.create();
	}

	private class TabClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			resetViews();

			switch(view.getId())
			{
				case R.id.doi_tab:
					doiTab.setTextColor(getResources().getColor(R.color.bright_light_blue));
					doiHighight.setVisibility(View.VISIBLE);
					break;
				case R.id.issn_tab:
					issnTab.setTextColor(getResources().getColor(R.color.bright_light_blue));
					issnHighight.setVisibility(View.VISIBLE);
					break;
				case R.id.isbn_tab:
					isbnTab.setTextColor(getResources().getColor(R.color.bright_light_blue));
					isbnHighight.setVisibility(View.VISIBLE);
					break;
			}
		}

		public void resetViews()
		{
			doiTab.setTextColor(getResources().getColor(android.R.color.black));
			isbnTab.setTextColor(getResources().getColor(android.R.color.black));
			issnTab.setTextColor(getResources().getColor(android.R.color.black));

			doiHighight.setVisibility(View.GONE);
			isbnHighight.setVisibility(View.GONE);
			issnHighight.setVisibility(View.GONE);
		}
	}
}
