package com.zeowls.gifts.CategoryPage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.ItemDetailsPage.ItemDataMode;
import com.zeowls.gifts.R;
import com.zeowls.gifts.ShopsTap.ShopDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nora on 3/26/2016.
 */
public class ItemsByCategoryIdFragment extends Fragment{
    static ArrayList<ItemDataMode> items = new ArrayList<>();
    RecyclerView recyclerView;
    ContentAdapter adapter;

    int id =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        adapter = new ContentAdapter();

        new loadingData().execute();

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return recyclerView;
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
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getContext());
                JSONObject itemsJSON = core.getItemsByCategoryId(id);
                if (core.getItemsByCategoryId(id) != null &&  itemsJSON.getJSONArray("Items").length() != 0 ){
                    Log.d("json",core.getItemsByCategoryId(id).toString());
                    for (int i = 0; i < itemsJSON.getJSONArray("Items").length(); i++){
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

                        items.add(item1);
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
            super(inflater.inflate(R.layout.category_item_card, parent, false));


            // Adding Snackbar to Action Button inside card
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Image is pressed",
                            Snackbar.LENGTH_LONG).show();
                }
            });
            TextView textView = (TextView) itemView.findViewById(R.id.textView);

            textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Text is pressed",
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
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // no-op
            if (items.size() != 0) {
                ImageView image = (ImageView) holder.itemView.findViewById(R.id.imageView);
                TextView name = (TextView) holder.itemView.findViewById(R.id.textView);
                //TextView text = (TextView) holder.itemView.findViewById(R.id.card_text);
                name.setText(items.get(position).getName());
                //text.setText(shops.get(position).getDescription());
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
