package com.au.easyreference.app.fragments;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.squareup.picasso.Picasso;

/**
 * @author Marcus Hooper
 */
public class MoreInfoDialog extends DialogFragment
{
	public static final String MESSAGE = "message";
	public static final String IMAGE = "image";

	@InjectView(R.id.close)
	ImageView closeButton;
	@InjectView(R.id.message)
	TextView message;
	@InjectView(R.id.image)
	ImageView image;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.more_info_layout, container, false);
		ButterKnife.inject(this, view);

		float density = getResources().getDisplayMetrics().density;
		int size = (int) (300 * density);

		message.setText(getArguments().getString(MESSAGE));
		Picasso.with(getActivity()).load(getArguments().getInt(IMAGE)).resize(size, size).centerInside().into(image);

		closeButton.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
		closeButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				dismiss();
			}
		});

		return view;
	}
}
