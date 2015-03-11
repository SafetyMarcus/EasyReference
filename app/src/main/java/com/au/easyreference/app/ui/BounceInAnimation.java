package com.au.easyreference.app.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class BounceInAnimation
{
	View viewToAnimate;
	ArrayList<ObjectAnimator> animations = new ArrayList<>();

	public BounceInAnimation(View viewToAnimate)
	{
		this.viewToAnimate = viewToAnimate;
		setUpAnimation();
	}

	private void setUpAnimation()
	{
		AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

		ObjectAnimator inAnimator = ObjectAnimator.ofFloat(viewToAnimate, "translationY", 0);
		inAnimator.setDuration(200);
		inAnimator.setInterpolator(interpolator);
		viewToAnimate.setVisibility(View.VISIBLE);

		ObjectAnimator bounceUpAnimator = ObjectAnimator.ofFloat(viewToAnimate, "translationY", -50);
		bounceUpAnimator.setDuration(100);
		bounceUpAnimator.setInterpolator(interpolator);

		ObjectAnimator settleAnimator = ObjectAnimator.ofFloat(viewToAnimate, "translationY", 0);
		settleAnimator.setDuration(100);
		settleAnimator.setInterpolator(interpolator);

		inAnimator.addListener(new BounceAnimationListener(bounceUpAnimator, 50));
		bounceUpAnimator.addListener(new BounceAnimationListener(settleAnimator, 25));

		animations.add(inAnimator);
		animations.add(bounceUpAnimator);
		animations.add(settleAnimator);
	}

	public void startAnimation()
	{
		animations.get(0).start();
	}

	private class BounceAnimationListener implements Animator.AnimatorListener
	{
		ObjectAnimator nextAnimation;
		int delay;

		public BounceAnimationListener(ObjectAnimator nextAnimation, int delay)
		{
			this.nextAnimation = nextAnimation;
			this.delay = delay;
		}

		@Override
		public void onAnimationStart(Animator animation)
		{
		}

		@Override
		public void onAnimationEnd(Animator animation)
		{
			viewToAnimate.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					nextAnimation.start();
				}
			}, delay);
		}

		@Override
		public void onAnimationCancel(Animator animation)
		{
		}

		@Override
		public void onAnimationRepeat(Animator animation)
		{
		}
	}
}
