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
	private View viewToAnimate;
	private ArrayList<ObjectAnimator> animations = new ArrayList<>();
	private FinishListener finishListener;

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
		bounceUpAnimator.setDuration(75);
		bounceUpAnimator.setInterpolator(interpolator);

		ObjectAnimator settleAnimator = ObjectAnimator.ofFloat(viewToAnimate, "translationY", 0);
		settleAnimator.setDuration(50);
		settleAnimator.setInterpolator(interpolator);

		ObjectAnimator outAnimator = ObjectAnimator.ofFloat(viewToAnimate, "translationY", -1000);
		settleAnimator.setDuration(200);
		settleAnimator.setInterpolator(interpolator);

		inAnimator.addListener(new BounceAnimationListener(bounceUpAnimator, 50));
		bounceUpAnimator.addListener(new BounceAnimationListener(settleAnimator, 25));
		settleAnimator.addListener(new BounceAnimationListener(outAnimator, 5000));
		outAnimator.addListener(new BounceAnimationListener(true));

		animations.add(inAnimator);
		animations.add(bounceUpAnimator);
		animations.add(settleAnimator);
		animations.add(outAnimator);
	}

	public void setFinishListener(FinishListener listener)
	{
		finishListener = listener;
	}

	public void startAnimation()
	{
		animations.get(0).start();
	}

	private class BounceAnimationListener implements Animator.AnimatorListener
	{
		ObjectAnimator nextAnimation;
		int delay;
		boolean finish;

		public BounceAnimationListener(ObjectAnimator nextAnimation, int delay)
		{
			this.nextAnimation = nextAnimation;
			this.delay = delay;
		}

		public BounceAnimationListener(boolean finish)
		{
			this.finish = finish;
		}

		@Override
		public void onAnimationStart(Animator animation)
		{
			if(finish)
				viewToAnimate.setVisibility(View.GONE);
		}

		@Override
		public void onAnimationEnd(Animator animation)
		{
			if(finish)
			{
				finishListener.onFinish();
				return;
			}

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

	public interface FinishListener
	{
		public void onFinish();
	}
}
