package com.zeowls.gifts.ItemDetailsPage;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;
import com.zeowls.gifts.ShopDetailsPage.Shop_Detail_Activity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nezar Saleh on 3/24/2016.
 */
public class Item_Detail_Fragment extends Fragment {


    int id = 0;
    CollapsingToolbarLayout collapsingToolbar;
    TextView name, description, price, shopName;
    Button visitShop;
    ImageView Item_Pic;


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
        visitShop = (Button) view.findViewById(R.id.Item_Detail_Shop_Visit);
        Item_Pic= (ImageView) view.findViewById(R.id.image);


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
                Picasso.with(getContext()).load("http://bubble-zeowls.herokuapp.com/uploads/" + itemsJSON.getJSONArray("Items").getJSONObject(0).getString("image")).into(Item_Pic);


                visitShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Shop_Detail_Activity.class);
                        try {
                            intent.putExtra("id",itemsJSON.getJSONArray("Items").getJSONObject(0).getInt("shop_id") );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getContext().startActivity(intent);
                    }
                });


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
