package com.zeowls.gifts.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.Activities.MainActivity;
import com.zeowls.gifts.Activities.ShoppingCartActivity;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.BackEndOwl.FireOwl;
import com.zeowls.gifts.R;
import com.zeowls.gifts.views.adapters.SlidingImage_Adapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class ItemDetailFragment extends Fragment {

    int item_id = 0;
    int shop_id = 0;
    int user_id = 0;
    ValueAnimator mAnimator;
    ValueAnimator mAnimator2;
    ValueAnimator mAnimator3;

    private ArrayList<String> ImagesArray = new ArrayList<>();

    TextView name, description, price, itemNameToolbar, shopName, item_detail_desc_2, item_detail_shop_name_2;
    Button visitShop, addToCart;
    ImageView itemPic, item_Shop_Photo, item_Shop_Photo_2;
    LinearLayout Details_Header, OverView_Header, Reviews_Header;
    LinearLayout Expandable_Reviews, Expandable_OverView, Expandable_Details;

    String item_name, item_price, item_image, item_desc, shop_name_txt, Shop_image, Shop_Address;

    Shop_Detail_Fragment_3 endFragment;

    Picasso picasso;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        picasso = Picasso.with(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        description = (TextView) view.findViewById(R.id.item_detail_desc);
        item_detail_desc_2 = (TextView) view.findViewById(R.id.item_detail_desc_2);
        addToCart = (Button) view.findViewById(R.id.addToCart);
        price = (TextView) view.findViewById(R.id.item_detail_price);
        shopName = (TextView) view.findViewById(R.id.item_detail_shop_name);
        item_detail_shop_name_2 = (TextView) view.findViewById(R.id.item_detail_shop_name_2);

        visitShop = (Button) view.findViewById(R.id.item_detail_shop_visit);
        //addToCart = (Button) view.findViewById(R.id.item_detail_addtocart);
        itemPic = (ImageView) view.findViewById(R.id.item_image_pager);

        Reviews_Header = (LinearLayout) view.findViewById(R.id.Reviews_Header);
        OverView_Header = (LinearLayout) view.findViewById(R.id.OverView_Header);
        Details_Header = (LinearLayout) view.findViewById(R.id.Details_Header);

        Expandable_Reviews = (LinearLayout) view.findViewById(R.id.Expandable_Reviews);
        Expandable_OverView = (LinearLayout) view.findViewById(R.id.Expandable_OverView);
        Expandable_Details = (LinearLayout) view.findViewById(R.id.Expandable_Details);

        item_Shop_Photo_2 = (ImageView) view.findViewById(R.id.item_Shop_Photo_2);
        item_Shop_Photo = (ImageView) view.findViewById(R.id.item_Shop_Photo);

        Bundle bundle = getArguments();
        String Title;
        Bitmap imageBitmap;
        String transText;
        String transitionName;

        if (bundle != null) {
            transitionName = bundle.getString("TRANS_NAME");
            Title = bundle.getString("ACTION");
            imageBitmap = bundle.getParcelable("IMAGE");
            transText = bundle.getString("TRANS_TEXT");
        }

        new loadingData().execute();
    }

    private void updateUI() {
        description.setText(item_desc);
        price.setText(item_price);
        shopName.setText(shop_name_txt);
        item_detail_desc_2.setText(item_desc);
        item_detail_shop_name_2.setText(shop_name_txt);
        if (item_name != null && ((MainActivity) getActivity()).toolbar != null) {
            ((MainActivity) getActivity()).toolbar.setTitle(item_name);
        }
//        ((MainActivity) getActivity()).mDrawerToggle.setDrawerIndicatorEnabled(false);

        picasso.load("http://bubble.zeowls.com/uploads/" + Shop_image).fit().centerCrop().into(item_Shop_Photo);
        picasso.load("http://bubble.zeowls.com/uploads/" + Shop_image).fit().centerCrop().into(item_Shop_Photo_2);
        picasso.load("http://bubble.zeowls.com/uploads/" + item_image).fit().centerCrop().into(itemPic);

        Expandable_Reviews.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        Expandable_Reviews.getViewTreeObserver().removeOnPreDrawListener(this);
                        Expandable_Reviews.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                        Expandable_Reviews.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0, Expandable_Reviews.getMeasuredHeight());
                        return true;
                    }
                });


        Reviews_Header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Expandable_Reviews.getVisibility() == View.GONE) {
                    expand();
                } else {
                    collapse();
                }
            }
        });


        Expandable_OverView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        Expandable_OverView.getViewTreeObserver().removeOnPreDrawListener(this);
                        Expandable_OverView.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                        Expandable_Reviews.measure(widthSpec, heightSpec);

                        mAnimator2 = slideAnimator2(0, Expandable_OverView.getMeasuredHeight());
                        return true;
                    }
                });


        OverView_Header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Expandable_OverView.getVisibility() == View.GONE) {
                    expand2();
                } else {
                    collapse2();
                }
            }
        });


        Expandable_Details.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        Expandable_Details.getViewTreeObserver().removeOnPreDrawListener(this);
                        Expandable_Details.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                        Expandable_Reviews.measure(widthSpec, heightSpec);

                        mAnimator3 = slideAnimator3(0, Expandable_Details.getMeasuredHeight());
                        return true;
                    }
                });


        Details_Header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Expandable_Details.getVisibility() == View.GONE) {
                    expand3();
                } else {
                    collapse3();
                }
            }
        });

    }

    public void setId(int id) {
        this.item_id = id;
    }

    private class loadingData extends AsyncTask {

        JSONObject itemsJSON;

        @Override
        protected void onPostExecute(Object o) {
            try {
                JSONObject item = itemsJSON.getJSONArray("Items").getJSONObject(0);

                item_name = item.getString("name");
                item_price = "$" + item.getString("price");
                item_image = item.getString("image");
                item_desc = item.getString("description");
                shop_name_txt = item.getString("shop_name");
                shop_id = item.getInt("shop_id");
                Shop_image = item.getString("shop_image");


                updateUI();

                endFragment = new Shop_Detail_Fragment_3();

                visitShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fragmentTag = "ShopFragment";
                        String backStateName = this.getClass().getName();
                        FragmentManager manager = getFragmentManager();
                        if (manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
                            FragmentTransaction ft = manager.beginTransaction();
                            endFragment.setId(shop_id);
                            ft.add(R.id.fragment_main, endFragment, fragmentTag);
                            ft.addToBackStack(backStateName);
                            ft.commit();
                        }
                    }
                });


                addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (user_id != 0) {
                            if (item_id != 0 && shop_id != 0) {
                                new Core(getActivity()).addToCart(shop_id, item_id, item_name, item_price, item_image, item_desc, shop_name_txt);
                                FireOwl fireOwl = new FireOwl(getActivity());
                                fireOwl.addOrder(shop_id, item_id, user_id);

                                ShoppingCartActivity endFragment2 =  new ShoppingCartActivity();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .hide(getFragmentManager().findFragmentByTag("homeFragment"))
                                        .add(R.id.fragment_main, endFragment2)
                                        .addToBackStack(null)
                                        .commit();
//                                Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
//                                startActivity(intent);
                            } else {
                                Log.d("Id Empty", "Item And Shop Ids are Empty");
                            }
//                        } else {
//                            Toast.makeText(getActivity(), "please login first", Toast.LENGTH_SHORT).show();
//                            ((MainActivity) getActivity()).mDrawerLayout.openDrawer(GravityCompat.START);
////                            DialogFragment newFragment = new LoginFragment();
////                            newFragment.show(getFragmentManager(), "missiles");
//                        }
                    }
                });
                ((MainActivity) getActivity()).toolbar.setTitle(item_name);


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

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).toolbar.setTitle("Bubble");

    }

    private void expand() {
        //set Visible
        Expandable_Reviews.setVisibility(View.VISIBLE);
        /* Remove and used in preDrawListener
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);

		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/
        mAnimator.start();
    }


    private void collapse() {
        int finalHeight = Expandable_Reviews.getHeight();
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                Expandable_Reviews.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = Expandable_Reviews.getLayoutParams();
                layoutParams.height = value;
                Expandable_Reviews.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    private void expand2() {
        //set Visible
        Expandable_OverView.setVisibility(View.VISIBLE);
        /* Remove and used in preDrawListener
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);

		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/
        mAnimator2.start();
    }

    private void collapse2() {

        int finalHeight = Expandable_OverView.getHeight();
        ValueAnimator mAnimator = slideAnimator2(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                Expandable_OverView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator2(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = Expandable_OverView.getLayoutParams();
                layoutParams.height = value;
                Expandable_OverView.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    private void expand3() {
        //set Visible
        Expandable_Details.setVisibility(View.VISIBLE);
        /* Remove and used in preDrawListener
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);

		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/
        mAnimator3.start();
    }

    private void collapse3() {
        int finalHeight = Expandable_Details.getHeight();
        ValueAnimator mAnimator = slideAnimator3(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                Expandable_Details.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();


    }


    private ValueAnimator slideAnimator3(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = Expandable_Details.getLayoutParams();
                layoutParams.height = value;
                Expandable_Details.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    @Override
    public void onResume() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Credentials", getActivity().MODE_PRIVATE);
        user_id = prefs.getInt("id", 0);
        super.onResume();
    }


}
