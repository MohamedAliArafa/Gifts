package com.zeowls.gifts.Fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.Models.ItemDataMode;
import com.zeowls.gifts.Models.orderDataModel;
import com.zeowls.gifts.R;
import com.zeowls.gifts.Utility.PrefUtils;
import com.zeowls.gifts.views.adapters.CursorRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {

    private static final String TAG = "ShoppingCart";
    int userId = 0;
    RecyclerView recyclerView;
    Picasso picasso;
    static ArrayList<orderDataModel> OrdersArray = new ArrayList<>();
    ContentAdapter adapter;


    public OrdersFragment() {
        // Required empty public constructor
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new loadingOrdersData().execute();
        picasso = Picasso.with(getActivity());

        try {
            userId = PrefUtils.getCurrentUser(getActivity()).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.shopping_cart_recycler_view);
    }


    public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

        ImageView Shopping_Cart_Item_Image, Shopping_Cart_Shop_Image;
        TextView Shopping_Cart_Shop_Name, Shopping_Cart_Item_Name, Shopping_Cart_Item_Price,
                Shopping_Cart_Item_Quantity, Shopping_Cart_Order_Status, Shopping_Cart_Item_Address;
        Button Shopping_Cart_Check_Out;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
                Shopping_Cart_Shop_Name = (TextView) view.findViewById(R.id.Shopping_Cart_Shop_Name);
                Shopping_Cart_Item_Address = (TextView) view.findViewById(R.id.Shopping_Cart_Item_Address);
                Shopping_Cart_Item_Name = (TextView) view.findViewById(R.id.Shopping_Cart_Item_Name);
                Shopping_Cart_Item_Quantity = (TextView) view.findViewById(R.id.Shopping_Cart_Item_Qty);
                Shopping_Cart_Order_Status = (TextView) view.findViewById(R.id.Shopping_Cart_Order_Status);
                Shopping_Cart_Item_Price = (TextView) view.findViewById(R.id.Shopping_Cart_Item_Price);
                Shopping_Cart_Check_Out = (Button) view.findViewById(R.id.Shopping_Cart_Check_Out);
                Shopping_Cart_Item_Image = (ImageView) view.findViewById(R.id.Shopping_Cart_Item_Image);
                Shopping_Cart_Shop_Image = (ImageView) view.findViewById(R.id.imageView);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycler_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (OrdersArray.size() != 0) {
                orderDataModel order = OrdersArray.get(position);
                Shopping_Cart_Shop_Name.setText(order.getShop_name());
                Shopping_Cart_Item_Name.setText(order.getItem_name());
                Shopping_Cart_Item_Price.setText("$" + order.getItem_price());
                Shopping_Cart_Item_Quantity.setText(order.getItem_quantity());
                Shopping_Cart_Item_Address.setText(order.getShipping_address());
                switch (order.getStatus()) {
                    case 0:
                        Shopping_Cart_Order_Status.setText("Pending");
                        break;
                    case 1:
                        Shopping_Cart_Order_Status.setText("Confirmed by User");
                        break;
                    case 2:
                        Shopping_Cart_Order_Status.setText("Confirmed by shop");
                        break;
                    case 3:
                        Shopping_Cart_Order_Status.setText("On Delivery");
                        break;
                    case 4:
                        Shopping_Cart_Order_Status.setText("Received by User");
                        break;
                    default:
                        Shopping_Cart_Order_Status.setText("Unknown");
                }
                picasso.load("http://bubble.zeowls.com/uploads/" + order.getItem_image()).into(Shopping_Cart_Item_Image);
                picasso.load("http://bubble.zeowls.com/uploads/" + order.getShop_image()).into(Shopping_Cart_Shop_Image);
            }
        }

        @Override
        public int getItemCount() {
            return OrdersArray.size();
        }
    }

    private class loadingOrdersData extends AsyncTask {

        JSONObject response;
        Core core;

        @Override
        protected void onPreExecute() {
            core = new Core(getActivity());
            OrdersArray.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.i(TAG, response.toString());
            try {
                if (response != null) {
                    JSONArray jsonArray = response.getJSONArray("orders");
                    orderDataModel order;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        order = new orderDataModel();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        order.setItem_id(jsonObject.getInt("id"));
                        order.setItem_name(jsonObject.getString("item_name"));
                        order.setShop_name(jsonObject.getString("shop_name"));
                        order.setShop_image(jsonObject.getString("shop_image"));
                        order.setItem_price(jsonObject.getString("item_price"));
                        order.setItem_image(jsonObject.getString("item_image"));
                        order.setItem_quantity(jsonObject.getString("item_quantity"));
                        order.setShipping_address(jsonObject.getString("shipping_address"));
                        order.setStatus(jsonObject.getInt("order_status"));
                        OrdersArray.add(order);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new ContentAdapter();
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                response = core.getUserOrders(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
