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
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.activities.apaactivities.APABookChapterReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APABookReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APAJournalReferenceActivity;
import com.au.easyreference.app.activities.apaactivities.APAWebPageReferenceActivity;
import com.au.easyreference.app.fragments.ContainerDialogFragment;
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

	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;
	@InjectView(R.id.references_list_view)
	protected JazzyListView referencesListView;
	@InjectView(R.id.list_title)
	protected EditText title;

	@InjectView(R.id.plus_button)
	protected ImageView plusButton;
	@InjectView(R.id.plus_book)
	protected ImageView plusBook;
	@InjectView(R.id.plus_web)
	protected ImageView plusWeb;
	@InjectView(R.id.plus_journal)
	protected ImageView plusJournal;
	@InjectView(R.id.plus_book_chapter)
	protected ImageView plusBookChapter;

	public ReferenceListAdapter adapter;
	public int type;
	public ReferenceList referenceList;

	boolean hasAnimatedOut;
	boolean expandedOptions;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.reference_list_activity);
		super.onCreate(savedInstanceState);
		ButterKnife.inject(this);

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

		plusBook.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
		plusJournal.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

		plusButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
//				TypeDialog dialog = new TypeDialog();
//				dialog.setCloseListener(closeListener);
//				dialog.show(getSupportFragmentManager(), "type");

				if(!expandedOptions)
					expandFab();
				else
					collapseFab();

				expandedOptions = !expandedOptions;
			}
		});
	}

	private void expandFab()
	{
		new ObjectAnimator().ofFloat(plusButton, "rotation", 0, 45).start();
		AnimatorSet animatorSet = new AnimatorSet();
		Animator translateBookUp = new ObjectAnimator().ofFloat(plusBook, TRANSLATION_Y, 0, -360).setDuration(300);
		Animator translateJournalUp = new ObjectAnimator().ofFloat(plusJournal, TRANSLATION_Y, 0, -360).setDuration(300);
		Animator translateBookChapterUp = new ObjectAnimator().ofFloat(plusBookChapter, TRANSLATION_Y, 0, -360).setDuration(300);
		Animator translateWebUp = new ObjectAnimator().ofFloat(plusWeb, TRANSLATION_Y, 0, -360).setDuration(300);
		animatorSet.playTogether(translateBookUp, translateJournalUp, translateBookChapterUp, translateWebUp);
		animatorSet.addListener(new AnimationEndListener()
		{
			@Override
			void onEnd(Animator animation)
			{
				AnimatorSet set = new AnimatorSet();
				Animator translateBookDown = new ObjectAnimator().ofFloat(plusBook, TRANSLATION_Y, -360, -300).setDuration(100);
				Animator journalLeft = new ObjectAnimator().ofFloat(plusJournal, TRANSLATION_X, 0, -160).setDuration(150);
				Animator journalDown = new ObjectAnimator().ofFloat(plusJournal, TRANSLATION_Y, -360, -240).setDuration(150);
				Animator chapterLeft = new ObjectAnimator().ofFloat(plusBookChapter, TRANSLATION_X, 0, -160).setDuration(150);
				Animator chapterDown = new ObjectAnimator().ofFloat(plusBookChapter, TRANSLATION_Y, -360, -240).setDuration(150);
				Animator webLeft = new ObjectAnimator().ofFloat(plusWeb, TRANSLATION_X, 0, -160).setDuration(150);
				Animator webDown = new ObjectAnimator().ofFloat(plusWeb, TRANSLATION_Y, -360, -240).setDuration(150);
				set.playTogether(translateBookDown, journalLeft, journalDown, chapterLeft, chapterDown, webLeft, webDown);
				set.addListener(new AnimationEndListener()
				{
					@Override
					void onEnd(Animator animation)
					{
						AnimatorSet set = new AnimatorSet();
						Animator chapterLeft = new ObjectAnimator().ofFloat(plusBookChapter, TRANSLATION_X, -160, -260).setDuration(150);
						Animator chapterDown = new ObjectAnimator().ofFloat(plusBookChapter, TRANSLATION_Y, -240, -120).setDuration(150);
						Animator webLeft = new ObjectAnimator().ofFloat(plusWeb, TRANSLATION_X, -160, -260).setDuration(150);
						Animator webDown = new ObjectAnimator().ofFloat(plusWeb, TRANSLATION_Y, -240, -120).setDuration(150);
						set.playTogether(chapterLeft, chapterDown, webLeft, webDown);
						set.addListener(new AnimationEndListener()
						{
							@Override
							void onEnd(Animator animation)
							{
								AnimatorSet set = new AnimatorSet();
								Animator webLeft = new ObjectAnimator().ofFloat(plusWeb, TRANSLATION_X, -260, -300).setDuration(150);
								Animator webDown = new ObjectAnimator().ofFloat(plusWeb, TRANSLATION_Y, -120, 30).setDuration(150);
								set.playTogether(webLeft, webDown);
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

	private void collapseFab()
	{
//		new ObjectAnimator().ofFloat(plusButton, "rotation", 45, 0).start();
//		AnimatorSet animatorSet = new AnimatorSet();
//		animatorSet.playTogether(createYExpandAnimator(plusBook, bookYOffset), createXExpandAnimator(plusBook, bookXOffset),
//				createYExpandAnimator(plusJournal, bookYOffset), createXExpandAnimator(plusJournal, bookXOffset),
//				createYExpandAnimator(plusBookChapter, bookYOffset), createXExpandAnimator(plusBookChapter, bookXOffset),
//				createYExpandAnimator(plusWeb, bookYOffset), createXExpandAnimator(plusWeb, bookXOffset));
//		animatorSet.start();
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

	private ContainerDialogFragment.CloseListener closeListener = new ContainerDialogFragment.CloseListener()
	{
		@Override
		public void onClose(Object result)
		{
			referenceList.referenceList.add(new ReferenceItem((Integer) result));
			adapter.notifyDataSetChanged();
			showReference(referenceList.referenceList.get(referenceList.referenceList.size() - 1));
		}
	};

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
