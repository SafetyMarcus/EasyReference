package com.au.easyreference.app.ui;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author Marcus Hooper
 */
public class FloatOutAnimation extends FloatAnimation
{
	public FloatOutAnimation(View viewToAnimate)
	{
		super(viewToAnimate);
		setUpAnimation();
	}

	@Override
	protected void setUpAnimation()
	{
		interpolator = new AccelerateDecelerateInterpolator();

		ObjectAnimator floatUpAnimator = getAnimator(-20, 100);
		ObjectAnimator hideAnimator = getAnimator(1000, 500);

		floatUpAnimator.addListener(new FloatInListener(hideAnimator));
		hideAnimator.addListener(new FloatInListener(null));

		animations.add(floatUpAnimator);
		animations.add(hideAnimator);
	}
}
