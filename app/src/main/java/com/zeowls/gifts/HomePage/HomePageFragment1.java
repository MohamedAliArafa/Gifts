package com.zeowls.gifts.HomePage;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zeowls.gifts.R;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment1 extends Fragment {

    //    ActionBar supportActionBar;
    String imageTransitionName = "";
    String textTransitionName = "";
    Bundle bundle = new Bundle();
    ViewPager viewPager;
    TabLayout tabs;
    final ShopsContentFragment ShopsTab = new ShopsContentFragment();
    final GiftsContentFragment1 GiftsTab = new GiftsContentFragment1();
    final CategoryContentFragment1 CategoryTab = new CategoryContentFragment1();

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    HomePageFragment fragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page_1, container, false);
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
        //  viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        // Set Tabs inside Toolbar


        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main_2, GiftsTab);
        fragmentTransaction.commit();


        tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Tab 1"));
        tabs.addTab(tabs.newTab().setText("Tab 2"));
        tabs.addTab(tabs.newTab().setText("Tab 3"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Toast.makeText(getContext(), "Hi " + tab.getPosition(), Toast.LENGTH_SHORT).show();
                switch (tab.getPosition()) {
                    case 0:
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_main_2, GiftsTab);
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_main_2, ShopsTab);
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_main_2, CategoryTab);
                        fragmentTransaction.commit();
                        break;
                    default:
                        Toast.makeText(getContext(), "Default", Toast.LENGTH_SHORT).show();
                        break;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Adding Floating Action Button to bottom right of main view
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Snackbar.make(v, "Hello To bubble", Snackbar.LENGTH_LONG).show();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

//    // Add Fragments to Tabs
//    private void setupViewPager(ViewPager viewPager) {
//        Adapter adapter = new Adapter(getFragmentManager());
//        adapter.addFragment(new GiftsContentFragment1(), "Gifts");
//        adapter.addFragment(new ShopsContentFragment(), "Shops");
//        adapter.addFragment(new CategoryContentFragment1(), "Catogries");
//        viewPager.setAdapter(adapter);
//    }

    @Override
    public void onResume() {
//        setupViewPager(viewPager);
//        assert tabs != null;
//        tabs.setupWithViewPager(viewPager);
        super.onResume();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
