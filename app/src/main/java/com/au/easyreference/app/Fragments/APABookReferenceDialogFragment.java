package com.au.easyreference.app.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.Activities.ReferenceListActivity;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.Utils.ERApplication;

/**
 * @author Marcus Hooper
 */
public class APABookReferenceDialogFragment extends BaseAPAReferenceDialogFragment
{
	public static final String KEY_ID = "key_id";

	@InjectView(R.id.location)
	public EditText location;
	@InjectView(R.id.publisher)
	public EditText publisher;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View layout = getActivity().getLayoutInflater().inflate(R.layout.apa_book_reference_layout, null);
		ButterKnife.inject(this, layout);

		super.onCreateView(inflater, container, savedInstanceState);

		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
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

				dismiss();
			}
		});

		Bundle args = getArguments();

		if(args != null)
			setUpView(args.getString(KEY_ID));

		listener = ((ReferenceListActivity) getActivity()).apaListener;

		return layout;
	}

	public void setUpView(String id)
	{
		super.setUpView(id);
		for(ReferenceItem reference : ERApplication.allReferences)
		{
			currentReference = reference;
			if(reference.id.equalsIgnoreCase(id))
			{
				if(reference.location != null && reference.location.length() > 0)
					location.setText(reference.location);
				if(reference.publisher != null && reference.publisher.length() > 0)
					publisher.setText(reference.publisher);
			}
			break;
		}
	}
}
