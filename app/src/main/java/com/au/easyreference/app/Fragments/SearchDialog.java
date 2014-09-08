package com.au.easyreference.app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Marcus Hooper
 */
public class SearchDialog extends DialogFragment
{
	private static final String URL = "http://api.springer.com/metadata/json?q=";
	private static final int DOI = 0;
	private static final int ISSN = 1;
	private static final int ISBN = 2;

	@InjectView(R.id.doi_tab)
	TextView doiTab;
	@InjectView(R.id.doi_highlight)
	ImageView doiHighlight;

	@InjectView(R.id.isbn_tab)
	TextView isbnTab;
	@InjectView(R.id.isbn_highlight)
	ImageView isbnHighlight;

	@InjectView(R.id.issn_tab)
	TextView issnTab;
	@InjectView(R.id.issn_highlight)
	ImageView issnHighlight;

	@InjectView(R.id.search)
	EditText search;
	@InjectView(R.id.search_button)
	Button searchButton;

	@InjectView(R.id.results_list)
	ListView resultsList;

	private ArrayList<String> results;
	private ArrayAdapter<String> resultsAdapter;

	private int visibleView = DOI;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(getString(R.string.search));
		View layout = getActivity().getLayoutInflater().inflate(R.layout.search_dialog, null);
		ButterKnife.inject(this, layout);
		builder.setView(layout);

		results = new ArrayList<String>();

		doiTab.setOnClickListener(new TabClickListener());
		issnTab.setOnClickListener(new TabClickListener());
		isbnTab.setOnClickListener(new TabClickListener());

		searchButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				new QueryDatabaseAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});

		return builder.create();
	}

	private class TabClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			resetViews();

			switch(view.getId())
			{
				case R.id.doi_tab:
					doiTab.setTextColor(getResources().getColor(R.color.bright_light_blue));
					doiHighlight.setVisibility(View.VISIBLE);
					visibleView = DOI;
					break;
				case R.id.issn_tab:
					issnTab.setTextColor(getResources().getColor(R.color.bright_light_blue));
					issnHighlight.setVisibility(View.VISIBLE);
					visibleView = ISSN;
					break;
				case R.id.isbn_tab:
					isbnTab.setTextColor(getResources().getColor(R.color.bright_light_blue));
					isbnHighlight.setVisibility(View.VISIBLE);
					visibleView = ISBN;
					break;
			}
		}

		public void resetViews()
		{
			doiTab.setTextColor(getResources().getColor(android.R.color.black));
			isbnTab.setTextColor(getResources().getColor(android.R.color.black));
			issnTab.setTextColor(getResources().getColor(android.R.color.black));

			doiHighlight.setVisibility(View.GONE);
			isbnHighlight.setVisibility(View.GONE);
			issnHighlight.setVisibility(View.GONE);
		}
	}

	private void setUpList(JSONObject resultsObject)
	{
		results.clear();

		JSONArray resultsArray = resultsObject.optJSONArray("records");
		for(int i = 0; i < resultsArray.length(); i++)
			results.add(resultsArray.optJSONObject(i).optString("publicationName"));

		resultsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.reference_item, results);
		resultsList.setAdapter(resultsAdapter);
	}

	private class QueryDatabaseAsync extends AsyncTask
	{
		String response;

		@Override
		protected Object doInBackground(Object[] objects)
		{
			StringBuilder query = new StringBuilder(URL);

			switch(visibleView)
			{
				case DOI:
					query.append("doi:");
					break;
				case ISSN:
					query.append("issn:");
					break;
				case ISBN:
					query.append("isbn:");
					break;
			}

			query.append(search.getText());
			query.append("&api_key=").append(getString(R.string.SpringerMetaDataKey));

			HttpRequest httpRequest = HttpRequest.get(query);
			httpRequest.acceptJson();

			response = httpRequest.body();

			return false;
		}

		@Override
		protected void onPostExecute(Object o)
		{
			try
			{
				setUpList(new JSONObject(response));
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
		}
	}
}
