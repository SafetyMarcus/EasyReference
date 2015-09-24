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
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
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

	@Bind(R.id.book_info)
	protected TextView bookInfo;
	@Bind(R.id.journal_info)
	protected TextView journalInfo;
	@Bind(R.id.chapter_info)
	protected TextView chapterInfo;
	@Bind(R.id.web_info)
	protected TextView webInfo;

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

		plusBook.setVisibility(View.GONE);
		plusJournal.setVisibility(View.GONE);
		plusBookChapter.setVisibility(View.GONE);
		plusWeb.setVisibility(View.GONE);
		bookInfo.setAlpha(0);
		journalInfo.setAlpha(0);
		chapterInfo.setAlpha(0);
		webInfo.setAlpha(0);

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

	private void expandFab()
	{
		plusBook.setVisibility(View.VISIBLE);
		plusJournal.setVisibility(View.VISIBLE);
		plusBookChapter.setVisibility(View.VISIBLE);
		plusWeb.setVisibility(View.VISIBLE);

		bookInfo.setTranslationY(-490);
		bookInfo.setTranslationX(190);
		journalInfo.setTranslationY(-390);
		journalInfo.setTranslationX(-20);
		chapterInfo.setTranslationY(-250);
		chapterInfo.setTranslationX(-250);
		webInfo.setTranslationY(-100);
		webInfo.setTranslationX(-340);

		ObjectAnimator.ofFloat(plusButton, "rotation", 0, 45).start();
		AnimatorSet animatorSet = new AnimatorSet();
		Animator translateBookUp = ObjectAnimator.ofFloat(plusBook, TRANSLATION_Y, 0, -360).setDuration(300);
		Animator translateJournalUp = ObjectAnimator.ofFloat(plusJournal, TRANSLATION_Y, 0, -360).setDuration(300);
		Animator translateBookChapterUp = ObjectAnimator.ofFloat(plusBookChapter, TRANSLATION_Y, 0, -360).setDuration(300);
		Animator translateWebUp = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_Y, 0, -360).setDuration(300);
		animatorSet.playTogether(translateBookUp, translateJournalUp, translateBookChapterUp, translateWebUp);
		animatorSet.addListener(new AnimationEndListener()
		{
			@Override
			void onEnd(Animator animation)
			{
				AnimatorSet set = new AnimatorSet();
				Animator translateBookDown = ObjectAnimator.ofFloat(plusBook, TRANSLATION_Y, -360, -300).setDuration(100);
				Animator journalLeft = ObjectAnimator.ofFloat(plusJournal, TRANSLATION_X, 0, -160).setDuration(150);
				Animator journalDown = ObjectAnimator.ofFloat(plusJournal, TRANSLATION_Y, -360, -240).setDuration(150);
				Animator chapterLeft = ObjectAnimator.ofFloat(plusBookChapter, TRANSLATION_X, 0, -160).setDuration(150);
				Animator chapterDown = ObjectAnimator.ofFloat(plusBookChapter, TRANSLATION_Y, -360, -240).setDuration(150);
				Animator webLeft = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_X, 0, -160).setDuration(150);
				Animator webDown = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_Y, -360, -240).setDuration(150);
				set.playTogether(translateBookDown, journalLeft, journalDown, chapterLeft, chapterDown, webLeft, webDown);
				set.addListener(new AnimationEndListener()
				{
					@Override
					void onEnd(Animator animation)
					{
						AnimatorSet set = new AnimatorSet();
						Animator chapterLeft = ObjectAnimator.ofFloat(plusBookChapter, TRANSLATION_X, -160, -260).setDuration(150);
						Animator chapterDown = ObjectAnimator.ofFloat(plusBookChapter, TRANSLATION_Y, -240, -120).setDuration(150);
						Animator webLeft = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_X, -160, -260).setDuration(150);
						Animator webDown = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_Y, -240, -120).setDuration(150);
						set.playTogether(chapterLeft, chapterDown, webLeft, webDown);
						set.addListener(new AnimationEndListener()
						{
							@Override
							void onEnd(Animator animation)
							{
								AnimatorSet set = new AnimatorSet();
								Animator webLeft = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_X, -260, -300).setDuration(150);
								Animator webDown = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_Y, -120, 30).setDuration(150);
								set.playTogether(webLeft, webDown);
								set.addListener(new AnimationEndListener()
								{
									@Override
									void onEnd(Animator animation)
									{
										new Handler().postDelayed(new Runnable()
										{
											@Override
											public void run()
											{
												bookInfo.animate().alpha(1).start();
												journalInfo.animate().alpha(1).start();
												chapterInfo.animate().alpha(1).start();
												webInfo.animate().alpha(1).start();
											}
										}, 1000);
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
		});
		animatorSet.start();
	}

	private void collapseFab(final ReferenceItem referenceItem)
	{
		bookInfo.animate().alpha(0).start();
		journalInfo.animate().alpha(0).start();
		chapterInfo.animate().alpha(0).start();
		webInfo.animate().alpha(0).start();

		ObjectAnimator.ofFloat(plusButton, "rotation", 45, 0).start();
		AnimatorSet set = new AnimatorSet();
		Animator webLeft = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_X, -300, -260).setDuration(150);
		Animator webDown = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_Y, 30, -120).setDuration(150);
		set.playTogether(webLeft, webDown);
		set.addListener(new AnimationEndListener()
		{
			@Override
			void onEnd(Animator animation)
			{
				AnimatorSet set = new AnimatorSet();
				Animator chapterLeft = ObjectAnimator.ofFloat(plusBookChapter, TRANSLATION_X, -260, -160).setDuration(150);
				Animator chapterDown = ObjectAnimator.ofFloat(plusBookChapter, TRANSLATION_Y, -120, -240).setDuration(150);
				Animator webLeft = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_X, -260, -160).setDuration(150);
				Animator webDown = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_Y, -120, -240).setDuration(150);
				set.playTogether(chapterLeft, chapterDown, webLeft, webDown);
				set.addListener(new AnimationEndListener()
				{
					@Override
					void onEnd(Animator animation)
					{
						AnimatorSet set = new AnimatorSet();
						Animator translateBookDown = ObjectAnimator.ofFloat(plusBook, TRANSLATION_Y, -300, -360).setDuration(100);
						Animator journalLeft = ObjectAnimator.ofFloat(plusJournal, TRANSLATION_X, -160, 0).setDuration(150);
						Animator journalDown = ObjectAnimator.ofFloat(plusJournal, TRANSLATION_Y, -240, -360).setDuration(150);
						Animator chapterLeft = ObjectAnimator.ofFloat(plusBookChapter, TRANSLATION_X, -160, 0).setDuration(150);
						Animator chapterDown = ObjectAnimator.ofFloat(plusBookChapter, TRANSLATION_Y, -240, -360).setDuration(150);
						Animator webLeft = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_X, -160, 0).setDuration(150);
						Animator webDown = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_Y, -240, -360).setDuration(150);
						set.playTogether(translateBookDown, journalLeft, journalDown, chapterLeft, chapterDown, webLeft, webDown);
						set.addListener(new AnimationEndListener()
						{
							@Override
							void onEnd(Animator animation)
							{
								AnimatorSet animatorSet = new AnimatorSet();
								Animator translateBookDown = ObjectAnimator.ofFloat(plusBook, TRANSLATION_Y, -360, 0).setDuration(300);
								Animator translateJournalDown = ObjectAnimator.ofFloat(plusJournal, TRANSLATION_Y, -360, 0).setDuration(300);
								Animator translateBookChapterDown = ObjectAnimator.ofFloat(plusBookChapter, TRANSLATION_Y, -360, 0).setDuration(300);
								Animator translateWebDown = ObjectAnimator.ofFloat(plusWeb, TRANSLATION_Y, -360, 0).setDuration(300);
								animatorSet.playTogether(translateBookDown, translateJournalDown, translateBookChapterDown, translateWebDown);
								animatorSet.addListener(new AnimationEndListener()
								{
									@Override
									void onEnd(Animator animation)
									{
										plusBook.setVisibility(View.GONE);
										plusJournal.setVisibility(View.GONE);
										plusBookChapter.setVisibility(View.GONE);
										plusWeb.setVisibility(View.GONE);

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

		bookInfo.setAlpha(0);
		journalInfo.setAlpha(0);
		chapterInfo.setAlpha(0);
		webInfo.setAlpha(0);

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
