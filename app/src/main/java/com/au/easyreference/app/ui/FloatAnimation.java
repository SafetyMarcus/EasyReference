package com.au.easyreference.app.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public abstract class FloatAnimation
{
	public View viewToAnimate;
	public ArrayList<ObjectAnimator> animations = new ArrayList<>();
	public AccelerateDecelerateInterpolator interpolator;

	public FloatAnimation(View viewToAnimate)
	{
		this.viewToAnimate = viewToAnimate;
	}

	protected abstract void setUpAnimation();

	public void animate()
	{
		animations.get(0).start();
	}

	protected ObjectAnimator getAnimator(float value, int duration)
	{
		ObjectAnimator animator = ObjectAnimator.ofFloat(viewToAnimate, "translationY", value);
		animator.setDuration(duration);
		animator.setInterpolator(interpolator);

		return animator;
	}

	public long getDuration()
	{
		long duration = 0;

		for(ObjectAnimator animator : animations)
			duration += animator.getDuration();

		return duration;
	}

	protected class FloatInListener implements ObjectAnimator.AnimatorListener
	{
		private ObjectAnimator nextAnimation;

		public FloatInListener(ObjectAnimator nextAnimation)
		{
			this.nextAnimation = nextAnimation;
		}

		@Override
		public void onAnimationStart(Animator animation)
		{
		}

		@Override
		public void onAnimationEnd(Animator animation)
		{
			if(nextAnimation == null)
				return;

			nextAnimation.start();
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
