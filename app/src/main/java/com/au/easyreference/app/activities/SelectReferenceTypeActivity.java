package com.au.easyreference.app.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeTransform;
import android.view.View;
import android.view.ViewAnimationUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.au.easyreference.app.R;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.utils.ERApplication;

/**
 * @author Marcus Hooper
 */
public class SelectReferenceTypeActivity extends BaseActivity implements View.OnClickListener
{
	public static final String TYPE = "type";

	@Bind(R.id.book)
	View book;
	@Bind(R.id.journal)
	View journal;
	@Bind(R.id.chapter)
	View chapter;
	@Bind(R.id.web)
	View web;
	@Bind(R.id.plus_button)
	View plus;
	@Bind(R.id.background)
	View background;
	@Bind(R.id.main)
	View main;

	boolean animating;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_reference_type_activivty);
		ButterKnife.bind(this);

		if(ERApplication.is21Plus)
		{
			main.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
			{
				@Override
				public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
				{
					background.removeOnLayoutChangeListener(this);
					showView();
				}
			});
		}
		else
			background.setVisibility(View.VISIBLE);

		plus.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});
		book.setOnClickListener(this);
		journal.setOnClickListener(this);
		chapter.setOnClickListener(this);
		web.setOnClickListener(this);
	}

	private void showView()
	{
		if(animating)
			return;

		animating = true;
		plus.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				// get the center for the clipping circle
				background.setVisibility(View.VISIBLE);
				int cx = background.getWidth() / 2;
				int cy = background.getHeight() / 2;

				// get the final radius for the clipping circle
				int finalRadius = Math.max(background.getWidth(), background.getHeight());

				// create the animator for this view (the start radius is zero)
				Animator anim = ViewAnimationUtils.createCircularReveal(background, cx, cy, 0, finalRadius);
				anim.setDuration(500);

				// make the view visible and start the animation
				anim.start();
				animating = false;
			}
		}, 200);
	}

	void hideView()
	{
		// get the center for the clipping circle
		int cx = background.getMeasuredWidth() / 2;
		int cy = background.getMeasuredHeight() / 2;

		// get the initial radius for the clipping circle
		int initialRadius = background.getWidth() / 2;

		// create the animation (the final radius is zero)
		Animator anim =
				ViewAnimationUtils.createCircularReveal(background, cx, cy, initialRadius, 0);

		// make the view invisible when the animation is done
		anim.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				super.onAnimationEnd(animation);
				background.setVisibility(View.INVISIBLE);
			}
		});

		// start the animation
		anim.start();
	}

	@Override
	public void onBackPressed()
	{
		hideView();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v)
	{
		Intent result = new Intent();
		int type;
		switch(v.getId())
		{
			case R.id.journal:
				type = ReferenceItem.JOURNAL_REFERENCE;
				break;
			case R.id.chapter:
				type = ReferenceItem.BOOK_CHAPTER;
				break;
			case R.id.web:
				type = ReferenceItem.WEB_PAGE;
				break;

			case R.id.book:
			default:
				type = ReferenceItem.BOOK_REFERENCE;
				break;
		}

		result.putExtra(TYPE, type);
		setIntent(result);
		setResult(RESULT_OK, result);
		onBackPressed();
	}
}
