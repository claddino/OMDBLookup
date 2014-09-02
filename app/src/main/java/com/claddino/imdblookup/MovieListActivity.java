package com.claddino.imdblookup;

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
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.claddino.imdblookup.utilities.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MovieListActivity extends ListActivity {

    // JSON Node names
    private static final String TAG_TITLE = "Title";
    private static final String TAG_PLOT = "Plot";
    private static final String TAG_YEAR = "Year";
    private static final String TAG_POSTER = "Poster";
    private static final String TAG_IMDBID = "imdbID";
    private static final String TAG_DIRECTOR = "Director";
    private static final String TAG_TYPE = "Type";
    private static final String TAG_RATED = "Rated";
    private static final String TAG_RELEASED = "Released";
    private static final String TAG_RUNTIME = "Runtime";
    private static final String TAG_RATING = "imdbRating";
    private static final String TAG_ACTORS = "Actors";
    private static final String TAG_GENRE = "Genre";
    private static final String TAG_WRITER = "Writer";
    private static final String TAG_LANGUAGE = "Language";
    private static final String TAG_AWARDS = "Awards";
    private static final String TAG_METASCORE = "Metascore";
    private static final String TAG_COUNTRY = "Country";
    public static Context context;
    private static String url = "http://www.omdbapi.com/?";
    private final String tag = "MovieListActivity";
    // Movies JSONArray
    JSONArray moviesJsonArray = null;
    // ArrayList for ListView
    ArrayList<HashMap<String, String>> movieElements;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        movieElements = new ArrayList<HashMap<String, String>>();

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
     */
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
                        moviesJsonArray = jsonObj.getJSONArray("Search");

                        // looping through All movies
                        for (int i = 0; i < moviesJsonArray.length(); i++) {
                            JSONObject c = moviesJsonArray.getJSONObject(i);

                            String type = c.getString(TAG_TYPE);
                            String title = c.getString(TAG_TITLE);
                            String year = c.getString(TAG_YEAR);
                            String imdbID = c.getString(TAG_IMDBID);
                            String Json = sh.makeServiceCall("http://www.omdbapi.com/?" + "i=" + imdbID, ServiceHandler.GET);
                            JSONObject jsonMovieInfo = new JSONObject(Json);
                            String poster = jsonMovieInfo.getString(TAG_POSTER);
                            String rating = jsonMovieInfo.getString(TAG_RATING);
                            String actors = jsonMovieInfo.getString(TAG_ACTORS);
                            HashMap<String, String> movie = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            movie.put(TAG_TITLE, title);
                            movie.put(TAG_YEAR, year);
                            movie.put(TAG_IMDBID, imdbID);
                            movie.put(TAG_TYPE, type);
                            movie.put(TAG_POSTER, poster);
                            movie.put(TAG_RATING, rating);
                            movie.put(TAG_ACTORS, actors);

                            // adding contact to contact list
                            movieElements.add(movie);
                        }

                    } else if (!(jsonStr.contains("["))) {
                        listtype = "individualMovie";
                        String title = jsonObj.getString(TAG_TITLE);
                        String year = jsonObj.getString(TAG_YEAR);
                        String director = jsonObj.getString(TAG_DIRECTOR);
                        String plot = jsonObj.getString(TAG_PLOT);
                        String poster = jsonObj.getString(TAG_POSTER);
                        String rating = jsonObj.getString(TAG_RATING);
                        String rated = jsonObj.getString(TAG_RATED);
                        String released = jsonObj.getString(TAG_RELEASED);
                        String runtime = jsonObj.getString(TAG_RUNTIME);
                        String genre = jsonObj.getString(TAG_GENRE);

                        String writer = jsonObj.getString(TAG_WRITER);
                        String language = jsonObj.getString(TAG_LANGUAGE);
                        String country = jsonObj.getString(TAG_COUNTRY);
                        String awards = jsonObj.getString(TAG_AWARDS);
                        String metascore = jsonObj.getString(TAG_METASCORE);


                        HashMap<String, String> individualMovie = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        individualMovie.put(TAG_TITLE, title);
                        individualMovie.put(TAG_YEAR, year);
                        individualMovie.put(TAG_DIRECTOR, director);
                        individualMovie.put(TAG_PLOT, plot);
                        individualMovie.put(TAG_POSTER, poster);
                        individualMovie.put(TAG_RATING, rating);
                        individualMovie.put(TAG_RATED, rated);

                        individualMovie.put(TAG_RUNTIME, runtime);
                        individualMovie.put(TAG_RELEASED, released);
                        individualMovie.put(TAG_GENRE, genre);
                        individualMovie.put(TAG_WRITER, writer);
                        individualMovie.put(TAG_LANGUAGE, language);
                        individualMovie.put(TAG_COUNTRY, country);
                        individualMovie.put(TAG_AWARDS, awards);
                        individualMovie.put(TAG_METASCORE, metascore);

                        // adding individualMovie to contact list
                        movieElements.add(individualMovie);
                    }
                } catch (JSONException e) {
                    Log.e(tag, e.toString());
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
                Collections.sort(movieElements, new Comparator<HashMap<String, String>>() {

                    @Override
                    public int compare(HashMap<String, String> lhs,
                                       HashMap<String, String> rhs) {
                        // Do your comparison logic here and retrn accordingly.
                        return lhs.get("Year").compareTo(rhs.get("Year"));
                    }
                });
                ListAdapter adapter = new SimpleAdapter(MovieListActivity.this,
                        movieElements, R.layout.movie_list, new String[]{
                        TAG_TITLE, TAG_YEAR, TAG_TYPE, TAG_IMDBID, TAG_POSTER, TAG_RATING, TAG_ACTORS,





                },
                        new int[]{R.id.title, R.id.year, R.id.type,
                                R.id.imdbid, R.id.poster, R.id.rating, R.id.actors}
                );

                setListAdapter(adapter);

                ((SimpleAdapter) adapter).setViewBinder(new MyBinder());
            } else {
//language, country , awards
                ExpandableListAdapter expListAdapter;
                ExpandableListView expListView;
                expListView = (ExpandableListView) MovieListActivity.findViewById(R.id.lvExp);

                HashMap<String, List<String>> listDataChild;
                List<String> listDataHeader =  new ArrayList<String>();;
                List<String> top250 = new ArrayList<String>();
                top250.add("The Shawshank Redemption");

                listDataHeader.add(TAG_COUNTRY);
                listDataChild = new HashMap<String, List<String>>();
                listDataChild.put(listDataHeader.get(0), top250);

                ListAdapter adapter;
                adapter = new SimpleAdapter(MovieListActivity.this,
                        movieElements, R.layout.individual_movie_detail,
                        new String[]{TAG_TITLE, TAG_YEAR, TAG_DIRECTOR,
                                TAG_PLOT, TAG_RATING, TAG_POSTER, TAG_RATED,
                                TAG_RUNTIME, TAG_RELEASED,TAG_GENRE,TAG_WRITER,
                                TAG_METASCORE,}, new int[]{
                        R.id.title, R.id.year, R.id.director, R.id.plot, R.id.imdbRating, R.id.poster
                        , R.id.rated, R.id.runtime, R.id.released , R.id.genre, R.id.writer  , R.id.metascore


                }
                );

                expListAdapter = new ExpandableListAdapter(MovieListActivity.this,listDataHeader,listDataChild);

                // setting list adapter


                ((SimpleAdapter) adapter).setViewBinder(new MyBinder());

                setListAdapter(adapter);

                expListView.setAdapter(expListAdapter);

            }

        }
//TODO fix spaces is titlee bug
    }

}
