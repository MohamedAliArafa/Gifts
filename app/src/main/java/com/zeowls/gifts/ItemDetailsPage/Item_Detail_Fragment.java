package com.zeowls.gifts.ItemDetailsPage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.ImageSLider2.SlidingImage_Adapter;
import com.zeowls.gifts.LoginPage.LoginActivity;
import com.zeowls.gifts.R;
import com.zeowls.gifts.ShopDetailsPage.Shop_Detail_Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Nezar Saleh on 3/24/2016.
 */
public class Item_Detail_Fragment extends Fragment {


    int id = 0;
    CollapsingToolbarLayout collapsingToolbar;
    TextView name, description, price, shopName;
    Button visitShop, addToCart;
    ImageView Item_Pic;
    private PagerAdapter mPagerAdapter;
    Shop_Detail_Fragment Detail_Fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    // private static final int NUM_PAGES = 5;
//    protected FragmentActivity myContext;

    //    ScreenSlidePageFragment fr;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.android, R.drawable.android1};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();


    int userId = 0;

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
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle(getString(R.string.item_title));

        description = (TextView) view.findViewById(R.id.Item_Description);
        price = (TextView) view.findViewById(R.id.description2);
        shopName = (TextView) view.findViewById(R.id.item_Detail_Shop_title);
        visitShop = (Button) view.findViewById(R.id.Item_Detail_Shop_Visit);
        addToCart = (Button) view.findViewById(R.id.addToCartBTN);
        Item_Pic = (ImageView) view.findViewById(R.id.image);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new SlidingImage_Adapter(getContext(), ImagesArray);
        mPager.setAdapter(mPagerAdapter);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != 0 && id != 0) {
                    new addToCart().execute();
                }else {
                    Intent in = new Intent(getActivity(), LoginActivity.class);
                    startActivity(in);
                }
            }
        });

        //TODO
        init();
    }

    @Override
    public void onResume() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Credentials", getActivity().MODE_PRIVATE);
        userId= prefs.getInt("id", 0);
        super.onResume();
    }

    private void init() {

        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);
//        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
//        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        // indicator.setRadius(5 * density);

        NUM_PAGES = IMAGES.length;
        mPagerAdapter.notifyDataSetChanged();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
//        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                currentPage = position;
//
//            }
//
//            @Override
//            public void onPageScrolled(int pos, float arg1, int arg2) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int pos) {
//
//            }
//        });

    }


    public void setId(int id) {
        this.id = id;
    }


    private class loadingData extends AsyncTask {

        JSONObject itemsJSON;
        Picasso picasso;

        @Override
        protected void onPreExecute() {
            Context context = getContext();
            picasso = Picasso.with(context);
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                description.setText(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("description"));
                price.setText("$" + itemsJSON.getJSONArray("Items").getJSONObject(0).getString("price"));
                shopName.setText(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("shop_name"));
                collapsingToolbar.setTitle(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("name"));
                picasso.load("http://bubble.zeowls.com/uploads/" + itemsJSON.getJSONArray("Items").getJSONObject(0).getString("image")).fit().centerCrop().into(Item_Pic);

                visitShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        if (Detail_Fragment != null) {
                            fragmentTransaction.remove(Detail_Fragment);
                        }
                        Detail_Fragment = new Shop_Detail_Fragment();
                        try {
                            Detail_Fragment.setId(itemsJSON.getJSONArray("Items").getJSONObject(0).getInt("shop_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fragmentTransaction.replace(R.id.fragment, Detail_Fragment);
                        fragmentTransaction.commit();
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

    private class addToCart extends AsyncTask {

        JSONObject itemsJSON;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Object o) {
            if ((boolean) o){
                Toast.makeText(getActivity(), "item added to cart", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "some thing went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Boolean state = false;
            Core core = new Core(getContext());
            try {
                itemsJSON = core.addToCart(userId,id);
                state = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return state;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
