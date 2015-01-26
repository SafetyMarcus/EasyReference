package com.au.easyreference.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.InjectView;
import butterknife.Optional;
import com.au.easyreference.app.fragments.AuthorDialogFragment;
import com.au.easyreference.app.R;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;
import com.au.easyreference.app.utils.ERApplication;

/**
 * @author Marcus Hooper
 */
public class BaseAPAReferenceActivity extends ActionBarActivity
{
	public static final int SAVE = 1;
	public static final String KEY_LIST_ID = "key_list_id";
	public static final String KEY_ID = "key_id";

	@InjectView(R.id.author)
	public EditText author;
	@InjectView(R.id.author_button)
	public Button authorButton;
	@InjectView(R.id.year)
	public EditText year;
	@InjectView(R.id.title)
	public EditText title;
	@Optional
	@InjectView(R.id.subtitle)
	public EditText subtitle;

	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;

	public ReferenceList referenceList;
	public ReferenceItem currentReference;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back_white));
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});

		toolbar.setBackgroundColor(getResources().getColor(R.color.easy_reference_red));
		setSupportActionBar(toolbar);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			getWindow().setStatusBarColor(getResources().getColor(R.color.dark_red));

		Bundle args = getIntent().getExtras();

		if(args != null)
		{
			for(ReferenceList list : ERApplication.referenceLists)
				if(list.id.equalsIgnoreCase(args.getString(KEY_LIST_ID)))
					referenceList = list;

			if(args.containsKey(KEY_ID))
				setUpView(args.getString(KEY_ID));
		}

		authorButton.setOnClickListener(new AuthorClickListener());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
		}

		return super.onOptionsItemSelected(item);
	}

	public void setUpView(String id)
	{
		currentReference = referenceList.getReferenceForId(id);

		if(currentReference != null)
		{
			if(currentReference.author != null && currentReference.author.length() > 0)
				author.setText(currentReference.author);
			if(currentReference.year != null && currentReference.year.length() > 0)
				year.setText(currentReference.year);
			if(currentReference.title != null && currentReference.title.length() > 0)
				title.setText(currentReference.title);
			if(subtitle != null && currentReference.subtitle != null && currentReference.subtitle.length() > 0)
				subtitle.setText(currentReference.subtitle);
		}
	}

	public void save()
	{
		currentReference.author = author.getText().toString();
		currentReference.year = year.getText().toString();
		currentReference.title = title.getText().toString();
		currentReference.subtitle = subtitle != null ? subtitle.getText().toString() : "";
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != Activity.RESULT_OK)
			return;

		switch(requestCode)
		{
			case AuthorDialogFragment.GET_AUTHOR:
				if(author.getText().length() > 0)
					author.append(", ");

				author.append(data.getStringExtra(AuthorDialogFragment.AUTHOR_STRING));
				break;

			default:
				super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public class AuthorClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Fragment authorFragment = new AuthorDialogFragment();
			DialogActivity.showDialog(BaseAPAReferenceActivity.this, authorFragment, AuthorDialogFragment.GET_AUTHOR);
		}
	}
}