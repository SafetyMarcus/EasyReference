package com.au.easyreference.app.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.apaactivities.APABookChapterReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APABookReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APAJournalReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APAWebPageReferenceActivity;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;
import com.au.easyreference.app.references.ReferenceListAdapter;
import com.au.easyreference.app.ui.FloatInAnimation;
import com.au.easyreference.app.ui.FloatOutAnimation;
import com.au.easyreference.app.ui.SlideInBottomEffect;
import com.au.easyreference.app.utils.ERApplication;
import com.au.easyreference.app.utils.HelperFunctions;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class ReferenceListActivity extends BaseActivity
{
	public static final String KEY_TYPE = "type";
	public static final String KEY_ID = "id";

	@Bind(R.id.toolbar)
	protected Toolbar toolbar;
	@Bind(R.id.references_list_view)
	protected JazzyListView referencesListView;
	@Bind(R.id.list_title)
	protected EditText title;

	@Bind(R.id.plus_button)
	protected ImageView plusButton;
	@Bind(R.id.plus_book)
	protected ImageView plusBook;
	@Bind(R.id.plus_web)
	protected ImageView plusWeb;
	@Bind(R.id.plus_journal)
	protected ImageView plusJournal;
	@Bind(R.id.plus_book_chapter)
	protected ImageView plusBookChapter;

	public ReferenceListAdapter adapter;
	public int type;
	public ReferenceList referenceList;

	boolean hasAnimatedOut;
	boolean expandedOptions;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reference_list_activity);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back_white));
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});

		Intent intent = getIntent();
		if(intent != null)
		{
			type = intent.getIntExtra(KEY_TYPE, 0);
			String id = intent.getStringExtra(KEY_ID);

			if(id != null)
			{
				referenceList = HelperFunctions.getReferenceListForId(id);
				type = referenceList.referenceType;
			}
			else
			{
				referenceList = new ReferenceList("", type, new ArrayList<ReferenceItem>());
				referenceList.saveToFile(getApplication());
				ERApplication.referenceLists.add(referenceList);
			}

			title.setText(referenceList.title);
		}

		adapter = new ReferenceListAdapter(this, referenceList, getLayoutInflater());
		referencesListView.setTransitionEffect(new SlideInBottomEffect(300));
		referencesListView.setAdapter(adapter);
		referencesListView.setEmptyView(findViewById(android.R.id.empty));
		referencesListView.addFooterView(getLayoutInflater().inflate(R.layout.footer, referencesListView, false), null, false);
		referencesListView.setFooterDividersEnabled(false);

		setVisibility(View.GONE);

		plusBook.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
		plusJournal.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
		MiniPlusListener miniPlusListener = new MiniPlusListener();
		plusBook.setOnClickListener(miniPlusListener);
		plusJournal.setOnClickListener(miniPlusListener);
		plusBookChapter.setOnClickListener(miniPlusListener);
		plusWeb.setOnClickListener(miniPlusListener);

		plusButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(!expandedOptions)
					expandFab();
				else
					collapseFab(null);

				expandedOptions = !expandedOptions;
			}
		});
	}

	private static final float up = -HelperFunctions.convertIntToDp(80);

	private void expandFab()
	{
		setVisibility(View.VISIBLE);

		ObjectAnimator.ofFloat(plusButton, "rotation", 0, 45).start();
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(animateViewUp(plusBook, false), animateViewUp(plusJournal, false), animateViewUp(plusBookChapter, false), animateViewUp(plusWeb, false));
		animatorSet.addListener(new AnimationEndListener()
		{
			@Override
			void onEnd(Animator animation)
			{
				AnimatorSet set = new AnimatorSet();
				Animator translateBookDown = ObjectAnimator.ofFloat(plusBook, TRANSLATION_Y, up, -HelperFunctions.convertIntToDp(70)).setDuration(100);
				set.playTogether(translateBookDown, animateViewLeftDown(plusJournal, 1, false), animateViewLeftDown(plusBookChapter, 1, false), animateViewLeftDown(plusWeb, 1, false));
				set.addListener(new AnimationEndListener()
				{
					@Override
					void onEnd(Animator animation)
					{
						AnimatorSet set = new AnimatorSet();
						set.playTogether(animateViewLeftDown(plusBookChapter, 2, false), animateViewLeftDown(plusWeb, 2, false));
						set.addListener(new AnimationEndListener()
						{
							@Override
							void onEnd(Animator animation)
							{
								animateViewLeftDown(plusWeb, 3, false).start();
							}
						});
						set.start();
					}
				});
				set.start();
			}
		});
		animatorSet.start();
	}

	private Animator animateViewUp(View view, boolean reverse)
	{
		if(reverse)
			return ObjectAnimator.ofFloat(view, TRANSLATION_Y, up, 0).setDuration(300);
		return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, up).setDuration(300);
	}

	private Animator animateViewLeftDown(View view, int level, boolean reverse)
	{
		float left = 0;
		float down = 0;
		float originx = 0;
		float originy = 0;

		if(level == 1)
		{
			left = -HelperFunctions.convertIntToDp(44);
			originx = 0;
			down = -HelperFunctions.convertIntToDp(60);
			originy = up;
		}
		else if(level == 2)
		{
			left = -HelperFunctions.convertIntToDp(76);
			originx = -HelperFunctions.convertIntToDp(44);
			down = -HelperFunctions.convertIntToDp(28);
			originy = -HelperFunctions.convertIntToDp(60);
		}
		else if(level == 3)
		{
			left = -HelperFunctions.convertIntToDp(76);
			originx = -HelperFunctions.convertIntToDp(76);
			down = HelperFunctions.convertIntToDp(16);
			originy = -HelperFunctions.convertIntToDp(28);
		}

		AnimatorSet set = new AnimatorSet();
		Animator leftAnimation;
		Animator downAnimation;

		if(reverse)
		{
			leftAnimation = ObjectAnimator.ofFloat(view, TRANSLATION_X, left, originx).setDuration(150);
			downAnimation = ObjectAnimator.ofFloat(view, TRANSLATION_Y, down, originy).setDuration(150);
		}
		else
		{
			leftAnimation = ObjectAnimator.ofFloat(view, TRANSLATION_X, originx, left).setDuration(150);
			downAnimation = ObjectAnimator.ofFloat(view, TRANSLATION_Y, originy, down).setDuration(150);
		}

		set.playTogether(leftAnimation, downAnimation);
		return set;
	}

	private void collapseFab(final ReferenceItem referenceItem)
	{
		ObjectAnimator.ofFloat(plusButton, "rotation", 45, 0).start();
		Animator set = animateViewLeftDown(plusWeb, 3, true);
		set.addListener(new AnimationEndListener()
		{
			@Override
			void onEnd(Animator animation)
			{
				AnimatorSet set = new AnimatorSet();
				set.playTogether(animateViewLeftDown(plusBookChapter, 2, true), animateViewLeftDown(plusWeb, 2, true));
				set.addListener(new AnimationEndListener()
				{
					@Override
					void onEnd(Animator animation)
					{
						AnimatorSet set = new AnimatorSet();
						Animator translateBookDown = ObjectAnimator.ofFloat(plusBook, TRANSLATION_Y, -HelperFunctions.convertIntToDp(70), up).setDuration(100);
						set.playTogether(translateBookDown, animateViewLeftDown(plusJournal, 1, true), animateViewLeftDown(plusBookChapter, 1, true), animateViewLeftDown(plusWeb, 1, true));
						set.addListener(new AnimationEndListener()
						{
							@Override
							void onEnd(Animator animation)
							{
								AnimatorSet animatorSet = new AnimatorSet();
								animatorSet.playTogether(animateViewUp(plusBook, true), animateViewUp(plusJournal, true), animateViewUp(plusBookChapter, true), animateViewUp(plusWeb, true));
								animatorSet.addListener(new AnimationEndListener()
								{
									@Override
									void onEnd(Animator animation)
									{
										setVisibility(View.GONE);
										if(referenceItem != null)
											showReference(referenceItem);
									}
								});
								animatorSet.start();
							}
						});
						set.start();
					}
				});
				set.start();
			}
		});
		set.start();
	}

	private void setVisibility(int visibility)
	{
		plusBook.setVisibility(visibility);
		plusJournal.setVisibility(visibility);
		plusBookChapter.setVisibility(visibility);
		plusWeb.setVisibility(visibility);
	}

	private static final String TRANSLATION_Y = "translationY";
	private static final String TRANSLATION_X = "translationX";

	public void showReference(final ReferenceItem referenceItem)
	{
		Class intentClass = null;
		if(referenceItem.type == ReferenceItem.BOOK_REFERENCE)
			intentClass = APABookReferenceActivity.class;
		else if(referenceItem.type == ReferenceItem.JOURNAL_REFERENCE)
			intentClass = APAJournalReferenceActivity.class;
		else if(referenceItem.type == ReferenceItem.BOOK_CHAPTER)
			intentClass = APABookChapterReferenceActivity.class;
		else if(referenceItem.type == ReferenceItem.WEB_PAGE)
			intentClass = APAWebPageReferenceActivity.class;

		if(intentClass != null)
		{
			hasAnimatedOut = true;
			new FloatOutAnimation(plusButton).animate();

			final Class finalIntentClass = intentClass;
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					Intent intent = new Intent(ReferenceListActivity.this, finalIntentClass);

					intent.putExtra(APABookReferenceActivity.KEY_LIST_ID, referenceList.id);
					intent.putExtra(APABookReferenceActivity.KEY_ID, referenceItem.id);

					startActivityForVersion(intent);
				}
			}, 300);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	private void addNewReference(int type)
	{
		referenceList.referenceList.add(new ReferenceItem(type));
		adapter.notifyDataSetChanged();
		plusButton.performClick();

		collapseFab(referenceList.referenceList.get(referenceList.referenceList.size() - 1));
	}

	@Override
	public void onBackPressed()
	{
		referenceList.title = title.getText().toString();
		referenceList.saveToFile(getApplication());

		super.onBackPressed();
	}

	@Override
	public void onPause()
	{
		ERApplication.BUS.unregister(this);
		referenceList.saveToFile(getApplication());
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged();
		ERApplication.BUS.register(this);

		if(hasAnimatedOut)
		{
			hasAnimatedOut = false;
			new FloatInAnimation(plusButton).animate();
		}
	}

	private class MiniPlusListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			int type;
			switch(v.getId())
			{
				case R.id.plus_web:
					type = ReferenceItem.WEB_PAGE;
					break;

				case R.id.plus_book_chapter:
					type = ReferenceItem.BOOK_CHAPTER;
					break;

				case R.id.plus_journal:
					type = ReferenceItem.JOURNAL_REFERENCE;
					break;

				case R.id.plus_book:
				default:
					type = ReferenceItem.BOOK_REFERENCE;
			}

			addNewReference(type);
		}
	}

	private abstract class AnimationEndListener implements Animator.AnimatorListener
	{
		@Override
		public void onAnimationStart(Animator animation)
		{
		}

		@Override
		public void onAnimationEnd(Animator animation)
		{
			onEnd(animation);
		}

		@Override
		public void onAnimationCancel(Animator animation)
		{
		}

		@Override
		public void onAnimationRepeat(Animator animation)
		{
		}

		abstract void onEnd(Animator animation);
	}
}
