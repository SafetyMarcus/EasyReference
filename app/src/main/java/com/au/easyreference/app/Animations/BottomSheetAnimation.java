package com.au.easyreference.app.Animations;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Marcus on 19/08/2014.
 */
public class BottomSheetAnimation
{
	private static final int DURATION = 400;

	public static void animateSheetUp(View view)
	{
		animate(view, "translationY", 0, new DecelerateInterpolator(1.0f));
	}

	public static void animateSheetUp(View view, float height)
	{
		animate(view, "translationY", height, new DecelerateInterpolator(1.0f));
	}

	public static void animateSheetDown(View view)
	{
		animate(view, "translationY", view.getHeight(), new AccelerateInterpolator(1.0f));
	}

	public static void animateButtonDown(View view)
	{
		ObjectAnimator animTranslate = ObjectAnimator.ofFloat(view, "translationY", 0);
		ObjectAnimator animRotate = ObjectAnimator.ofFloat(view, "rotation", 0);

		animateSet(animTranslate, animRotate, new AccelerateInterpolator(1.0f));
	}

	public static void animateButtonUp(View view, float height)
	{

		ObjectAnimator animTranslate = ObjectAnimator.ofFloat(view, "translationY", height * -1);
		ObjectAnimator animRotate = ObjectAnimator.ofFloat(view, "rotation", 45);

		animateSet(animTranslate, animRotate, new DecelerateInterpolator(1.0f));
	}

	public static void animateShadowIn(View view)
	{
		animate(view, "alpha", 1, new DecelerateInterpolator(1.0f));
	}

	public static void animateShadowOut(View view)
	{
		animate(view, "alpha", 0, new AccelerateInterpolator(1.0f));
	}

	private static void animate(View view, String property, float height, Interpolator interpolator)
	{
		ObjectAnimator anim = ObjectAnimator.ofFloat(view, property, height);
		anim.setDuration(DURATION);
		anim.setInterpolator(interpolator);
		anim.start();
	}

	private static void animateSet(ObjectAnimator animator1, ObjectAnimator animator2, Interpolator interpolator)
	{

		AnimatorSet set = new AnimatorSet();
		set.setDuration(DURATION);
		set.setInterpolator(interpolator);

		set.play(animator1).with(animator2);

		set.start();
	}
}
