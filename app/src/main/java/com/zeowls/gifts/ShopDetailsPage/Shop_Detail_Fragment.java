package com.zeowls.gifts.ShopDetailsPage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Shop_Detail_Fragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {


    int id = 0;
    CollapsingToolbarLayout collapsingToolbar;
    TextView Shop_Name, Shop_Name_before, Shop_Slogan;
    ImageView Shop_Pic, ShopHeader_Pic;

    Picasso picasso;
    public ActionBar supportActionBar;

    // private static final int NUM_PAGES = 5;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;

    protected FragmentActivity myContext;

    loadingData loadingData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loadingData = new loadingData();
        if (loadingData.getStatus() != AsyncTask.Status.RUNNING){
            loadingData.execute();
        }
        return inflater.inflate(R.layout.shop_details_in_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
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

//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        if (supportActionBar != null) {
//            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
//            supportActionBar.setDisplayHomeAsUpEnabled(true);
//            supportActionBar.setTitle("");
//        }

        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        Shop_Name_before = (TextView) view.findViewById(R.id.item_Detail_Shop_title1);
        Shop_Name = (TextView) view.findViewById(R.id.item_Detail_Shop_title);
        Shop_Slogan = (TextView) view.findViewById(R.id.item_Detail_Shop_Slogan);
        Shop_Pic = (ImageView) view.findViewById(R.id.item_Detail_SHop_Image);
        ShopHeader_Pic = (ImageView) view.findViewById(R.id.image);

        Shop_Pic.setImageBitmap(imageBitmap);
        ShopHeader_Pic.setImageBitmap(imageBitmap);
        Shop_Name_before.setText(actionTitle);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Shop_Name_before.setTransitionName(transitionName);
            ShopHeader_Pic.setTransitionName(transText);
        }

        mTitleContainer = (LinearLayout) view.findViewById(R.id.main_linearlayout_title);
        AppBarLayout mAppBarLayout = (AppBarLayout) view.findViewById(R.id.main_appbar);
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(Shop_Name, 0, View.INVISIBLE);
        picasso = Picasso.with(getContext());
        collapsingToolbar.setTitle(getString(R.string.item_title));

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

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(Shop_Name, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(Shop_Name, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
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

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public void setId(int id) {
        this.id = id;
    }


    private class loadingData extends AsyncTask<Void, Void, Object> {

        JSONObject itemsJSON;

        @Override
        protected Object doInBackground(Void... params) {
            Core core = new Core(getContext());
            try {
                itemsJSON = core.getShop(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                Shop_Slogan.setText(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("description"));
                collapsingToolbar.setTitle(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("name"));

                String shopName = itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("name");
//                Shop_Name_before.setText(shopName);
                Shop_Name.setText(shopName);
                String profilePic = "http://bubble.zeowls.com/uploads/" + itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("profile_pic");
//                picasso.load(profilePic).fit().into(ShopHeader_Pic);
//                picasso.load(profilePic).fit().centerInside().into(Shop_Pic);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (loadingData.getStatus() == AsyncTask.Status.RUNNING){
            loadingData.cancel(true);
        }
        super.onDestroy();
    }
}
