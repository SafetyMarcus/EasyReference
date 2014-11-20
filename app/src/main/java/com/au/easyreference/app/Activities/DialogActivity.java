package com.au.easyreference.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

/**
 * @author Marcus Hooper
 */
public class DialogActivity extends ActionBarActivity
{
	private static Fragment fragment;

	private static void show(Activity context, Fragment fragmentToLoad, int requestCode)
	{
		Intent intent = new Intent(context, DialogActivity.class);
		fragment = fragmentToLoad;
		context.startActivityForResult(intent, requestCode);
	}

	private static void showDialog(Activity context, Fragment fragmentToLoad)
	{
		show(context, fragmentToLoad, 999999);
	}

	private static void showDialog(Activity context, Fragment fragmentToLoad, int requestCode)
	{
		show(context, fragmentToLoad, requestCode);
	}
}
