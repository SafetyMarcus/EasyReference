package com.au.easyreference.app.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;

/**
 * @author Marcus Hooper
 */
public class ContainerDialogFragment extends DialogFragment
{
	public static String TYPE_KEY = "container_child_type";

	@InjectView(R.id.toolbar)
	public Toolbar toolbar;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if(getResources().getBoolean(R.bool.is_tablet))
			getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = inflater.inflate(R.layout.container_dialog_fragment, container, false);
		ButterKnife.inject(this, view);

		toolbar.setBackgroundColor(getResources().getColor(R.color.easy_reference_red));
		toolbar.setNavigationIcon(R.drawable.arrow_back_white);
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});

		instantiateChild(getArguments().getString(TYPE_KEY), getArguments());

		return view;
	}

	public void onBackPressed()
	{
		if(getChildFragmentManager().getBackStackEntryCount() > 1)
			getChildFragmentManager().popBackStack();
		else
		{
			if(getResources().getBoolean(R.bool.is_tablet))
				dismiss();
			else
				getActivity().onBackPressed();
		}
	}

	private void instantiateChild(String key, Bundle args)
	{
		Fragment fragment = null;

		if(key.equalsIgnoreCase(SearchDialog.class.toString()))
			fragment = new SearchDialog();
		else if(key.equalsIgnoreCase(TypeDialog.class.toString()))
			fragment = new TypeDialog();

		fragment.setArguments(args);
		showChild(fragment);
	}

	public void showChild(Fragment fragment)
	{
		getChildFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment)
				.addToBackStack(fragment.getClass().toString()).commit();
	}
}
