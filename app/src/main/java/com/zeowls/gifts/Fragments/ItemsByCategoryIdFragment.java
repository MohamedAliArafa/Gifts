package com.zeowls.gifts.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.Models.ItemDataMode;
import com.zeowls.gifts.R;
import com.zeowls.gifts.views.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemsByCategoryIdFragment extends Fragment {
    static ArrayList<ItemDataMode> items = new ArrayList<>();
    RecyclerView recyclerView;
    ContentAdapter adapter;
    Context context;
    int id = 0;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayout mErrorText;

    protected FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        new loadingData().execute();

        return inflater.inflate(R.layout.content_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ContentAdapter();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mErrorText = (LinearLayout) view.findViewById(R.id.error);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setId(int id) {
        this.id = id;
    }

    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {
            items.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
//            recyclerView.setAdapter(adapter);
            if (items.size() != 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setAdapter(adapter);
            } else {
                mErrorText.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getContext());
                JSONObject itemsJSON = core.getItemsByCategoryId(id);
                if (itemsJSON != null && itemsJSON.getJSONArray("Items").length() != 0) {
                    Log.d("json", itemsJSON.toString());
                    for (int i = 0; i < itemsJSON.getJSONArray("Items").length(); i++) {
                        JSONArray itemsarray = itemsJSON.getJSONArray("Items");
                        JSONObject item = itemsarray.getJSONObject(i);
                        ItemDataMode item1 = new ItemDataMode();
                        item1.setCatId(item.getInt("cat_id"));
                        item1.setDesc(item.getString("description"));
                        item1.setId(item.getInt("id"));
                        item1.setName(item.getString("name"));
                        item1.setPrice(item.getString("price"));
                        item1.setShopId(item.getInt("shop_id"));
                        item1.setShortDesc(item.getString("short_description"));
                        item1.setImgUrl(item.getString("image"));
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
            super(inflater.inflate(R.layout.category_item_card, parent, false));

        }
    }


    /**
     * Adapter to display recycler view.
     */
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        Picasso picasso;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            picasso = Picasso.with(context);
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // no-op
            if (items.size() != 0) {
                ImageView image = (ImageView) holder.itemView.findViewById(R.id.imageView);
                TextView name = (TextView) holder.itemView.findViewById(R.id.name);
                TextView desc = (TextView) holder.itemView.findViewById(R.id.description);
                TextView price = (TextView) holder.itemView.findViewById(R.id.price);
                name.setText(items.get(position).getName());
                desc.setText(items.get(position).getDesc());
                price.setText("$" + items.get(position).getPrice());
                Log.d("Imagelist : ",items.get(position).getImgUrl());
                if (items.get(position).getImgUrl().equals("http://bubble.zeowls.com/uploads/")) {
                    image.setImageResource(R.drawable.bubble_logo);
                } else {
                    picasso.load(items.get(position).getImgUrl()).into(image);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String backStateName = this.getClass().getName();
                    FragmentManager fragmentManager = getFragmentManager();
                    ItemDetailFragment endFragment = new ItemDetailFragment();
                    endFragment.setId(items.get(position).getId());
                    endFragment.setShopId(items.get(position).getShopId());
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_main, endFragment)
                            .addToBackStack(backStateName)
                            .commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }


}
