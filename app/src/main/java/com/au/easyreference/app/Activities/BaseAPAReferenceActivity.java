package com.au.easyreference.app.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindColor;
import com.au.easyreference.app.R;
import com.au.easyreference.app.fragments.AuthorDialogFragment;
import com.au.easyreference.app.fragments.ContainerDialogFragment;
import com.au.easyreference.app.fragments.MoreInfoDialog;
import com.au.easyreference.app.fragments.SearchDialog;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;
import com.au.easyreference.app.utils.ERApplication;
import com.au.easyreference.app.utils.Result;
import com.easygoingapps.ThePoliceProcessor;
import com.easygoingapps.annotations.Observe;
import com.easygoingapps.utils.State;

/**
 * @author Marcus Hooper
 */
public class BaseAPAReferenceActivity extends BaseActivity
{
	public static final String KEY_LIST_ID = "key_list_id";
	public static final String KEY_ID = "key_id";

	@Observe(R.id.author)
	public State<String> author;
	@Observe(R.id.year)
	public State<String> year;
	@Observe(R.id.title)
	public State<String> title;

	@Bind(R.id.author_label)
	public TextView authorLabel;
	@Bind(R.id.author_button)
	public Button authorButton;
	@Bind(R.id.year_label)
	public TextView yearLabel;
	@Bind(R.id.title_label)
	public TextView titleLabel;

	@Observe(R.id.subtitle)
	public State<String> subtitle;

	@Nullable
	@Bind(R.id.subtitle_label)
	public TextView subtitleLabel;

	@Bind(R.id.toolbar)
	protected Toolbar toolbar;

	public @BindColor(R.color.light_gray) int lightGray;

	public ReferenceList referenceList;
	public ReferenceItem currentReference;

	public void setUpReferenceActivity(int type)
	{
		toolbar.setNavigationIcon(R.drawable.arrow_back_white);
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});

		toolbar.setBackgroundColor(getResources().getColor(R.color.easy_reference_red));
		setSupportActionBar(toolbar);

		Bundle args = getIntent().getExtras();

		if(args != null)
		{
			for(ReferenceList list : ERApplication.referenceLists)
				if(list.id.equalsIgnoreCase(args.getString(KEY_LIST_ID)))
					referenceList = list;

			if(args.containsKey(KEY_ID))
				currentReference = referenceList.getReferenceForId(args.getString(KEY_ID));
		}

		if(currentReference == null)
		{
			currentReference = new ReferenceItem(type);
			referenceList.referenceList.add(currentReference);
		}

		title = currentReference.title;
		author = currentReference.author;
		year = currentReference.year;
		subtitle = currentReference.subtitle;
		BaseAPAReferenceActivityBinding.watch(this);

		authorButton.setOnClickListener(new AuthorClickListener());

		authorLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		authorLabel.setOnClickListener(new LabelClickListener());

		yearLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		yearLabel.setOnClickListener(new LabelClickListener());

		titleLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
		titleLabel.setOnClickListener(new LabelClickListener());

		if(subtitleLabel != null)
		{
			subtitleLabel.getCompoundDrawables()[2].setColorFilter(lightGray, PorterDuff.Mode.SRC_IN);
			subtitleLabel.setOnClickListener(new LabelClickListener());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, 0, 0, getString(R.string.search)).setIcon(R.drawable.actionbar_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;

			case 0:
				Bundle args = new Bundle();
				args.putInt(SearchDialog.TYPE, currentReference.type);

				SearchDialog dialog = new SearchDialog();
				dialog.setArguments(args);
				dialog.show(getSupportFragmentManager(), "search");
				dialog.setCloseListener(closeListener);
		}

		return super.onOptionsItemSelected(item);
	}

	private ContainerDialogFragment.CloseListener closeListener = new ContainerDialogFragment.CloseListener()
	{
		@Override
		public void onClose(Object result)
		{
			int index = referenceList.referenceList.indexOf(referenceList.getReferenceForId(currentReference.id));
			referenceList.referenceList.remove(referenceList.getReferenceForId(currentReference.id));

			ReferenceItem newReference = new ReferenceItem((Result) result);
			newReference.id = currentReference.id;

			referenceList.referenceList.add(index, newReference);
			currentReference = referenceList.getReferenceForId(currentReference.id);
		}
	};

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}

	public void addAuthor(String authorString)
	{
		StringBuilder currentText = new StringBuilder(author.getValue());
		if(author.getValue().length() > 0)
		{
			if(currentText.toString().contains(" & "))
				currentText = new StringBuilder(currentText.toString().replace(" & ", ""));

			currentText.append(", & ");
		}

		currentText.append(getFormattedAuthorString(authorString));
		author.setValue(currentText.toString());
	}

	private String getFormattedAuthorString(String originalString)
	{
		String formattedString = "";

		String[] author = originalString.split(",");
		formattedString += firstLetterToUppercase(author[0]) + ", ";
		formattedString += author[1].toUpperCase();

		return formattedString;
	}

	private String firstLetterToUppercase(String originalString)
	{
		String formattedString = "";

		char[] characters = originalString.toLowerCase().toCharArray();
		String firstLetter = String.valueOf(characters[0]);
		firstLetter = firstLetter.toUpperCase();

		formattedString += firstLetter;
		for(int i = 1; i < characters.length; i++)
			formattedString += characters[i];

		return formattedString;
	}

	public class AuthorClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			AuthorDialogFragment dialogFragment = new AuthorDialogFragment();
			dialogFragment.setCloseListener(new ContainerDialogFragment.CloseListener()
			{
				@Override
				public void onClose(Object result)
				{
					addAuthor((String) result);
				}
			});
			dialogFragment.show(getFragmentManager(), "Author");
		}
	}

	public class LabelClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			int message = -1;
			int image = -1;

			switch(view.getId())
			{
				case R.id.author_label:
					message = R.string.author_info_message;
					image = R.drawable.add_author_image;
					break;

				case R.id.year_label:
					message = currentReference.type == ReferenceItem.WEB_PAGE ? R.string.add_web_year_message : R.string.add_year_message;
					image = currentReference.type == ReferenceItem.WEB_PAGE ? R.drawable.add_web_year_image : R.drawable.add_year_image;
					break;

				case R.id.title_label:
					message = R.string.add_title_message;
					image = R.drawable.add_title_image;
					break;

				case R.id.subtitle_label:
					message = R.string.add_subtitle_message;
					image = R.drawable.add_subtitle_image;
					break;

				case R.id.location_label:
					message = R.string.add_location_hint;
					image = R.drawable.add_location_image;
					break;

				case R.id.publisher_label:
					message = R.string.add_publisher_message;
					image = R.drawable.add_publisher_image;
					break;

				case R.id.book_title_label:
					message = R.string.add_chapter_title_message;
					image = R.drawable.add_chapter_title_image;
					break;

				case R.id.book_subtitle_label:
					message = R.string.add_chapter_subtitle_message;
					image = R.drawable.add_chapter_subtitle_image;
					break;

				case R.id.pages_of_chapter_label:
					message = R.string.add_pages_of_chapter_message;
					image = R.drawable.add_pages_of_chapter_image;
					break;

				case R.id.editors_label:
					message = R.string.add_editors_message;
					image = R.drawable.add_editors_image;
					break;

				case R.id.journal_title_label:
					message = R.string.add_journal_title_message;
					image = R.drawable.add_journal_title_image;
					break;

				case R.id.volume_number_label:
					message = R.string.add_volume_number_message;
					image = R.drawable.add_volume_number_image;
					break;

				case R.id.issue_label:
					message = R.string.add_issue_message;
					image = R.drawable.add_issue_image;
					break;

				case R.id.page_no_label:
					message = R.string.add_pages_message;
					image = R.drawable.add_pages_image;
					break;

				case R.id.doi_label:
					message = R.string.add_doi_message;
					image = R.drawable.add_doi_image;
					break;

				case R.id.url_label:
					message = R.string.add_url_message;
					image = R.drawable.add_url_image;
					break;
			}

			Bundle args = new Bundle();
			args.putString(MoreInfoDialog.MESSAGE, getString(message));
			args.putInt(MoreInfoDialog.IMAGE, image);

			MoreInfoDialog dialog = new MoreInfoDialog();
			dialog.setArguments(args);
			dialog.show(getFragmentManager(), null);
		}
	}
}
