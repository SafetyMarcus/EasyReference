package com.au.easyreference.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.utils.ERApplication;

/**
 * @author Marcus Hooper
 */
public class BaseActivity extends ActionBarActivity
{
	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;

	public void startActivityForVersion(Activity activity, Intent intent)
	{
		if(false) //TODO re-enable to test with an actual device -- ERApplication.is21Plus
		{
//			ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, toolbar, "toolbar");
//			startActivity(intent, options.toBundle());
//			startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
		}
		else
		{
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		if(!ERApplication.is21Plus)
			overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}
}
