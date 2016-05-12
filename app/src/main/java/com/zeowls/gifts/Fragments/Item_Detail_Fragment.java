package com.zeowls.gifts.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.BackEndOwl.FireOwl;
import com.zeowls.gifts.views.adapters.SlidingImage_Adapter;
import com.zeowls.gifts.Activities.MainActivity;
import com.zeowls.gifts.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Nezar Saleh on 3/24/2016.
 */
public class Item_Detail_Fragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {


    int item_id = 0;
    int shop_id = 0;
    int user_id = 0;

    CollapsingToolbarLayout collapsingToolbar;
    TextView name, description, price, item_name_toolbar, shop_name;
    Button visitShop, addToCart;
    ImageView Item_Pic;
    private PagerAdapter mPagerAdapter;
    Shop_Detail_Fragment endFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.giftintro, R.drawable.giftintro};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 1000;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;

    loadingData loadingData;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_detail_in_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingData = new loadingData();
        if (loadingData.getStatus() != AsyncTask.Status.RUNNING) {
            loadingData.execute();
        }

        Bundle bundle = getArguments();
        String actionTitle = "";
        Bitmap imageBitmap = null;
        String transText = "";
        String transitionName = "";

        if (bundle != null) {
            transitionName = bundle.getString("TRANS_NAME");
            actionTitle = bundle.getString("ACTION");
            imageBitmap = bundle.getParcelable("IMAGE");
            transText = bundle.getString("TRANS_TEXT");
        }

//        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle(getString(R.string.item_title));

        name = (TextView) view.findViewById(R.id.item_Detail_Shop_title1);
        description = (TextView) view.findViewById(R.id.Item_Description);
        price = (TextView) view.findViewById(R.id.description2);
        item_name_toolbar = (TextView) view.findViewById(R.id.item_Detail_toolbar_title);
        shop_name = (TextView) view.findViewById(R.id.item_detail_shop_name);
        visitShop = (Button) view.findViewById(R.id.item_detail_shop_visit);
        addToCart = (Button) view.findViewById(R.id.addToCartBTN);
        Item_Pic = (ImageView) view.findViewById(R.id.item_Detail_Image);

        mPager = (ViewPager) view.findViewById(R.id.pager);
//        mPagerAdapter = new SlidingImage_Adapter(getContext(), ImagesArray);
        mPager.setAdapter(mPagerAdapter);

        name.setText(actionTitle);
        Item_Pic.setImageBitmap(imageBitmap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            name.setTransitionName(transitionName);
            Item_Pic.setTransitionName(transText);
        }

        mTitleContainer = (LinearLayout) view.findViewById(R.id.main_linearlayout_title);
        AppBarLayout mAppBarLayout = (AppBarLayout) view.findViewById(R.id.main_appbar);
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(item_name_toolbar, 0, View.INVISIBLE);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id != 0) {
                    if (item_id != 0 && shop_id != 0) {
                        new addToCart().execute();
                    } else {
                        Log.d("Id Empty", "Item And Shop Ids are Empty");
                    }
                } else {

                    DialogFragment newFragment = new LoginFragment();
                    newFragment.show(getFragmentManager(), "missiles");

                }
            }
        });

        //TODO
        init();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(item_name_toolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(item_name_toolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onResume() {
//        if (((MainActivity) getActivity()).toolbar != null && ((MainActivity) getActivity()).toolbar.getVisibility() != View.GONE) {
//            ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
//        }
        SharedPreferences prefs = getActivity().getSharedPreferences("Credentials", getActivity().MODE_PRIVATE);
        user_id = prefs.getInt("id", 0);
        super.onResume();
    }

    private void init() {

        Collections.addAll(ImagesArray, IMAGES);
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
        this.item_id = id;
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
                JSONObject item = itemsJSON.getJSONArray("Items").getJSONObject(0);

                final String item_name = item.getString("name");
                final String item_price = "$" + item.getString("price");
                final String item_image = item.getString("image");
                final String item_desc = item.getString("description");
                final String shop_name_txt = item.getString("shop_name");
                shop_id = item.getInt("shop_id");

                description.setText(item_desc);
                price.setText(item_price);
                item_name_toolbar.setText(item_name);
                shop_name.setText(shop_name_txt);
                collapsingToolbar.setTitle(item_name);
                name.setText(item_name);

                if (item_image.equals("")) {
                    Item_Pic.setImageResource(R.drawable.giftintro);
                } else {
                    picasso.load("http://bubble.zeowls.com/uploads/" + item_image).fit().centerCrop().into(Item_Pic);
                }
                endFragment = new Shop_Detail_Fragment();

                visitShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fragmentTag = "ShopFragment";
                        String backStateName = this.getClass().getName();
                        FragmentManager manager = getFragmentManager();

                        if (manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
                            FragmentTransaction ft = manager.beginTransaction();
                            endFragment.setId(shop_id);
                            ft.add(R.id.fragment_main, endFragment, fragmentTag);
//                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.addToBackStack(backStateName);
                            ft.commit();
                        }

//                        FragmentManager fragmentManager = getFragmentManager();
//                        endFragment.setId(shop_id);
//                        fragmentManager.beginTransaction()
//                                .add(R.id.fragment_main, endFragment)
//                                .addToBackStack(null)
//                                .commit();
                    }
                });

                addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user_id != 0) {
                            if (item_id != 0 && shop_id != 0) {
//                        new addToCart().execute();
                                new Core(getActivity()).addToCart(shop_id,item_id,item_name,item_price,item_image,item_desc,shop_name_txt);
                                FireOwl fireOwl = new FireOwl(getActivity());
                                fireOwl.addOrder(shop_id,item_id,user_id);
                            } else {
                                Log.d("Id Empty", "Item And Shop Ids are Empty");
                            }
                        } else {
                            DialogFragment newFragment = new LoginFragment();
                            newFragment.show(getFragmentManager(), "missiles");
                        }
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
                itemsJSON = core.getItem(item_id);
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
            if ((boolean) o) {
                Toast.makeText(getActivity(), "item added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "some thing went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Boolean state = false;
            Core core = new Core(getContext());
            FireOwl fireOwl = new FireOwl(getContext());
            try {
                fireOwl.addOrder(shop_id, item_id, user_id);
//                itemsJSON = core.addToCart(user_id,id);
                state = true;
            } catch (Exception e) {
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

    @Override
    public void onPause() {
//        if (((MainActivity) getActivity()).toolbar != null && ((MainActivity) getActivity()).toolbar.getVisibility() != View.VISIBLE) {
//            ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
//        }
        if (loadingData.getStatus() == AsyncTask.Status.RUNNING) {
            loadingData.cancel(true);
        }
        super.onPause();
    }

}
