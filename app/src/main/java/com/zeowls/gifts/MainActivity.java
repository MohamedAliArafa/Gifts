package com.zeowls.gifts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zeowls.LoginFragment;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.HomePage.HomePageFragment1;
import com.zeowls.gifts.LoginPage.LoginActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public ActionBar supportActionBar;
    public AppBarLayout appBarLayout;
    public Toolbar toolbar;

    NavigationView navigationView;
    TextView usernameNav;

    static Button cartCount;
    static int mCartCount = 0;
    static int userId = 0;

    Fragment mContent;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    HomePageFragment1 fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            Fragment mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }else {

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new HomePageFragment1();
            fragmentTransaction.replace(R.id.fragment_main, fragment, "homeFragment");
            fragmentTransaction.commit();
        }

        configureToolbar();
        configureNavigationView();
        configureDrawer();
    }

    public void configureNavigationView() {
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
    }


    public void configureToolbar() {
        // Adding Toolbar to Main screen
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        // Create Navigation drawer and inlfate layout
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // Adding menu icon to Toolbar
        supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void configureDrawer() {
        // Configure drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_closed) {

            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.card_update_count);
        cartCount = (Button) MenuItemCompat.getActionView(item);
        cartCount.setText(String.valueOf(mCartCount));
        cartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Cart", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

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
        }
//        if (id == android.R.id.home) {
//            mDrawerLayout.openDrawer(GravityCompat.START);
//        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }
}