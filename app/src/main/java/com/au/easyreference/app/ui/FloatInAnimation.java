package com.au.easyreference.app.ui;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author Marcus Hooper
 */
public class FloatInAnimation extends FloatAnimation
{
	public FloatInAnimation(View viewToAnimate)
	{
		super(viewToAnimate);
		setUpAnimation();
	}

	@Override
	protected void setUpAnimation()
	{
		interpolator = new AccelerateDecelerateInterpolator();

		ObjectAnimator floatInAnimator = getAnimator(-20, 500);
		ObjectAnimator gravitateBackAnimator = getAnimator(10, 100);
		ObjectAnimator settleAnimator = getAnimator(0, 100);

		floatInAnimator.addListener(new FloatInListener(gravitateBackAnimator));
		gravitateBackAnimator.addListener(new FloatInListener(settleAnimator));
		settleAnimator.addListener(new FloatInListener(null));

		animations.add(floatInAnimator);
		animations.add(gravitateBackAnimator);
		animations.add(settleAnimator);
	}
}
