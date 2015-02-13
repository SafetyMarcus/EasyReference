package com.au.easyreference.app.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;

/**
 * @author Marcus Hooper
 */
public class AuthorDialogFragment extends Fragment
{
	@InjectView(R.id.first_name)
	public EditText firstName;
	@InjectView(R.id.middle_name)
	public EditText middleName;
	@InjectView(R.id.last_name)
	public EditText lastName;
	@InjectView(R.id.cancel)
	public TextView cancel;
	@InjectView(R.id.save)
	public TextView save;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View layout = inflater.inflate(R.layout.author_dialog_fragment, container, false);
		ButterKnife.inject(this, layout);
		setHasOptionsMenu(false);
		((ContainerDialogFragment) getParentFragment()).toolbar.setTitle(getString(R.string.author));

		cancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				((ContainerDialogFragment) getParentFragment()).onBackPressed();
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
					((ContainerDialogFragment) getParentFragment()).closeListener.onClose(getAuthorString());
					((ContainerDialogFragment) getParentFragment()).onBackPressed();
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
