package com.zeowls.gifts.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    final ShopsContentFragment ShopsTab = new ShopsContentFragment();
    final GiftsContentFragment1 GiftsTab = new GiftsContentFragment1();
    final CategoryContentFragment1 CategoryTab = new CategoryContentFragment1();
    @Override
    public void onCreate(Bundle savedInstanceState) {
//        GiftsFragment = new GiftsContentFragment1();
//        ShopFragment = new ShopsContentFragment();
//        CategoryFragment = new CategoryContentFragment1();
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
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        setupViewPager(viewPager);
        super.onViewStateRestored(savedInstanceState);
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        SamplePagerAdapter adapter = new SamplePagerAdapter(getFragmentManager());
        adapter.addFragment(GiftsTab, "Gifts");
        adapter.addFragment(ShopsTab, "Shops");
        adapter.addFragment(CategoryTab, "Categories");
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
