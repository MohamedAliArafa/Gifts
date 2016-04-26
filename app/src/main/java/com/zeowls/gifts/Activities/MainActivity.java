package com.zeowls.gifts.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.zeowls.gifts.Fragments.LoginFragment;
import com.zeowls.gifts.Fragments.HomePageFragment;
import com.zeowls.gifts.R;
import com.zeowls.gifts.provider.Contract;

import com.zeowls.gifts.provider.Contract.CartEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public ActionBar supportActionBar;
    public AppBarLayout appBarLayout;
    public Toolbar toolbar;

    NavigationView navigationView;
    TextView usernameNav;

    RelativeLayout Shopping_Cart_Relative;
    public TextView notif_count;
    public int mCartCount = 0;
    static int userId = 1;

    private int CART_LOADER = 0;

    static final int COL_CART_ID = 0;
    static final int COL_CART_ITEM_ID = 1;
    static final int COL_CART_ITEM_NAME = 2;
    static final int COL_CART_IETME_PRICE = 3;
    static final int COL_CART_ITEM_IMAGE = 4;
    static final int COL_CART_ITEM_DESC = 5;
    static final int COL_CART_SHOP_ID = 6;
    static final int COL_CART_SHOPE_NAME = 7;

    private static final String[] CART_COLUMNS = {
            Contract.CartEntry.TABLE_NAME + "." + Contract.CartEntry._ID,
            CartEntry.COLUMN_ITEM_ID,
            CartEntry.COLUMN_ITEM_NAME,
            CartEntry.COLUMN_ITEM_PRICE,
            CartEntry.COLUMN_ITEM_PHOTO,
            CartEntry.COLUMN_ITEM_PHOTO,
            CartEntry.COLUMN_ITEM_DESC,
            CartEntry.COLUMN_SHOP_ID,
            CartEntry.COLUMN_SHOP_NAME
    };

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    HomePageFragment fragment;
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new HomePageFragment();
        fragmentTransaction.replace(R.id.fragment_main, fragment, "homeFragment");
        fragmentTransaction.commit();
//        }

        configureToolbar();
        configureNavigationView();
        configureDrawer();
    }

    public void configureNavigationView() {
        // Set behavior of Navigation drawer
        assert navigationView != null;
        View header = navigationView.getHeaderView(0);
        usernameNav = (TextView) header.findViewById(R.id.nameNavText);

        SharedPreferences preferences = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        userId = preferences.getInt("id", 0);

        Menu menuNav = navigationView.getMenu();

        MenuItem login = menuNav.findItem(R.id.navLoginBTN);
        MenuItem logout = menuNav.findItem(R.id.navLogoutBTN);
        MenuItem fav = menuNav.findItem(R.id.navFavBTN);

        login.setVisible(false);
        login.setTitle("Login");

        if (userId == 0) {
            fav.setVisible(false);
            login.setVisible(true);
            logout.setVisible(false);
        } else {
            fav.setVisible(true);
            login.setVisible(false);
            logout.setVisible(true);
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

//                        // TODO: handle navigation
                        if (menuItem.getItemId() == R.id.navHomeBTN) {

                            if (fragmentManager.findFragmentByTag("homeFragment") == null) {
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragment = new HomePageFragment();
                                fragmentTransaction.replace(R.id.fragment_main, fragment, "homeFragment");
                                fragmentTransaction.commit();
                            }else {
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_main, fragmentManager.findFragmentByTag("homeFragment"));
                                fragmentTransaction.commit();
                            }

                            mDrawerLayout.closeDrawers();
                            return true;
                        }


                        if (menuItem.getItemId() == R.id.navLoginBTN) {

                            DialogFragment newFragment = new LoginFragment();
                            newFragment.show(getSupportFragmentManager(), "missiles");

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
        configureToolbar();
        configureNavigationView();
        configureDrawer();
        SharedPreferences prefs = getSharedPreferences("Credentials", MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");
            userId = prefs.getInt("id", 0);
            if (userId != 0) {
//                new cartCount().execute();
                Firebase ref = new Firebase("https://giftshop.firebaseio.com/orders/User");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        System.out.println(snapshot.getValue());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
            }
//            navigationView.getMenu().findItem(R.id.navLoginBTN).setVisible(false);
//            navigationView.getMenu().findItem(R.id.navLogoutBTN).setVisible(true);
            usernameNav.setText(name);
        } else {
//            navigationView.getMenu().findItem(R.id.navLoginBTN).setVisible(true);
//            navigationView.getMenu().findItem(R.id.navLogoutBTN).setVisible(false);
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
        Shopping_Cart_Relative = (RelativeLayout) MenuItemCompat.getActionView(item).findViewById(R.id.Shopping_Cart_Relative);
        notif_count = (TextView) MenuItemCompat.getActionView(item).findViewById(R.id.notif_count);
//        notif_count.setText(String.valueOf(mCartCount));
        getSupportLoaderManager().initLoader(CART_LOADER, null, this);
        Shopping_Cart_Relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCartCount != 0) {
                    Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Shopping Cart Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

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
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Contract.CartEntry.CONTENT_URI, CART_COLUMNS, null, null, Contract.CartEntry._ID + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCartCount = data.getCount();
        notif_count.setText(String.valueOf(mCartCount));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}