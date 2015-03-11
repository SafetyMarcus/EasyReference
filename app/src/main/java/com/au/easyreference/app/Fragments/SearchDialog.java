package com.au.easyreference.app.fragments;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.au.easyreference.app.R;
import com.au.easyreference.app.ui.BounceInAnimation;
import com.au.easyreference.app.utils.ERApplication;
import com.au.easyreference.app.utils.Result;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.au.easyreference.app.utils.ERApplication.BUS;
import static com.au.easyreference.app.utils.ERApplication.hasSeenSearchInfo;

/**
 * @author Marcus Hooper
 */
public class SearchDialog extends Fragment
{
	public static final String TYPE = "type";

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
	TextView searchButton;

	@InjectView(R.id.progress_bar)
	ProgressBar progressBar;
	@InjectView(R.id.results_list)
	ListView resultsList;

	@InjectView(R.id.first_time_info)
	View firstTimeInfo;

	private ArrayList<Result> results;
	private ResultsAdapter resultsAdapter;

	private int visibleView = DOI;
	private int type;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View layout = getActivity().getLayoutInflater().inflate(R.layout.search_dialog, container, false);
		ButterKnife.inject(this, layout);

		((ContainerDialogFragment) getParentFragment()).toolbar.setTitle(getString(R.string.search));
		results = new ArrayList<>();

		type = getArguments().getInt(TYPE);

		doiTab.setOnClickListener(new TabClickListener());
		issnTab.setOnClickListener(new TabClickListener());
		isbnTab.setOnClickListener(new TabClickListener());

		resultsAdapter = new ResultsAdapter(getActivity(), R.layout.list_item, getActivity().getLayoutInflater(), results);
		resultsList.setAdapter(resultsAdapter);

		resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				((ContainerDialogFragment) getParentFragment()).closeListener.onClose(resultsAdapter.getItem(i));
				((ContainerDialogFragment) getParentFragment()).onBackPressed();
			}
		});

		searchButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				resultsList.setVisibility(View.GONE);
				searchButton.setEnabled(false);
				progressBar.setVisibility(View.VISIBLE);
				new QueryDatabaseAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});

		if(!hasSeenSearchInfo)
			performFirstTimeAnimation();

		return layout;
	}

	private void performFirstTimeAnimation()
	{
		ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(firstTimeInfo, "translationY", -1000);
		hideAnimator.setDuration(0);
		hideAnimator.start();

		BounceInAnimation bounce = new BounceInAnimation(firstTimeInfo);
		bounce.setFinishListener(new BounceInAnimation.FinishListener()
		{
			@Override
			public void onFinish()
			{
				hasSeenSearchInfo = true;
				((ERApplication) getActivity().getApplication()).saveSettings();
			}
		});

		bounce.startAnimation();
	}

	@Override
	public void onPause()
	{
		BUS.unregister(this);
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		BUS.register(this);
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
		resultsList.setVisibility(View.VISIBLE);
		results.clear();

		JSONArray resultsArray = resultsObject.optJSONArray("records");
		for(int i = 0; i < resultsArray.length(); i++)
			results.add(new Result(resultsArray.optJSONObject(i), type));

		resultsAdapter.notifyDataSetChanged();
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
				searchButton.setEnabled(true);
				if(response != null && response.length() > 0)
					setUpList(new JSONObject(response));
				else
					Toast.makeText(getActivity(), R.string.no_results, Toast.LENGTH_SHORT).show();

				progressBar.setVisibility(View.GONE);
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
		}
	}

	private class ResultsAdapter extends ArrayAdapter<Result>
	{
		private LayoutInflater inflater;

		public ResultsAdapter(Context context, int resource, LayoutInflater inflater, ArrayList<Result> objects)
		{
			super(context, resource, objects);
			this.inflater = inflater;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			Result result = getItem(position);

			if(convertView == null)
				convertView = inflater.inflate(R.layout.result_item, null);

			TextView title = (TextView) convertView.findViewById(R.id.result_title);
			TextView publisher = (TextView) convertView.findViewById(R.id.result_publisher);
			TextView publicationName = (TextView) convertView.findViewById(R.id.result_publication_name);
			TextView publicationDate = (TextView) convertView.findViewById(R.id.result_publication_date);
			TextView volume = (TextView) convertView.findViewById(R.id.result_publication_volume);

			title.setText(result.title);
			publisher.setText(getString(R.string.result_publisher) + ' ' + result.publisher);
			publicationName.setText(getString(R.string.result_publication) + ' ' + result.publicationName);
			publicationDate.setText(getString(R.string.result_publication_date) + ' ' + result.publicationDate);
			volume.setText(getString(R.string.result_volume) + ' ' + result.volume);

			return convertView;
		}
	}
}
