package com.au.easyreference.app.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Pair;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.utils.ERApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcus Hooper
 */
public class BaseActivity extends ActionBarActivity
{
	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(!ERApplication.is21Plus)
			return;

		Transition transition = new Fade();
		transition.excludeTarget(android.R.id.statusBarBackground, true);
		transition.excludeTarget(android.R.id.navigationBarBackground, true);
		getWindow().setEnterTransition(transition);
		getWindow().setExitTransition(transition);
	}

	public void startActivityForVersion(Intent intent, View... sharedViews)
	{
		Bundle options = new Bundle();

		//TODO figure out crash starting activity while rotated
		int rotation = getWindow().getWindowManager().getDefaultDisplay().getRotation();
		if(rotation != Surface.ROTATION_90 && rotation != Surface.ROTATION_270)
		{
			if(ERApplication.is21Plus)
			{
				View statusBar = findViewById(android.R.id.statusBarBackground);
				View navigationBar = findViewById(android.R.id.navigationBarBackground);

				List<Pair<View, String>> views = new ArrayList<>();
				views.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
				views.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
				views.add(Pair.create(findViewById(R.id.toolbar), toolbar.getTransitionName()));
				for(View sharedView : sharedViews)
					views.add(new Pair<>(sharedView, sharedView.getTransitionName()));

				options = ActivityOptions.makeSceneTransitionAnimation(this, views.toArray(new Pair[views.size()])).toBundle();
			}
			else
				options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, toolbar, "toolbar").toBundle();
		}

		if(android.os.Build.VERSION.SDK_INT < 16)
			startActivity(intent);
		else
			startActivity(intent, options);

		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void startActivityForVersion(Intent intent)
	{
		startActivityForVersion(intent, toolbar);
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		if(!ERApplication.is21Plus)
			overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}
}
