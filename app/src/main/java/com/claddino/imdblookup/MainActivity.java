package com.claddino.imdblookup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    static String apiurl = "http://www.omdbapi.com/?";
    EditText title;
    EditText year;
    EditText plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            /*getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();*/
        }
        title = (EditText) findViewById(R.id.txtTitle);
        year = (EditText) findViewById(R.id.txtYear);
        plot = (EditText) findViewById(R.id.txtPlot);

        Button createAppointment = (Button) findViewById(R.id.btnSearch);
        createAppointment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = apiurl;
                // TODO Auto-generated method stub

                if (title.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Must Input a Title",
                            Toast.LENGTH_LONG).show();
                } else {

                    String titleQuery = "s=" + title.getText().toString();
                    String yearQuery = year.getText().toString();

                    url = url + titleQuery;
                    if (yearQuery != null) {
                        url = url + "&y=" + yearQuery;
                    }
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(MainActivity.this,
                            MovieListActivity.class);
                    // Optional parameters
                    myIntent.putExtra("UrlParameter", url);

                    MainActivity.this.startActivity(myIntent);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */


}