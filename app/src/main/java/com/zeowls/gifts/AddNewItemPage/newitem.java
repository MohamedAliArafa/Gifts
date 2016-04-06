package com.zeowls.gifts.AddNewItemPage;

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

import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;

import org.json.JSONException;

import java.util.ArrayList;

public class newitem extends AppCompatActivity {


    ListView listItems;
    ArrayList<String> items = new ArrayList<>();
    EditText nameEdit, quantity;
    ArrayAdapter<String> adapter;
    Button submit;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newitem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        listItems = (ListView) findViewById(R.id.itemsList);
        quantity = (EditText) findViewById(R.id.QuEdit);
        submit = (Button) findViewById(R.id.submitBTN);
        setSupportActionBar(toolbar);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEdit.getText() != null && quantity.getText() != null) {
                    //ToDo
                    new postData().execute();
                } else {
                    Toast.makeText(newitem.this, "please enter a name!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private class postData extends AsyncTask<Void, Void, Object> {

        String name;
        int Quantity;

        @Override
        protected Object doInBackground(Void... params) {

            try {
                new Core(newitem.this).newItem(name, Quantity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            name = nameEdit.getText().toString();
            Quantity = Integer.parseInt(quantity.getText().toString());
        }

        @Override
        protected void onPostExecute(Object o) {
            adapter.notifyDataSetChanged();
        }

    }


}
