package com.zeowls.gifts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class ItemDetailActivity extends AppCompatActivity {

    int id;
    TextView name, description;
    CollapsingToolbarLayout collapsingToolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        new loadingData().execute();
        collapsingToolbar.setTitle(getString(R.string.item_title));
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        description = (TextView) findViewById(R.id.description);
    }
    private class loadingData extends AsyncTask {

        JSONObject itemsJSON;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                description.setText(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("description"));
                collapsingToolbar.setTitle(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {

                Core core = new Core();
            try {
                itemsJSON = core.getShop(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
