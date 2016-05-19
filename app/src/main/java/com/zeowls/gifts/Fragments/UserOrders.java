package com.zeowls.gifts.Fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.Models.ItemDataMode;
import com.zeowls.gifts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nezar on 5/19/16.
 */
public class UserOrders  extends Fragment {

    static ArrayList<ItemDataMode> items = new ArrayList<>();
    RecyclerView recyclerView;
    ContentAdapter adapter;

    int user_Id = 0;

    protected FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        adapter = new ContentAdapter();

        new loadingData().execute();

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return recyclerView;
//        return inflater.inflate(R.layout.fragment_shop_all_items, container, false);
    }

    public void set_User_Id(int id) {
        this.user_Id = id;
    }

    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {
            items.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getContext());
                JSONObject itemsJSON = core.getUserOrdersByUserID(user_Id);
                if (itemsJSON != null && itemsJSON.getJSONArray("Items").length() != 0) {
                    Log.d("json", itemsJSON.toString());
                    for (int i = 0; i < itemsJSON.getJSONArray("Items").length(); i++) {
                        JSONArray itemsarray = itemsJSON.getJSONArray("Items");
                        JSONObject item = itemsarray.getJSONObject(i);
                        ItemDataMode item1 = new ItemDataMode();
                        item1.setCatId(item.getInt("cat_id"));

                        items.add(item1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.user_order_item_card, parent, false));

        }
    }

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static final int LENGTH = 18;
        ItemDetailFragment fragment;
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // no-op
            if (items.size() != 0) {
                ImageView user_order_header_Image = (ImageView) holder.itemView.findViewById(R.id.user_order_header_Image);
                TextView user_order_header_Item_name = (TextView) holder.itemView.findViewById(R.id.user_order_header_Item_name);
                TextView description = (TextView) holder.itemView.findViewById(R.id.user_order_Shop_name);
                TextView user_order_Shop_name = (TextView) holder.itemView.findViewById(R.id.user_order_item_Qty);
                TextView user_order_item_Price = (TextView) holder.itemView.findViewById(R.id.user_order_item_Price);
                TextView user_order_shipping_time = (TextView) holder.itemView.findViewById(R.id.user_order_shipping_time);

                if (items.get(position).getImgUrl().equals("http://bubble.zeowls.com/uploads/")) {
                    user_order_header_Image.setImageResource(R.drawable.bubble_logo);
                } else {
                    Picasso.with(getContext()).load(items.get(position).getImgUrl()).into(user_order_header_Image);
                }
                //text.setText(shops.get(position).getDescription());
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager = myContext.getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
//                    if (fragment != null) {
//                        fragmentTransaction.remove(fragment);
//                    }
                    fragment = new ItemDetailFragment();
                    fragment.setId(items.get(position).getId());
                    fragment.setShopId(items.get(position).getShopId());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.fragment_main, fragment, "ItemDetailFragment");
                    fragmentTransaction.commit();
//                    Context context = v.getContext();
//                    Toast.makeText(context, "id: " + shops.get(position).getId(), Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(context, ItemDetailActivity.class);
//                    intent.putExtra("id", shops.get(position).getId());
//                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }


}
