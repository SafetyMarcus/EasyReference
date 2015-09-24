package com.au.easyreference.app.fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.au.easyreference.app.R;

/**
 * @author Marcus Hooper
 */
public class AuthorDialogFragment extends DialogFragment
{
	@Bind(R.id.first_name)
	public EditText firstName;
	@Bind(R.id.middle_name)
	public EditText middleName;
	@Bind(R.id.last_name)
	public EditText lastName;
	@Bind(R.id.cancel)
	public TextView cancel;
	@Bind(R.id.save)
	public TextView save;
	@Bind(R.id.toolbar)
	public Toolbar toolbar;

	private ContainerDialogFragment.CloseListener closeListener;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View layout = inflater.inflate(R.layout.author_dialog_fragment, container, false);
		ButterKnife.bind(this, layout);
		setHasOptionsMenu(false);
		toolbar.setTitle(getString(R.string.author));

		cancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dismiss();
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
					closeListener.onClose(getAuthorString());
					dismiss();
				}
			}
		});

		return layout;
	}

	public void setCloseListener(ContainerDialogFragment.CloseListener listener)
	{
		this.closeListener = listener;
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
