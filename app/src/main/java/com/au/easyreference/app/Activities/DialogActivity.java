package com.au.easyreference.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import com.au.easyreference.app.R;

/**
 * @author Marcus Hooper
 */
public class DialogActivity extends ActionBarActivity
{
	private static Fragment fragment;
	public Toolbar toolbar;

	private static void show(Activity context, Fragment fragmentToLoad, int requestCode)
	{
		Intent intent = new Intent(context, DialogActivity.class);
		fragment = fragmentToLoad;
		context.startActivityForResult(intent, requestCode);
	}

	public static void showDialog(Activity context, Fragment fragmentToLoad)
	{
		show(context, fragmentToLoad, 9999);
	}

	public static void showDialog(Activity context, Fragment fragmentToLoad, int requestCode)
	{
		show(context, fragmentToLoad, requestCode);
	}

	public void swapFragments(Fragment newFragment)
	{
		fragment = newFragment;
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, newFragment).commit();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		boolean is21Plus = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.container_dialog_fragment);

		toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		toolbar.setBackgroundColor(getResources().getColor(R.color.easy_reference_red));
		if(is21Plus)
			getWindow().setStatusBarColor(getResources().getColor(R.color.easy_reference_red));

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if(getIntent() != null && getIntent().getExtras() != null)
			fragment.setArguments(getIntent().getExtras());

		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment).commit();
	}
}
