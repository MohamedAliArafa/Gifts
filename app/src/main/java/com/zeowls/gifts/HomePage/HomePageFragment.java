package com.zeowls.gifts.HomePage;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.zeowls.gifts.R;
import com.zeowls.gifts.views.adapters.SamplePagerAdapter;

public class HomePageFragment extends Fragment {

    //    ActionBar supportActionBar;
    String imageTransitionName = "";
    String textTransitionName = "";
    Bundle bundle = new Bundle();
    ViewPager viewPager;
    TabLayout tabs;
    final ShopsContentFragment endFragment = new ShopsContentFragment();
    GiftsContentFragment1 GiftsFragment = new GiftsContentFragment1();
    ShopsContentFragment ShopFragment = new ShopsContentFragment();
    CategoryContentFragment1 CategoryFragment = new CategoryContentFragment1();
    boolean fab_show_icons = false;
    FloatingActionButton fab, fab1, fab2, fab3;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        if (supportActionBar != null) {
//            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
//            supportActionBar.setDisplayHomeAsUpEnabled(true);
//            supportActionBar.setTitle("Bubble");
//        }
        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) view.findViewById(R.id.tabs);

//        We Disable FAB Button on 18-4-2016 from here and from design also

//         Adding Floating Action Button to bottom right of main view
//        fab = (FloatingActionButton) view.findViewById(R.id.fab);
//        fab1 = (FloatingActionButton) view.findViewById(R.id.fab_1);
//        fab2 = (FloatingActionButton) view.findViewById(R.id.fab_2);
//        fab3 = (FloatingActionButton) view.findViewById(R.id.fab_3);
//
//        assert fab != null;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//
//                //Animations
//                Animation show_fab_1 = AnimationUtils.loadAnimation(getContext(), R.anim.fab1_show);
//                Animation hide_fab_1 = AnimationUtils.loadAnimation(getContext(), R.anim.fab1_hide);
//
//                Animation show_fab_2 = AnimationUtils.loadAnimation(getContext(), R.anim.fab2_show);
//                Animation hide_fab_2 = AnimationUtils.loadAnimation(getContext(), R.anim.fab2_hide);
//
//                Animation show_fab_3 = AnimationUtils.loadAnimation(getContext(), R.anim.fab3_show);
//                Animation hide_fab_3 = AnimationUtils.loadAnimation(getContext(), R.anim.fab3_hide);
//                if (!fab_show_icons){
//                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
//                    layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
//                    layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
//                    fab1.setLayoutParams(layoutParams);
//                    fab1.startAnimation(show_fab_1);
//                    fab1.setClickable(true);
//
//                    FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
//                    layoutParams1.rightMargin += (int) (fab2.getWidth() * 1.7);
//                    layoutParams1.bottomMargin += (int) (fab2.getHeight() * 0.25);
//                    fab2.setLayoutParams(layoutParams1);
//                    fab2.startAnimation(show_fab_2);
//                    fab2.setClickable(true);
//
//                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
//                    layoutParams2.rightMargin += (int) (fab3.getWidth() * 1.7);
//                    layoutParams2.bottomMargin += (int) (fab3.getHeight() * 0.25);
//                    fab3.setLayoutParams(layoutParams2);
//                    fab3.startAnimation(show_fab_3);
//                    fab3.setClickable(true);
//
//                    fab_show_icons = true;
//                }else {
//                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
//                    layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
//                    layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
//                    fab1.setLayoutParams(layoutParams);
//                    fab1.startAnimation(hide_fab_1);
//                    fab1.setClickable(false);
//
//                    FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
//                    layoutParams1.rightMargin -= (int) (fab2.getWidth() * 1.7);
//                    layoutParams1.bottomMargin -= (int) (fab2.getHeight() * 0.25);
//                    fab2.setLayoutParams(layoutParams1);
//                    fab2.startAnimation(hide_fab_2);
//                    fab2.setClickable(false);
//
//                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab1.getLayoutParams();
//                    layoutParams2.rightMargin -= (int) (fab3.getWidth() * 1.7);
//                    layoutParams2.bottomMargin -= (int) (fab3.getHeight() * 0.25);
//                    fab3.setLayoutParams(layoutParams2);
//                    fab3.startAnimation(hide_fab_3);
//                    fab3.setClickable(false);
//                    fab_show_icons = false;
//                }
//
//
//                Snackbar.make(v, "Hello To bubble", Snackbar.LENGTH_LONG).show();
//            }
//        });
        super.onViewCreated(view, savedInstanceState);
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        SamplePagerAdapter adapter = new SamplePagerAdapter(getFragmentManager());
        adapter.addFragment(GiftsFragment, "Gifts");
        adapter.addFragment(ShopFragment, "Shops");
        adapter.addFragment(CategoryFragment, "Categories");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        setupViewPager(viewPager);
        assert tabs != null;
        tabs.setupWithViewPager(viewPager);
        super.onResume();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
