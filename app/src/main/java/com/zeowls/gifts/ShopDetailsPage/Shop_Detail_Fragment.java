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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.MainActivity;
import com.zeowls.gifts.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Shop_Detail_Fragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {


    int id = 0;
    CollapsingToolbarLayout collapsingToolbar;
    TextView Shop_Name, Shop_Name_before, Shop_Slogan,shop_phone,shop_email,shop_address, Shop_Slogan_Title;
    ImageView Shop_Pic, ShopHeader_Pic;
    CardView Shop_Slogan_Card;

    Picasso picasso;
    public ActionBar supportActionBar;
    Button Shop_Items_btn;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    // private static final int NUM_PAGES = 5;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 1000;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;

    protected FragmentActivity myContext;

    loadingData loadingData;
    ShopAllItemsFragment shopAllItemsFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (((MainActivity) getActivity()).toolbar != null) {
            ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loadingData = new loadingData();
        if (loadingData.getStatus() != AsyncTask.Status.RUNNING) {
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
        String mobile = "",email = "",address = "",owner_name = "";

        if (bundle != null) {
            transitionName = bundle.getString("TRANS_NAME");
            actionTitle = bundle.getString("ACTION");
            imageBitmap = bundle.getParcelable("IMAGE");
            transText = bundle.getString("TRANS_TEXT");
            mobile = bundle.getString("TRANS_MOBILE");
            email = bundle.getString("TRANS_EMAIL");
            address = bundle.getString("TRANS_ADDRESS");
            owner_name = bundle.getString("TRANS_OWNER_NAME");
        }

        Shop_Items_btn = (Button) view.findViewById(R.id.Shop_Items_btn);
        // Set Collapsing Toolbar layout to the screen
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        Shop_Name_before = (TextView) view.findViewById(R.id.item_Detail_Shop_title1);
        Shop_Name = (TextView) view.findViewById(R.id.item_Detail_Shop_title);
        Shop_Slogan_Card = (CardView) view.findViewById(R.id.item_Detail_Shop_Slogan_card);
        Shop_Slogan = (TextView) view.findViewById(R.id.item_Detail_Shop_Slogan);
        Shop_Slogan_Title = (TextView) view.findViewById(R.id.item_Detail_Slogan);
        Shop_Pic = (ImageView) view.findViewById(R.id.item_Detail_SHop_Image);
        ShopHeader_Pic = (ImageView) view.findViewById(R.id.image);

        shop_phone = (TextView) view.findViewById(R.id.shop_phone);
        shop_email = (TextView) view.findViewById(R.id.shop_email);
        shop_address = (TextView) view.findViewById(R.id.shop_address);

        if (!mobile.equals("null")) {
            shop_phone.setText(mobile);
        }else {
            shop_phone.setText("No Mobile Available");
        }
        if (!email.equals("null")) {
            shop_email.setText(email);
        }else {
            shop_email.setText("No Email Available");
        }
        if (!address.equals("null")) {
            shop_address.setText(address);
        }else {
            shop_address.setText("No Address Available");
        }
        if (!owner_name.equals("null")) {
            Shop_Slogan_Title.setText(owner_name);
        }else {
            Shop_Slogan_Title.setText("No Owner Name Available");
        }

        if (imageBitmap != null) {
            Shop_Pic.setImageBitmap(imageBitmap);
            ShopHeader_Pic.setImageBitmap(imageBitmap);
        }
        Shop_Name_before.setText(actionTitle);
        Shop_Name.setText(actionTitle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Shop_Name_before.setTransitionName(transitionName);
            ShopHeader_Pic.setTransitionName(transText);
        }

        mTitleContainer = (LinearLayout) view.findViewById(R.id.main_linearlayout_title);
        AppBarLayout mAppBarLayout = (AppBarLayout) view.findViewById(R.id.main_appbar);
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(Shop_Name, 0, View.INVISIBLE);
        picasso = Picasso.with(getContext());
        collapsingToolbar.setTitle(getString(R.string.item_title));


        Shop_Items_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                shopAllItemsFragment =  new ShopAllItemsFragment();
                shopAllItemsFragment.setId(id);
                fragmentTransaction.replace(R.id.fragment_main, shopAllItemsFragment);
                fragmentTransaction.commit();
            }
        });

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
                if (itemsJSON != null) {
                    if (!itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("description").equals("null")) {
                        if (!itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("description").isEmpty()) {
                            Shop_Slogan_Card.setVisibility(View.VISIBLE);
                            Shop_Slogan.setText(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("description"));
                        }
                    }

                    if (!itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("mobile").equals("null")) {
                        if (!itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("mobile").isEmpty()) {
                            shop_phone.setText(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("mobile"));
                        }
                    }

                    if (!itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("owner_email").equals("null")) {
                        if (!itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("owner_email").isEmpty()) {
                            shop_email.setText(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("owner_email"));
                        }
                    }
                    if (!itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("shop_address").equals("null")) {
                        if (!itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("shop_address").isEmpty()) {
                            shop_address.setText(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("shop_address"));
                        }
                    }

                    collapsingToolbar.setTitle(itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("name"));
                    String shopName = itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("name");
                    Shop_Name_before.setText(shopName);
                    Shop_Name.setText(shopName);

                    if (!itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("profile_pic").equals("null")) {
                        String profilePic = "http://bubble.zeowls.com/uploads/" + itemsJSON.getJSONArray("Shop").getJSONObject(0).getString("profile_pic");
                        picasso.load(profilePic).fit().into(ShopHeader_Pic);
                        picasso.load(profilePic).fit().centerInside().into(Shop_Pic);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
