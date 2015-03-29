package com.au.easyreference.app.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.InjectView;
import butterknife.Optional;
import com.au.easyreference.app.R;
import com.au.easyreference.app.fragments.AuthorDialogFragment;
import com.au.easyreference.app.fragments.ContainerDialogFragment;
import com.au.easyreference.app.fragments.SearchDialog;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;
import com.au.easyreference.app.utils.ERApplication;
import com.au.easyreference.app.utils.Result;

/**
 * @author Marcus Hooper
 */
public class BaseAPAReferenceActivity extends BaseActivity
{
	public static final String KEY_LIST_ID = "key_list_id";
	public static final String KEY_ID = "key_id";

	@InjectView(R.id.author)
	public EditText author;
	@InjectView(R.id.author_button)
	public Button authorButton;
	@InjectView(R.id.year)
	public EditText year;
	@InjectView(R.id.title)
	public EditText title;
	@Optional
	@InjectView(R.id.subtitle)
	public EditText subtitle;

	@InjectView(R.id.toolbar)
	protected Toolbar toolbar;

	public ReferenceList referenceList;
	public ReferenceItem currentReference;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back_white));
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
				setUpView(args.getString(KEY_ID));
		}

		authorButton.setOnClickListener(new AuthorClickListener());
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
			setUpView(currentReference.id);
		}
	};

	public void setUpView(String id)
	{
		currentReference = referenceList.getReferenceForId(id);

		if(currentReference != null)
		{
			if(currentReference.author != null && currentReference.author.length() > 0)
				author.setText(currentReference.author);
			if(currentReference.year != null && currentReference.year.length() > 0)
				year.setText(currentReference.year);
			if(currentReference.title != null && currentReference.title.length() > 0)
				title.setText(currentReference.title);
			if(subtitle != null && currentReference.subtitle != null && currentReference.subtitle.length() > 0)
				subtitle.setText(currentReference.subtitle);
		}
	}

	public void save()
	{
		currentReference.author = author.getText().toString();
		currentReference.year = year.getText().toString();
		currentReference.title = title.getText().toString();
		currentReference.subtitle = subtitle != null ? subtitle.getText().toString() : "";
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}

	public void addAuthor(String authorString)
	{
		if(author.getText().length() > 0)
			author.append(", ");

		author.append(getFormattedAuthorString(authorString));
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

		char[] characters = originalString.toCharArray();
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
			ContainerDialogFragment container = new ContainerDialogFragment();
			Bundle args = new Bundle();
			args.putString(ContainerDialogFragment.TYPE_KEY, AuthorDialogFragment.class.getName());
			container.setArguments(args);

			container.setCloseListener(new ContainerDialogFragment.CloseListener()
			{
				@Override
				public void onClose(Object result)
				{
					addAuthor((String) result);
				}
			});

			container.show(getFragmentManager(), "Container");
		}
	}
}
