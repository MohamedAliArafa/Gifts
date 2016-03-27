package com.zeowls.gifts.ShopDetailsPage;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nezar Saleh on 3/24/2016.
 */
public class Shop_Detail_Fragment extends Fragment {


    int id = 0;
    CollapsingToolbarLayout collapsingToolbar;
    TextView Shop_Name, Shop_Slogan;
    ImageView Shop_Pic,ShopHeader_Pic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new loadingData().execute();
        return inflater.inflate(R.layout.shop_details_in_fragment1, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);


        Shop_Name  = (TextView) view.findViewById(R.id.item_Detail_Shop_title);
        Shop_Slogan  = (TextView) view.findViewById(R.id.item_Detail_Shop_Slogan);
        Shop_Pic = (ImageView) view.findViewById(R.id.item_Detail_SHop_Image);
        ShopHeader_Pic = (ImageView) view.findViewById(R.id.image);


        collapsingToolbar.setTitle(getString(R.string.item_title));



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
                Shop_Slogan.setText(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("description"));
                collapsingToolbar.setTitle(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("name"));
                Shop_Name.setText(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("name"));
                Picasso.with(getContext()).load("http://bubble-zeowls.herokuapp.com/uploads/" + itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("profile_pic")).into(ShopHeader_Pic);
                Picasso.with(getContext()).load("http://bubble-zeowls.herokuapp.com/uploads/" + itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("profile_pic")).into(Shop_Pic);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {

            Core core = new Core(getContext());
            try {
                itemsJSON = core.getShop(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }







}
