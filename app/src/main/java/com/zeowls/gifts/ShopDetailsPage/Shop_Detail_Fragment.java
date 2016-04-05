package com.zeowls.gifts.ShopDetailsPage;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.ImageSLider2.SlidingImage_Adapter;
import com.zeowls.gifts.ImageSlider.ScreenSlidePageFragment;
import com.zeowls.gifts.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nezar Saleh on 3/24/2016.
 */
public class Shop_Detail_Fragment extends Fragment {


    int id = 0;
    CollapsingToolbarLayout collapsingToolbar;
    TextView Shop_Name, Shop_Slogan;
    ImageView Shop_Pic, ShopHeader_Pic;
    ViewPager viewPager;
    private PagerAdapter mPagerAdapter;
    // private static final int NUM_PAGES = 5;

    protected FragmentActivity myContext;

    ScreenSlidePageFragment fr;


    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.paris, R.drawable.paris_avatar, R.drawable.rightarrow, R.drawable.navback};
    private ArrayList<Integer> ImagesArray = new ArrayList<>();

    TextView viewAllItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new loadingData().execute();
        return inflater.inflate(R.layout.shop_details_in_fragment1, container, false);

    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        Shop_Name = (TextView) view.findViewById(R.id.item_Detail_Shop_title);
        Shop_Slogan = (TextView) view.findViewById(R.id.item_Detail_Shop_Slogan);
        Shop_Pic = (ImageView) view.findViewById(R.id.item_Detail_SHop_Image);
        ShopHeader_Pic = (ImageView) view.findViewById(R.id.image);

//        mPager = (ViewPager) view.findViewById(R.id.pager);
//        mPagerAdapter = new SlidingImage_Adapter(getContext(), ImagesArray);
//        mPager.setAdapter(mPagerAdapter);
//
//        init();


//
//        mPagerAdapter = new ScreenSlidePagerAdapter(myContext.getSupportFragmentManager());
//        viewPager.setAdapter(mPagerAdapter);
//
//        fr =  new ScreenSlidePageFragment();


        collapsingToolbar.setTitle(getString(R.string.item_title));


        viewAllItems = (TextView) view.findViewById(R.id.view_all_items);
        viewAllItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopAllItemsFragment fragment = null;
                final FragmentManager fragmentManager;
                final android.support.v4.app.FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (fragment != null) {
                    fragmentTransaction.remove(fragment);
                }

                fragment = new ShopAllItemsFragment();
                fragment.setId(id);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.commit();
            }
        });

    }


    private void init() {


        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

//
//        CirclePageIndicator indicator = (CirclePageIndicator)
//                findViewById(R.id.indicator);
//
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


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Toast.makeText(myContext, "Hi " + position, Toast.LENGTH_SHORT).show();
            fr.setName("Ali");

            if (position == 1) {
                fr.setName("Ali");
            } else if (position == 3) {
                fr.setName("Ali  3");
            }

            return new ScreenSlidePageFragment();


        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
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
                Picasso.with(getContext()).load("http://bubble.zeowls.com/uploads/" + itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("profile_pic")).resize(500, 500).into(ShopHeader_Pic);
                Picasso.with(getContext()).load("http://bubble.zeowls.com/uploads/" + itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("profile_pic")).resize(200, 200).centerInside().into(Shop_Pic);
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
