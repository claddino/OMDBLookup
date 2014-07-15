package com.claddino.imdblookup;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.claddino.imdblookup.utilities.ServiceHandler;

public class MovieListActivity extends ListActivity {

	private ProgressDialog pDialog;

	// URL to get contacts JSON

	// JSON Node names
	private static final String TAG_TITLE = "Title";
	private static final String TAG_PLOT = "Plot";
	private static final String TAG_YEAR = "Year";
	private static final String TAG_POSTER = "Poster";
	private static final String TAG_IMDBID = "imdbID";
	private static final String TAG_DIRECTOR = "Director";
	private static final String TAG_TYPE = "Type";
	private static final String TAG_RATING = "imdbRating";

	// contacts JSONArray
	JSONArray movies = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> movieList;
	private static String url = "http://www.omdbapi.com/?";

	public static Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_list);

		movieList = new ArrayList<HashMap<String, String>>();

		ListView lv = getListView();
		Intent intent = getIntent();
		String movieQuery = intent.getStringExtra("UrlParameter");
		String imdbID = intent.getStringExtra(TAG_IMDBID);

		// if imdbID is null it is not an individual movie
		if (imdbID != null) {
			url = "http://www.omdbapi.com/?";
			url = url + "i=" + imdbID;
			lv.setEnabled(false);
		} else {
			url = movieQuery;
		}

		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String imdbID = ((TextView) view.findViewById(R.id.imdbid))
						.getText().toString();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						MovieListActivity.class);

				in.putExtra(TAG_IMDBID, imdbID);

				startActivity(in);

			}
		});

		// Calling async task to get json
		new GetMovies().execute();
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetMovies extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MovieListActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler sh = new ServiceHandler();
			String listtype = null;
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					/*
					 * if jsonStr contains "[" it is a JSONArray and if it
					 */

					if (jsonStr.contains("[")) {
						listtype = "movielist";
						movies = jsonObj.getJSONArray("Search");

						// looping through All Contacts
						for (int i = 0; i < movies.length(); i++) {
							JSONObject c = movies.getJSONObject(i);

							String type = c.getString(TAG_TYPE);
							String title = c.getString(TAG_TITLE);
							String year = c.getString(TAG_YEAR);
							String imdbID = c.getString(TAG_IMDBID);
							HashMap<String, String> contact = new HashMap<String, String>();

							// adding each child node to HashMap key => value
							contact.put(TAG_TITLE, title);
							contact.put(TAG_YEAR, year);
							contact.put(TAG_IMDBID, imdbID);
							contact.put(TAG_TYPE, type);

							// adding contact to contact list
							movieList.add(contact);
						}

					} else if (!(jsonStr.contains("["))) {
						listtype = "individualmovie";
						String title = jsonObj.getString(TAG_TITLE);
						String year = jsonObj.getString(TAG_YEAR);
						String director = jsonObj.getString(TAG_DIRECTOR);
						String plot = jsonObj.getString(TAG_PLOT);
						String poster = jsonObj.getString(TAG_POSTER);
						String rating = jsonObj.getString(TAG_RATING);
						HashMap<String, String> contact = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						contact.put(TAG_TITLE, title);
						contact.put(TAG_YEAR, year);
						contact.put(TAG_DIRECTOR, director);
						contact.put(TAG_PLOT, plot);
						contact.put(TAG_POSTER, poster);
						contact.put(TAG_RATING, rating);

						// adding contact to contact list
						movieList.add(contact);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return listtype;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			if (result.equals("movielist")) {

				ListAdapter adapter = new SimpleAdapter(MovieListActivity.this,
						movieList, R.layout.list_item, new String[] {
								TAG_TITLE, TAG_YEAR, TAG_TYPE, TAG_IMDBID },
						new int[] { R.id.title, R.id.year, R.id.type,
								R.id.imdbid });

				setListAdapter(adapter);

				((SimpleAdapter) adapter).setViewBinder(new MyBinder());
			} else {

				ListAdapter adapter = new SimpleAdapter(MovieListActivity.this,
						movieList, R.layout.individual_movie_detail,
						new String[] { TAG_TITLE, TAG_YEAR, TAG_DIRECTOR,
								TAG_PLOT, TAG_RATING, TAG_POSTER }, new int[] {
								R.id.title, R.id.year, R.id.director,
								R.id.plot, R.id.imdbRating, R.id.poster });

				((SimpleAdapter) adapter).setViewBinder(new MyBinder());

				setListAdapter(adapter);

			}

		}

	}

}
