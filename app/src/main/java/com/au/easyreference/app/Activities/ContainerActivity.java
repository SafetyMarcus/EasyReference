package com.au.easyreference.app.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.au.easyreference.app.R;
import com.au.easyreference.app.fragments.ContainerDialogFragment;

/**
 * @author Marcus Hooper
 */
public class ContainerActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		boolean is21Plus = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

		if(is21Plus)
			getWindow().setStatusBarColor(getResources().getColor(R.color.dark_red));

		setContentView(R.layout.container_activity);
		ContainerDialogFragment container = new ContainerDialogFragment();
		container.setArguments(getIntent().getExtras());

		getFragmentManager().beginTransaction().replace(R.id.fragment_frame, container).commit();
	}
}
