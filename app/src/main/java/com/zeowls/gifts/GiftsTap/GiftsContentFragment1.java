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
import android.widget.Toast;

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
    static ArrayList<ItemDataMode> Category = new ArrayList<>();
    public RecyclerView recyclerView;
    MainAdapter adapter;
    static Context context;
    private Picasso picasso;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        context = getContext();
        new loadingData().execute();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        picasso = Picasso.with(context);
        adapter = new MainAdapter();

        GridLayoutManager manager = new GridLayoutManager(getActivity(),getResources().getInteger(R.integer.grid_span));
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
            pDialog = new ProgressDialog(context);
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
                Core core = new Core(context);
                JSONArray catarray = core.getAllCategories().getJSONArray("Category");
                for (int y = 0; y < catarray.length() ; y++){
                    ItemDataMode mainCategory = new ItemDataMode();
                    mainCategory.setId(catarray.getJSONObject(y).getInt("id"));
                    JSONArray subCatArray = core.getSubCategoriesByCatID(mainCategory.getId()).getJSONArray("Category");
                    for (int z = 0; z < subCatArray.length(); z++) {
                        ItemDataMode category = new ItemDataMode();
                        category.setName(subCatArray.getJSONObject(z).getString("name"));
                        category.setId(subCatArray.getJSONObject(z).getInt("id"));
                        JSONArray itemsarray = core.getItemsByCategoryId(category.getId()).getJSONArray("Items");
                        if (itemsarray.length() != 0) {
                            Category.add(category);
                            for (int i = 0; i < 4; i++) {
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
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.MainVH> {



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
//            holder.ItemName.setText(String.format("Section %d", section));
            holder.ItemName.setText(Category.get(section).getName());
        }

        @Override
        public void onBindViewHolder(MainVH holder, int section, int relativePosition, final int absolutePosition) {
            holder.ItemName.setText(String.format("S:%d, P:%d, A:%d", section, relativePosition, absolutePosition));

            if (GiftItems.size() != 0) {
                Log.d("Array size", String.valueOf(GiftItems.size()));
                holder.ItemName.setText(GiftItems.get(absolutePosition).getName());
                holder.ShopName.setText(GiftItems.get(absolutePosition).getShopName());
                holder.ItemPrice.setText(String.valueOf(GiftItems.get(absolutePosition).getPrice()));
                ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.card_image);
                picasso.load(GiftItems.get(absolutePosition).getImgUrl()).fit().centerCrop().into(imageView);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(context,"id: " + GiftItems.get(absolutePosition).getId(), Toast.LENGTH_SHORT).show();
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

            View v = LayoutInflater.from(context).inflate(layout, parent, false);

            return new MainVH(v);
        }

        public class MainVH extends RecyclerView.ViewHolder {


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
//                        Intent intent = new Intent(context, ItemDetailActivity_2.class);
//                        context.startActivity(intent);
                    }
                });


                ShopName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, ShopName.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });


                ItemName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, ItemName.getText(), Toast.LENGTH_SHORT).show();
                    }
                });


                ItemPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   Toast.makeText(context, "item price", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }
    }
}