package com.zeowls.gifts.ShopsTap;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;
import com.zeowls.gifts.ShopDetailsPage.Shop_Detail_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Provides UI for the view with Cards.
 */
public class ShopsContentFragment extends Fragment {

    static ArrayList<ShopDataModel> shops = new ArrayList<>();
    RecyclerView recyclerView;
    ContentAdapter adapter;
    ImageView imageView;
    static Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        adapter = new ContentAdapter();

        new loadingData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {
                shops.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getContext());
                JSONArray itemsarray = core.getAllShops().getJSONArray("Shop");
                if ( itemsarray.length() != 0 ){
                    for (int i = 0; i < itemsarray.length(); i++){
                        JSONObject item = itemsarray.getJSONObject(i);
                        ShopDataModel shop = new ShopDataModel();
                        shop.setId(item.getInt("id"));
                        shop.setName(item.getString("name"));
                        if(item.getString("description").equals("null"))
                            shop.setDescription("");
                        else
                            shop.setDescription(item.getString("description"));
                        shop.setOwner(item.getString("owner"));
                        shop.setPictureUrl(item.getString("profile_pic"));
                        shops.add(shop);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_card, parent, false));


            // Adding Snackbar to Action Button inside card
            Button button = (Button) itemView.findViewById(R.id.action_button);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Action is pressed",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            ImageButton favoriteImageButton =
                    (ImageButton) itemView.findViewById(R.id.favorite_button);
            favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Added to Favorite",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
            shareImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Share article",
                            Snackbar.LENGTH_LONG).show();
                }
            });


        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static final int LENGTH = 18;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // no-op
            if (shops.size() != 0) {
                TextView name = (TextView) holder.itemView.findViewById(R.id.card_title);
                TextView text = (TextView) holder.itemView.findViewById(R.id.card_text);
                ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.card_image);
                name.setText(shops.get(position).getName());
                text.setText(shops.get(position).getDescription());
                Picasso.with(context).load(shops.get(position).getPictureUrl()).resize(500, 500).into(imageView);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                  //  Toast.makeText(context,"id: " + shops.get(position).getId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, Shop_Detail_Activity.class);
                    intent.putExtra("id", shops.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return shops.size();
        }
    }
}