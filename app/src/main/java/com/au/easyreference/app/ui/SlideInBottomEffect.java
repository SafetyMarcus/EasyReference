package com.au.easyreference.app.ui;

import android.view.View;
import android.view.ViewPropertyAnimator;
import com.twotoasters.jazzylistview.JazzyEffect;

/**
 * @author Marcus Hooper
 */
public class SlideInBottomEffect implements JazzyEffect
{
	private int duration;

	public SlideInBottomEffect(int duration)
	{
		this.duration = duration;
	}

	@Override
	public void initView(View item, int position, int scrollDirection)
	{
		if(scrollDirection < 0)
			return;

		item.setTranslationY(item.getHeight() / 2 * scrollDirection);
	}

	@Override
	public void setupAnimation(View item, int position, int scrollDirection, ViewPropertyAnimator animator)
	{
		if(scrollDirection < 0)
			return;

		animator.translationYBy(-item.getHeight() / 2 * scrollDirection).setDuration(duration);
	}
}