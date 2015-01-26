package com.au.easyreference.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.activities.DialogActivity;
import com.au.easyreference.app.R;

/**
 * @author Marcus Hooper
 */
public class AuthorDialogFragment extends Fragment
{
	public static final int GET_AUTHOR = 1111;
	public static final String AUTHOR_STRING = "author";

	@InjectView(R.id.first_name)
	public EditText firstName;
	@InjectView(R.id.middle_name)
	public EditText middleName;
	@InjectView(R.id.last_name)
	public EditText lastName;
	@InjectView(R.id.cancel)
	public Button cancel;
	@InjectView(R.id.save)
	public Button save;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View layout = inflater.inflate(R.layout.author_dialog_fragment, container, false);
		ButterKnife.inject(this, layout);
		setHasOptionsMenu(false);
		((DialogActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.author));
		((DialogActivity) getActivity()).toolbar.setTitle(getString(R.string.author));
		((DialogActivity) getActivity()).toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().onBackPressed();
			}
		});

		cancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().onBackPressed();
			}
		});

		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(firstName.getText().length() == 0)
					showWarningDialog(true);
				else if(lastName.getText().length() == 0)
					showWarningDialog(false);
				else
				{
					Intent result = new Intent();
					result.putExtra(AUTHOR_STRING, getAuthorString());
					getActivity().setResult(Activity.RESULT_OK, result);

					getActivity().onBackPressed();
				}
			}
		});

		return layout;
	}

	public String getAuthorString()
	{
		StringBuilder author = new StringBuilder();

		author.append(lastName.getText().toString()).append(", ");

		author.append(firstName.getText().charAt(0)).append('.');

		if(middleName.getText().length() > 0)
			author.append(' ').append(middleName.getText().charAt(0)).append('.');

		return author.toString();
	}

	public void showWarningDialog(boolean firstName)
	{
		int missingId = firstName ? R.string.first_name : R.string.last_name;
		String missingString = getString(missingId);

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle(getString(R.string.missing_title, missingString));
		alertDialog.setMessage(getString(R.string.missing_body, missingString));
		alertDialog.setPositiveButton(android.R.string.ok, null);
		alertDialog.show();
	}
}
