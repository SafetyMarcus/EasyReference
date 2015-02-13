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

	protected CloseListener closeListener;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
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
			dismiss();
	}

	private void instantiateChild(String key, Bundle args)
	{
		try
		{
			Fragment fragment = (Fragment) Class.forName("com.au.easyreference.app.fragments." + key.substring(key.lastIndexOf(".") + 1)).newInstance();
			fragment.setArguments(args);
			showChild(fragment);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void showChild(Fragment fragment)
	{
		getChildFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment)
				.addToBackStack(fragment.getClass().toString()).commit();
	}

	public void setCloseListener(CloseListener closeListener)
	{
		this.closeListener = closeListener;
	}

	public interface CloseListener
	{
		public abstract void onClose(Object result);
	}
}
