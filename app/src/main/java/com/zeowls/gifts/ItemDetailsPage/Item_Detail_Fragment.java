package com.zeowls.gifts.ItemDetailsPage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nezar Saleh on 3/24/2016.
 */
public class Item_Detail_Fragment extends Fragment {


    int id = 0;
    CollapsingToolbarLayout collapsingToolbar;
    TextView name, description, price, shopName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new loadingData().execute();
        return inflater.inflate(R.layout.item_detail_in_fragment, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle(getString(R.string.item_title));

       description = (TextView) view.findViewById(R.id.Item_Description);
        price = (TextView) view.findViewById(R.id.description2);
        shopName = (TextView) view.findViewById(R.id.item_Detail_Shop_title);

    }


    public void setId(int id) {
        this.id = id;
    }


    private class loadingData extends AsyncTask {

        JSONObject itemsJSON;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                description.setText(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("description"));
                price.setText(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("price"));
                shopName.setText(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("shop_name"));
                collapsingToolbar.setTitle(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {

            Core core = new Core(getContext());
            try {
                itemsJSON = core.getItem(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }







}
