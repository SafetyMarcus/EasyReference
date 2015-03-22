package com.au.easyreference.app.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ArrayAdapter;
import com.au.easyreference.app.utils.ERApplication;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class ShowOptionsAdapter<T> extends ArrayAdapter<T>
{
	public ShowOptionsAdapter(Context context, int resource)
	{
		super(context, resource);
	}

	public ShowOptionsAdapter(Context context, int resource, ArrayList<T> objects)
	{
		super(context, resource, objects);
	}

	protected void showOptions(final View viewToShow)
	{
		if(!ERApplication.is21Plus)
		{
			viewToShow.setVisibility(View.VISIBLE);
			return;
		}

		// get the center for the clipping circle
		viewToShow.setVisibility(View.VISIBLE);
		int cx = viewToShow.getWidth() / 2;
		int cy = viewToShow.getHeight() / 2;

		// get the final radius for the clipping circle
		int finalRadius = Math.max(viewToShow.getWidth(), viewToShow.getHeight());

		// create the animator for this view (the start radius is zero)
		Animator anim = ViewAnimationUtils.createCircularReveal(viewToShow, cx, cy, 0, finalRadius);
		anim.setDuration(1000);

		// make the view visible and start the animation
		anim.start();
	}

	protected void hideOptions(final View viewToHide)
	{
		if(!ERApplication.is21Plus)
		{
			viewToHide.setVisibility(View.INVISIBLE);
			return;
		}

		// get the center for the clipping circle
		int cx = viewToHide.getWidth() / 2;
		int cy = viewToHide.getHeight() / 2;

		// get the initial radius for the clipping circle
		int initialRadius = viewToHide.getWidth();

		// create the animation (the final radius is zero)
		Animator anim = ViewAnimationUtils.createCircularReveal(viewToHide, cx, cy, initialRadius, 0);

		// make the view invisible when the animation is done
		anim.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				super.onAnimationEnd(animation);
				viewToHide.setVisibility(View.INVISIBLE);
			}
		});

		// start the animation
		anim.start();
	}
}
