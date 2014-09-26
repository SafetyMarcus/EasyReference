package com.au.easyreference.app.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.Utils.ERApplication;

/**
 * @author Marcus Hooper
 */
public class BaseAPAReferenceDialogFragment extends DialogFragment
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

	@InjectView(R.id.cancel)
	protected Button cancel;
	@InjectView(R.id.save)
	protected Button save;

	public APAReferenceListener listener;
	public ReferenceItem currentReference;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		cancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				dismiss();
			}
		});

		Bundle args = getArguments();

		if(args != null)
		{
			setUpView(args.getString(KEY_ID));
		}

		return null;
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
			}
			break;
		}
	}

	public interface APAReferenceListener
	{
		public void onReferenceCreated(ReferenceItem newReference);
	}
}
