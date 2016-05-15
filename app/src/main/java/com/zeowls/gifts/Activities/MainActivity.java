package com.zeowls.gifts.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.Fragments.HomePageFragment;
import com.zeowls.gifts.Models.UserDataModel;
import com.zeowls.gifts.QuickstartPreferences;
import com.zeowls.gifts.R;
import com.zeowls.gifts.RegistrationIntentService;
import com.zeowls.gifts.Utility.PrefUtils;
import com.zeowls.gifts.provider.Contract;

import com.zeowls.gifts.provider.Contract.CartEntry;

import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

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
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    static int userId = 1;
    public ActionBarDrawerToggle mDrawerToggle;
    public ActionBar supportActionBar;
    public AppBarLayout appBarLayout;
    public Toolbar toolbar;
    public TextView notif_count;
    public int mCartCount = 0;
    Picasso picasso;
    NavigationView navigationView;
    TextView usernameNav;
    ImageView userimageNav;
    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    UserDataModel user;
    RelativeLayout Shopping_Cart_Relative;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    HomePageFragment fragment;
    ShoppingCartActivity shoppingFragment;
    public DrawerLayout mDrawerLayout;
    private boolean isResumed = false;
    private int CART_LOADER = 0;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SDK init
        FacebookSdk.sdkInitialize(getBaseContext());
        callbackManager = CallbackManager.Factory.create();
        Firebase.setAndroidContext(this);
        user = new UserDataModel();
        AppEventsLogger.activateApp(this);
        picasso = Picasso.with(getBaseContext());

        //Set View Cotent
        setContentView(R.layout.activity_main);

        //check user ID
        try {
            userId = PrefUtils.getCurrentUser(getBaseContext()).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //track FB login
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (isResumed) {
                    FragmentManager manager = getSupportFragmentManager();
                    int backStackSize = manager.getBackStackEntryCount();
                    for (int i = 0; i < backStackSize; i++) {
                        manager.popBackStack();
                    }
                    if (currentAccessToken != null) {
                        Log.i(TAG, currentAccessToken.getToken());
                    } else {
                        PrefUtils.clearCurrentUser(getBaseContext());
                        userId = 0;
                        configureNavigationView();
                    }
                }
            }
        };

        //init home fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new HomePageFragment();
        fragmentTransaction.replace(R.id.fragment_main, fragment, "homeFragment");
        fragmentTransaction.commit();

        configureToolbar();
        configureNavigationView();
        configureDrawer();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.i(TAG, "Register Token SENT to Server");
                } else {
                    Log.i(TAG, "Register Token NOT SENT to Server for some reason");
                }
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void configureNavigationView() {
        // Set behavior of Navigation drawer
        assert navigationView != null;

        View header = navigationView.getHeaderView(0);
        usernameNav = (TextView) header.findViewById(R.id.nameNavText);
        userimageNav = (ImageView) header.findViewById(R.id.nameNavImage);

        try {


            userId = PrefUtils.getCurrentUser(this).getId();
            picasso.load(PrefUtils.getCurrentUser(this).getProfilePic()).into(userimageNav);
            usernameNav.setText(PrefUtils.getCurrentUser(this).getName());
        } catch (Exception e) {
            e.printStackTrace();
            usernameNav.setText("Guest");
            userimageNav.setImageDrawable(null);
        }

        loginButton = (LoginButton) header.findViewById(R.id._Facebook_login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
//        loginButton.setFragment(getBaseContext());

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                user.setFBToken(loginResult.getAccessToken().getToken());
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("response: ", response + "");
                                try {
                                    new UserFBLoginTask(object).execute();
                                    Toast.makeText(getBaseContext(), "welcome " + object.getString("name"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday, picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "You Have CANCELED the Login Request", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
            }
        });



        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

//                        // TODO: handle navigation
                        if (menuItem.getItemId() == R.id.navHomeBTN) {

                            if (fragmentManager.findFragmentByTag("homeFragment") == null) {
                                for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                    fragmentManager.popBackStack();
                                }
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragment = new HomePageFragment();
                                fragmentTransaction.replace(R.id.fragment_main, fragment, "homeFragment");
                                fragmentTransaction.commit();
                            } else {
                                for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                    fragmentManager.popBackStack();
                                }
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_main, fragmentManager.findFragmentByTag("homeFragment"));
                                fragmentTransaction.commit();
                            }

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

        // Create Navigation drawer and inflate layout
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
        mDrawerToggle.setDrawerIndicatorEnabled(true);
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
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        isResumed = true;

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);

        registerReceiver();
        configureToolbar();
        configureNavigationView();
        configureDrawer();

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
        getSupportLoaderManager().initLoader(CART_LOADER, null, this);
        Shopping_Cart_Relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCartCount != 0) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    shoppingFragment = new ShoppingCartActivity();
                    fragmentTransaction.add(R.id.fragment_main, shoppingFragment);
                    fragmentTransaction.hide(fragmentManager.findFragmentByTag("homeFragment"));
                    fragmentTransaction.addToBackStack(null)         ;
                    fragmentTransaction.commit();
//                    Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
//                    startActivity(intent);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public class UserFBLoginTask extends AsyncTask<Void, Void, Void> {

        private JSONObject mGraphResponse;

        UserFBLoginTask(JSONObject GraphResponse) {
            mGraphResponse = GraphResponse;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Core core = new Core(getBaseContext());
            try {
                JSONObject object = mGraphResponse;
                int user_id = core.signUpFBUser(object, user.getFBToken());
                userId = user_id;
                user.setId(user_id);
                user.setFacebookID(object.getInt("id"));
                user.setProfilePic(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                user.setName(object.getString("name"));
                user.setGender(object.getString("gender"));
                user.setEmail(object.getString("email"));
                user.setDOB(object.getString("birthday"));

                PrefUtils.setCurrentUser(user, getBaseContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PrefUtils.setCurrentUser(user, getBaseContext());
            configureNavigationView();
        }

        //        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//        }
    }
}