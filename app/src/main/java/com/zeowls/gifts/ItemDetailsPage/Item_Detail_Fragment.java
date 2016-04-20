package com.zeowls.gifts.ItemDetailsPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.zeowls.gifts.ImageSLider2.SlidingImage_Adapter;
import com.zeowls.gifts.LoginPage.LoginActivity;
import com.zeowls.gifts.MainActivity;
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
    private static final Integer[] IMAGES = {R.drawable.android, R.drawable.android1};
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
        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadingData = new loadingData();
        if (loadingData.getStatus() != AsyncTask.Status.RUNNING){
            loadingData.execute();
        }
        return inflater.inflate(R.layout.item_detail_in_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        shop_name = (TextView) view.findViewById(R.id.item_Detail_Shop_title);
        visitShop = (Button) view.findViewById(R.id.Item_Detail_Shop_Visit);
        addToCart = (Button) view.findViewById(R.id.addToCartBTN);
        Item_Pic = (ImageView) view.findViewById(R.id.item_Detail_Image);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new SlidingImage_Adapter(getContext(), ImagesArray);
        mPager.setAdapter(mPagerAdapter);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Shop_Name_before.setTransitionName(transitionName);
            Item_Pic.setTransitionName(transText);
        }

        Item_Pic.setImageBitmap(imageBitmap);
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
                    }else {
                        Log.d("Id Empty","Item And Shop Ids are Empty");
                    }
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
        SharedPreferences prefs = getActivity().getSharedPreferences("Credentials", getActivity().MODE_PRIVATE);
        user_id = prefs.getInt("id", 0);
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
                description.setText(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("description"));
                price.setText("$" + itemsJSON.getJSONArray("Items").getJSONObject(0).getString("price"));
                item_name_toolbar.setText(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("name"));
                shop_name.setText(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("shop_name"));
                shop_id= itemsJSON.getJSONArray("Items").getJSONObject(0).getInt("shop_id");
                collapsingToolbar.setTitle(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("name"));
                name.setText(itemsJSON.getJSONArray("Items").getJSONObject(0).getString("name"));
                picasso.load("http://bubble.zeowls.com/uploads/" + itemsJSON.getJSONArray("Items").getJSONObject(0).getString("image")).fit().centerCrop().into(Item_Pic);
                endFragment = new Shop_Detail_Fragment();

                final Bundle bundle = new Bundle();
                final String imageTransitionName = "transition";
                final String textTransitionName = "transtext";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    name.setTransitionName(textTransitionName);
//                    holder.imageView.setTransitionName(imageTransitionName);
//                setSharedElementReturnTransition(TransitionInflater.from(
//                        getActivity()).inflateTransition(R.transition.change_image_trans));
                    setExitTransition(TransitionInflater.from(
                            getActivity()).inflateTransition(android.R.transition.fade));

                    endFragment.setSharedElementEnterTransition(TransitionInflater.from(
                            getActivity()).inflateTransition(R.transition.change_image_trans));
                    endFragment.setSharedElementReturnTransition(TransitionInflater.from(
                            getActivity()).inflateTransition(R.transition.change_image_trans));
                    endFragment.setEnterTransition(TransitionInflater.from(
                            getActivity()).inflateTransition(android.R.transition.fade));
                }
                bundle.putString("TRANS_NAME", imageTransitionName);
                bundle.putString("TRANS_TEXT", textTransitionName);
                bundle.putString("ACTION", name.getText().toString());
//                bundle.putParcelable("IMAGE", ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap());
                visitShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = getFragmentManager();
                        try {
                            endFragment.setId(itemsJSON.getJSONArray("Items").getJSONObject(0).getInt("shop_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fragmentManager.beginTransaction()
                                .add(R.id.fragment_main, endFragment)
                                .addToBackStack(null)
                                .commit();
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
            FireOwl fireOwl = new FireOwl(getContext());
            try {
                fireOwl.addOrder(shop_id,item_id, user_id);
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
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        if (loadingData.getStatus() == AsyncTask.Status.RUNNING) {
            loadingData.cancel(true);
        }
        super.onPause();
    }

}
