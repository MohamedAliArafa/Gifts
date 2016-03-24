package com.zeowls.gifts;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class newitem extends AppCompatActivity {


    ListView listItems;
    ArrayList<String> items = new ArrayList<>();
    EditText nameEdit;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newitem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        listItems = (ListView) findViewById(R.id.itemsList);
        Button submit = (Button) findViewById(R.id.submitBTN);
        setSupportActionBar(toolbar);

        new loadingData().execute();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEdit.getText() != null) {
                    //ToDo
                    new postData().execute();
                } else {
                    Toast.makeText(newitem.this, "please enter a name!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private class postData extends AsyncTask {

        String name;

        @Override
        protected void onPreExecute() {
            name = nameEdit.getText().toString();
        }

        @Override
        protected void onPostExecute(Object o) {
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                new Core().newItem(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Object o) {
            listItems.setAdapter(adapter);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core();
                JSONObject itemsJSON = core.getAllShops();
                if (itemsJSON.getJSONArray("Shop").length() != 0){
                    for (int i = 0; i < itemsJSON.getJSONArray("Shop").length(); i++){
                        JSONArray itemsarray = itemsJSON.getJSONArray("Shop");
                        JSONObject item = itemsarray.getJSONObject(i);
                        String name = item.getString("name");
                        items.add(name);
                        //items.add(itemsJSON.getJSONArray("Shop").getJSONObject(i).getString("name"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
