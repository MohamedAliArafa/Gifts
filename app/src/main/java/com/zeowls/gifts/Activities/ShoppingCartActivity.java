package com.zeowls.gifts.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;
import com.zeowls.gifts.Utility.PrefUtils;
import com.zeowls.gifts.provider.Contract;
import com.zeowls.gifts.views.adapters.CursorRecyclerViewAdapter;

import org.json.JSONObject;

public class ShoppingCartActivity extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    int userId = 0;
    RecyclerView recyclerView;
    CursorContentAdapter adapter;
    Picasso picasso;
    private static final String TAG = "ShoppingCart";

    public int mCartCount = 0;

    private int CART_LOADER = 0;
    Core core;

    static final int COL_CART_ID = 0;
    static final int COL_CART_ITEM_ID = 1;
    static final int COL_CART_ITEM_NAME = 2;
    static final int COL_CART_ITEM_PRICE = 3;
    static final int COL_CART_ITEM_IMAGE = 4;
    static final int COL_CART_ITEM_DESC = 5;
    static final int COL_CART_SHOP_ID = 6;
    static final int COL_CART_SHOP_NAME = 7;

    private static final String[] CART_COLUMNS = {
            Contract.CartEntry.TABLE_NAME + "." + Contract.CartEntry._ID,
            Contract.CartEntry.COLUMN_ITEM_ID,
            Contract.CartEntry.COLUMN_ITEM_NAME,
            Contract.CartEntry.COLUMN_ITEM_PRICE,
            Contract.CartEntry.COLUMN_ITEM_PHOTO,
            Contract.CartEntry.COLUMN_ITEM_DESC,
            Contract.CartEntry.COLUMN_SHOP_ID,
            Contract.CartEntry.COLUMN_SHOP_NAME
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        core = new Core(getActivity());


        getLoaderManager().initLoader(CART_LOADER, null, this);
        new loadingOrdersData().execute();
        picasso = Picasso.with(getActivity());

        try {
            userId = PrefUtils.getCurrentUser(getActivity()).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.activity_shopping_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.shopping_cart_recycler_view);
    }

    private class GetCart extends AsyncTask {

        JSONObject itemsJSON;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Object o) {
//            try {
//
//              //  itemName.setText(itemsJSON.getJSONArray("Cart").getJSONObject(0).getString("item_name"));
//               // price.setText( "$" + itemsJSON.getJSONArray("Cart").getJSONObject(0).getString("item_price"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Core core = new Core(getActivity());
            itemsJSON = core.getUserCart(userId);
            return true;
        }
    }


    public class CursorContentAdapter extends CursorRecyclerViewAdapter<CursorContentAdapter.ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static final int LENGTH = 18;

        public CursorContentAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView Shopping_Cart_Item_Image;
            TextView Shopping_Cart_Shop_Name, Shopping_Cart_Item_Name, Shopping_Cart_Item_Price;
            Button Shopping_Cart_Check_Out;

            public ViewHolder(View view) {
                super(view);
                Shopping_Cart_Shop_Name = (TextView) view.findViewById(R.id.Shopping_Cart_Shop_Name);
                Shopping_Cart_Item_Name = (TextView) view.findViewById(R.id.Shopping_Cart_Item_Name);
                Shopping_Cart_Item_Price = (TextView) view.findViewById(R.id.Shopping_Cart_Item_Price);
                Shopping_Cart_Check_Out = (Button) view.findViewById(R.id.Shopping_Cart_Check_Out);
                Shopping_Cart_Item_Image = (ImageView) view.findViewById(R.id.Shopping_Cart_Item_Image);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_recycler_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, Cursor cursor) {

            final Cursor data = cursor;
            holder.Shopping_Cart_Shop_Name.setText(cursor.getString(ShoppingCartActivity.COL_CART_SHOP_NAME));
            holder.Shopping_Cart_Item_Name.setText(cursor.getString(ShoppingCartActivity.COL_CART_ITEM_NAME));
            holder.Shopping_Cart_Item_Price.setText(cursor.getString(ShoppingCartActivity.COL_CART_ITEM_PRICE));
            picasso.load("http://bubble.zeowls.com/uploads/" + cursor.getString(ShoppingCartActivity.COL_CART_ITEM_IMAGE)).into(holder.Shopping_Cart_Item_Image);
            final int Shop_id = cursor.getInt(ShoppingCartActivity.COL_CART_SHOP_ID);
            holder.Shopping_Cart_Check_Out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingData loadingData = new loadingData();
                    loadingData.setItemID(data.getInt(ShoppingCartActivity.COL_CART_ITEM_ID));
                    loadingData.execute();
                }
            });
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Contract.CartEntry.CONTENT_URI, CART_COLUMNS, null, null, Contract.CartEntry._ID + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCartCount = data.getCount();
        adapter = new CursorContentAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerView.setAdapter(adapter);
    }


    private class loadingData extends AsyncTask {

        int response = 0;

        int ItemID = 0;

        public void setItemID(int itemID) {
            ItemID = itemID;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Object o) {
            if (response == 1) {
                Toast.makeText(getActivity(), "Order Sent Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "please login first", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).mDrawerLayout.openDrawer(GravityCompat.START);
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {

            response = core.makeOrder(ItemID, userId, 1);
            return null;
        }
    }

    private class loadingOrdersData extends AsyncTask {

        String response;


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Object o) {
//            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
            Log.i(TAG, response);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            response = core.getUserOrders(userId).toString();
            return null;
        }
    }


}
