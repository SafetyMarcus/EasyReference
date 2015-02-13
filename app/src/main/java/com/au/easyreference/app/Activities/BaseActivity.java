package com.au.easyreference.app.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
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

	@Override
	public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
	{
		if(ERApplication.is21Plus)
		{
			getWindow().setStatusBarColor(getResources().getColor(R.color.dark_red));
			getWindow().setFeatureInt(Window.FEATURE_ACTIVITY_TRANSITIONS, 1);
			getWindow().setFeatureInt(Window.FEATURE_CONTENT_TRANSITIONS, 1);
			getWindow().setEnterTransition(new Explode());
			getWindow().setExitTransition(new Fade());
		}
		return super.onCreateView(parent, name, context, attrs);
	}

	protected void startActivityForVersion(Activity activity, Intent intent)
	{
		if(ERApplication.is21Plus)
		{
			ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
			startActivity(intent, options.toBundle());
		}
		else
			startActivity(intent);
	}
}
