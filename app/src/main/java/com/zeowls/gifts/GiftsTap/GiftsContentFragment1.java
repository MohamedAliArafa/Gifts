package com.zeowls.gifts.GiftsTap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.SectionedREcycler.SectionedRecyclerViewAdapter;
import com.zeowls.SectionedREcycler.SpacesItemDecoration;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.ItemDetailsPage.ItemDataMode;
import com.zeowls.gifts.ItemDetailsPage.ItemDetailActivity_2;
import com.zeowls.gifts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Provides UI for the view with Cards.
 */
public class GiftsContentFragment1 extends Fragment {

    static ArrayList<ItemDataMode> GiftItems = new ArrayList<>();
    public RecyclerView recyclerView;
    MainAdapter adapter;
    static Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);


        new loadingData().execute();


        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        adapter = new MainAdapter();

        GridLayoutManager manager = new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.grid_span));
        recyclerView.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }


    private class loadingData extends AsyncTask {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            GiftItems.clear();

            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getString(R.string.loading) + "...");
            pDialog.setIndeterminate(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();



        }

        private void hidePDialog() {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }

        @Override
        protected void onPostExecute(Object o) {

            hidePDialog();
            recyclerView.setAdapter(adapter);
            Log.d("Gifts Array", GiftItems.toString());
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getContext());
                JSONObject itemsJSON = core.getItemsByCategoryId(1);
                if (core.getItemsByCategoryId(1) != null && itemsJSON.getJSONArray("Items").length() != 0) {
                    for (int i = 0; i < 4; i++) {
                        JSONArray itemsarray = itemsJSON.getJSONArray("Items");
                        JSONObject item = itemsarray.getJSONObject(i);
                        ItemDataMode Gift_Item = new ItemDataMode();
                        Gift_Item.setId(item.getInt("id"));
                        Gift_Item.setName(item.getString("name"));
                        Gift_Item.setShopName(item.getString("shop_name"));
                        Gift_Item.setDesc(item.getString("description"));
                        Gift_Item.setPrice("$" + item.getString("price"));
                        Gift_Item.setImgUrl(item.getString("image"));

                        GiftItems.add(Gift_Item);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                Core core = new Core(getContext());
                JSONObject itemsJSON = core.getItemsByCategoryId(2);
                if (core.getItemsByCategoryId(2) != null && itemsJSON.getJSONArray("Items").length() != 0) {
                    for (int i = 0; i < 4; i++) {
                        JSONArray itemsarray = itemsJSON.getJSONArray("Items");
                        JSONObject item = itemsarray.getJSONObject(i);
                        ItemDataMode Gift_Item = new ItemDataMode();
                        Gift_Item.setId(item.getInt("id"));
                        Gift_Item.setName(item.getString("name"));
                        Gift_Item.setDesc(item.getString("description"));
                        Gift_Item.setShopName(item.getString("shop_name"));
                        Gift_Item.setPrice("$" + item.getString("price"));
                        Gift_Item.setImgUrl(item.getString("image"));

                        GiftItems.add(Gift_Item);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                Core core = new Core(getContext());
                JSONObject itemsJSON = core.getItemsByCategoryId(3);
                if (core.getItemsByCategoryId(3) != null && itemsJSON.getJSONArray("Items").length() != 0) {
                    for (int i = 0; i < 4; i++) {
                        JSONArray itemsarray = itemsJSON.getJSONArray("Items");
                        JSONObject item = itemsarray.getJSONObject(i);
                        ItemDataMode Gift_Item = new ItemDataMode();
                        Gift_Item.setId(item.getInt("id"));
                        Gift_Item.setName(item.getString("name"));
                        Gift_Item.setDesc(item.getString("description"));
                        Gift_Item.setShopName(item.getString("shop_name"));
                        Gift_Item.setPrice("$" + item.getString("price"));
                        Gift_Item.setImgUrl(item.getString("image"));

                        GiftItems.add(Gift_Item);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                Core core = new Core(getContext());
                JSONObject itemsJSON = core.getItemsByCategoryId(4);
                if (core.getItemsByCategoryId(4) != null && itemsJSON.getJSONArray("Items").length() != 0) {
                    for (int i = 0; i < 4; i++) {
                        JSONArray itemsarray = itemsJSON.getJSONArray("Items");
                        JSONObject item = itemsarray.getJSONObject(i);
                        ItemDataMode Gift_Item = new ItemDataMode();
                        Gift_Item.setId(item.getInt("id"));
                        Gift_Item.setName(item.getString("name"));
                        Gift_Item.setDesc(item.getString("description"));
                        Gift_Item.setPrice("$" + item.getString("price"));
                        Gift_Item.setShopName(item.getString("shop_name"));
                        Gift_Item.setImgUrl(item.getString("image"));

                        GiftItems.add(Gift_Item);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


    public static class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.MainVH> {


        @Override
        public int getSectionCount() {
            if (GiftItems.size() != 0) {
                return GiftItems.size()/4;
            }

            return 0;

        }

        @Override
        public int getItemCount(int section) {
//        if (section % 2 == 0)
//            return 2; // even sections get 4 items
            return 4; // odd get 8
        }

        @Override
        public void onBindHeaderViewHolder(MainVH holder, int section) {
            holder.ShopName.setText(String.format("Section %d", section));

        }

        @Override
        public void onBindViewHolder(MainVH holder, int section, int relativePosition, final int absolutePosition) {
            holder.ShopName.setText(String.format("S:%d, P:%d, A:%d", section, relativePosition, absolutePosition));

            if (GiftItems.size() != 0) {
                Log.d("Araay size", String.valueOf(GiftItems.size()));
                holder.ItemName.setText(GiftItems.get(absolutePosition).getName());
                holder.ShopName.setText(GiftItems.get(absolutePosition).getShopName());
                holder.ItemPrice.setText(String.valueOf(GiftItems.get(absolutePosition).getPrice()));
                ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.card_image);
                Picasso.with(context).load(GiftItems.get(absolutePosition).getImgUrl()).into(imageView);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = v.getContext();
                //    Toast.makeText(context,"id: " + GiftItems.get(absolutePosition).getId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ItemDetailActivity_2.class);
                    intent.putExtra("id", GiftItems.get(absolutePosition).getId());
                    context.startActivity(intent);

                    //Context context = v.getContext();
                    //Toast.makeText(context,"id: " + GiftItems.get(absolutePosition).getId(), Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(context, ItemDetailActivity_2.class);
                    //intent.putExtra("id", GiftItems.get(absolutePosition).getId());
                    //context.startActivity(intent);


                }
            });

        }

        @Override
        public int getItemViewType(int section, int relativePosition, int absolutePosition) {
//        if (section == 1)
//            return 0; // VIEW_TYPE_HEADER is -2, VIEW_TYPE_ITEM is -1. You can return 0 or greater.
            return super.getItemViewType(section, relativePosition, absolutePosition);
        }

        @Override
        public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            int layout;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    layout = R.layout.list_item_header;
                    break;
                case VIEW_TYPE_ITEM:
                    layout = R.layout.list_item_main;
                    break;
                default:
                    layout = R.layout.list_item_main_bold;
                    break;
            }

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(layout, parent, false);

            return new MainVH(v);
        }


        public static class MainVH extends RecyclerView.ViewHolder {


            final TextView ShopName;
            final TextView ItemName;
            final TextView ItemPrice;

            public MainVH(View itemView) {
                super(itemView);

                ShopName = (TextView) itemView.findViewById(R.id.card_Shop_name);
                ItemName = (TextView) itemView.findViewById(R.id.card_Name);
                ItemPrice = (TextView) itemView.findViewById(R.id.share_button);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity_2.class);
                        context.startActivity(intent);
                    }
                });


                ShopName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                     //   Toast.makeText(context, ShopName.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });


                ItemName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                     //   Toast.makeText(context, "Item  name", Toast.LENGTH_SHORT).show();
                    }
                });


                ItemPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                     //   Toast.makeText(context, "item price", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }
    }


}