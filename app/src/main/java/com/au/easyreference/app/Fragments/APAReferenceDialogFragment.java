package com.au.easyreference.app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.Utils.ERApplication;

/**
 * @author Marcus Hooper
 */
public class APAReferenceDialogFragment extends DialogFragment
{
	public static final String KEY_ID = "key_id";

	@InjectView(R.id.author)
	public EditText author;
	@InjectView(R.id.year)
	public EditText year;
	@InjectView(R.id.title)
	public EditText title;
	@InjectView(R.id.subtitle)
	public EditText subtitle;
	@InjectView(R.id.location)
	public EditText location;
	@InjectView(R.id.publisher)
	public EditText publisher;

	private APAReferenceListener listener;
	private ReferenceItem currentReference;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(getString(R.string.new_reference));
		builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(currentReference == null)
				{
					ReferenceItem newItem = new ReferenceItem(author.getText().toString(), year.getText().toString(),
							title.getText().toString(), subtitle.getText().toString(), location.getText().toString(),
							publisher.getText().toString(), ReferenceItem.BOOK_REFERENCE);

					if(listener != null)
						listener.onReferenceCreated(newItem);
				}
				else
				{
					currentReference.author = author.getText().toString();
					currentReference.year = year.getText().toString();
					currentReference.title = title.getText().toString();
					currentReference.subtitle = subtitle.getText().toString();
					currentReference.location = location.getText().toString();
					currentReference.publisher = publisher.getText().toString();

					if(listener != null)
						listener.onReferenceCreated(currentReference);
				}
			}
		}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dismiss();
			}
		});

		View layout = getActivity().getLayoutInflater().inflate(R.layout.apa_book_reference_layout, null);
		ButterKnife.inject(this, layout);
		builder.setView(layout);

		Bundle args = getArguments();

		if(args != null)
		{
			setUpView(args.getString(KEY_ID));
		}

		listener = ((ReferenceListDialogFragment) getActivity().getFragmentManager().findFragmentByTag("Reference Dialog")).apaListener;

		return builder.create();
	}

	public void setUpView(String id)
	{
		for(ReferenceItem reference : ERApplication.allReferences)
		{
			currentReference = reference;
			if(reference.id.equalsIgnoreCase(id))
			{
				if(reference.author != null && reference.author.length() > 0)
					author.setText(reference.author);
				if(reference.year != null && reference.year.length() > 0)
					year.setText(reference.year);
				if(reference.title != null && reference.title.length() > 0)
					title.setText(reference.title);
				if(reference.subtitle != null && reference.subtitle.length() > 0)
					subtitle.setText(reference.subtitle);
				if(reference.location != null && reference.location.length() > 0)
					location.setText(reference.location);
				if(reference.publisher != null && reference.publisher.length() > 0)
					publisher.setText(reference.publisher);
			}
			break;
		}
	}

	public interface APAReferenceListener
	{
		public void onReferenceCreated(ReferenceItem newReference);
	}
}
