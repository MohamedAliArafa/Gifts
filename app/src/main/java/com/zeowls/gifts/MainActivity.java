package com.zeowls.gifts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.zeowls.LoginFragment;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.BackEndOwl.FireOwl;
import com.zeowls.gifts.CategoryPage.CategoryContentFragment1;
import com.zeowls.gifts.GiftsTap.GiftsContentFragment1;
import com.zeowls.gifts.LoginPage.LoginActivity;
import com.zeowls.gifts.ShopsTap.ShopsContentFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    Firebase myFirebaseRef;
    FireOwl fireOwl = new FireOwl();
    NavigationView navigationView;
    TextView usernameNav;
    static Button cartCount;
    static int mCartCount = 0;
    static int userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        assert tabs != null;
        tabs.setupWithViewPager(viewPager);
        // Create Navigation drawer and inlfate layout
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://giftshop.firebaseio.com/");
        myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");

        // Set behavior of Navigation drawer
        assert navigationView != null;
        View header = navigationView.getHeaderView(0);
        usernameNav = (TextView) header.findViewById(R.id.nameNavText);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

//                        // TODO: handle navigation
                        if (menuItem.getItemId() == R.id.navLoginBTN) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawers();
                            return true;
                        }

                        if (menuItem.getItemId() == R.id.navLogoutBTN) {
                            SharedPreferences.Editor editor = getSharedPreferences("Credentials", MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            finish();
                            startActivity(getIntent());
                            mDrawerLayout.closeDrawers();
                            return true;
                        }
                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        // Adding Floating Action Button to bottom right of main view
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                //    Snackbar.make(v, fireOwl.addItem(MainActivity.this) + " Added", Snackbar.LENGTH_LONG).show();
                Snackbar.make(v, "Hello To bubble", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        SharedPreferences prefs = getSharedPreferences("Credentials", MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");
            userId = prefs.getInt("id", 0);
            if (userId != 0) {
                new cartCount().execute();
            }
            navigationView.getMenu().findItem(R.id.navLoginBTN).setVisible(false);
            navigationView.getMenu().findItem(R.id.navLogoutBTN).setVisible(true);
            usernameNav.setText(name);
        } else {
            navigationView.getMenu().findItem(R.id.navLoginBTN).setVisible(true);
            navigationView.getMenu().findItem(R.id.navLogoutBTN).setVisible(false);
            usernameNav.setText("Guest");
        }
        super.onResume();
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new GiftsContentFragment1(), "Gifts");
        adapter.addFragment(new ShopsContentFragment(), "Shops");
        adapter.addFragment(new CategoryContentFragment1(), "Catogries");
        viewPager.setAdapter(adapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.card_update_count);
        cartCount = (Button) MenuItemCompat.getActionView(item);
        cartCount.setText(String.valueOf(mCartCount));

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    public class cartCount extends AsyncTask<Void, Void, Objects> {

//        @Override
//        protected void onPostExecute(Objects objects) {
//            cartCount.setText(String.valueOf(mCartCount));
//            super.onPostExecute(objects);
//        }

        @Override
        protected Objects doInBackground(Void... params) {
            Core core = new Core(getBaseContext());
            mCartCount = core.cartCount(userId);
            return null;
        }
    }


    public class sendItems extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... params) {
            Core core = new Core(getBaseContext());
            return core.sendPrams();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            LoginFragment loginFragment = new LoginFragment();
            loginFragment.show(getFragmentManager(), "sign in dialog");

//            new sendItems().execute();
//            Intent intent = new Intent(MainActivity.this, newitem.class
//);
//            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}