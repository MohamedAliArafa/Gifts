package com.zeowls.gifts.CategoryPage;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import com.zeowls.SectionedREcycler.SectionedRecyclerViewAdapter;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.ItemDetailsPage.Item_Detail_Fragment;
import com.zeowls.gifts.R;
import com.zeowls.gifts.ShopsTap.ShopDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nora on 3/24/2016.
 */
public class CategoryContentFragment1 extends Fragment {


    static ArrayList<ShopDataModel> categories = new ArrayList<>();
    static ArrayList<ShopDataModel> SubCategoreis = new ArrayList<>();



    public RecyclerView recyclerView;
    MainAdapter2 adapter;

    static int Section_ItemsCount;


    int id = 0;

    Context context;

    loadingData loadingData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        adapter = new MainAdapter2();
        Section_ItemsCount=0;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        loadingData = new loadingData();
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (loadingData.getStatus() != AsyncTask.Status.RUNNING){
            loadingData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    public void setId(int id) {
        this.id = id;
    }


    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {
            categories.clear();
            SubCategoreis.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getContext());
                JSONArray itemsarray = core.getAllCategories().getJSONArray("Category");
                if (itemsarray.length() != 0) {
                    for (int i = 0; i < itemsarray.length(); i++) {
                        JSONObject item = itemsarray.getJSONObject(i);
                        ShopDataModel category = new ShopDataModel();
                        category.setId(item.getInt("id"));
                        category.setName(item.getString("name"));
                        categories.add(category);

                        JSONArray itemsarray2 = core.getSubCategoriesByCatID(category.getId()).getJSONArray("Category");
                        if (itemsarray2.length() != 0) {
                            Log.d("json", itemsarray2.toString());
                            for (int y = 0; y < itemsarray2.length(); y++) {
                                item = itemsarray2.getJSONObject(y);
                                ShopDataModel item1 = new ShopDataModel();
                                item1.setId(item.getInt("id"));
                                item1.setName(item.getString("name"));
                                item1.setParentCatID(item.getInt("parentCat"));
                                SubCategoreis.add(item1);
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

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.category_item_card, parent, false));


            // Adding Snackbar to Action Button inside card
            final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Image is pressed",
                            Snackbar.LENGTH_LONG).show();

//                    Context context =  imageView.getContext();
//                    Log.d("context",context.toString());
//                    context.startActivity(new Intent(context, ItemsByCategoryIdActivity.class));
                }
            });
            TextView textView = (TextView) itemView.findViewById(R.id.name);

            textView.setOnClickListener(new View.OnClickListener() {
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


    public static class MainAdapter2 extends SectionedRecyclerViewAdapter<MainAdapter2.MainVH> {


        @Override
        public int getSectionCount() {
            if (categories.size() != 0) {
                return categories.size();
            }

            return 0;

        }

        @Override
        public int getItemCount(int section) {
//        if (section % 2 == 0)
//            return 2; // even sections get 4 items


            if (categories.size() != 0) {
                for (int y = 0; y < categories.size(); y++) {

                    if (SubCategoreis.size() != 0) {
                        Section_ItemsCount = 0;
                        for (int x = 0; x < SubCategoreis.size(); x++) {

                            if (categories.get(y).getId() == SubCategoreis.get(x).getParentCatID()) {
                                Section_ItemsCount++;

                            }
                        }

                        if (section == y) {
                            return Section_ItemsCount;
                        }
                    }


                }
            }


            return 0;
        }

        @Override
        public void onBindHeaderViewHolder(MainVH holder, int section) {
            holder.Main_Category_Name.setText(String.format("Section %d", section));
            holder.Main_Category_Name.setText(categories.get(section).getName());

        }

        @Override
        public void onBindViewHolder(MainVH holder, int section, int relativePosition, final int absolutePosition) {
            holder.Sub_Category_Item_Name.setText(String.format("S:%d, P:%d, A:%d", section, relativePosition, absolutePosition));


            if (SubCategoreis.size() != 0) {
                Log.d("Araay size", String.valueOf(SubCategoreis.size()));

                holder.Sub_Category_Item_Name.setText(SubCategoreis.get(absolutePosition).getName());

            }

//
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    ItemsByCategoryIdFragment fragment = null;
//                    FragmentManager fragmentManager;
//                    FragmentTransaction fragmentTransaction;
//                    fragmentManager = myContext.getSupportFragmentManager();
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    if (fragment != null) {
//                        fragmentTransaction.remove(fragment);
//                    }
//
//                    fragment = new ItemsByCategoryIdFragment();
//                    fragment.setId(SubCategoreis.get(absolutePosition).getId());
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.replace(R.id.fragment, fragment);
//                    fragmentTransaction.commit();

                    Context context = v.getContext();
//                    Toast.makeText(context,"id: " + GiftItems.get(absolutePosition).getId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ItemsByCategoryIdActivity.class);
                    intent.putExtra("id", SubCategoreis.get(absolutePosition).getId());
                    context.startActivity(intent);


                }
            });
//
//

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
                    layout = R.layout.list_item_header_2;
                    break;
                case VIEW_TYPE_ITEM:
                    layout = R.layout.list_item_main_2;
                    break;
                default:
                    layout = R.layout.list_item_main_bold;
                    break;
            }

            View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new MainVH(v);
        }


        public static class MainVH extends RecyclerView.ViewHolder {


            final ImageView Sub_Category_Item_Image;
            final TextView Sub_Category_Item_Name;
            final TextView Main_Category_Name;


            public MainVH(View itemView) {
                super(itemView);

                Sub_Category_Item_Image = (ImageView) itemView.findViewById(R.id.Sub_Category_Item_Image);
                Sub_Category_Item_Name = (TextView) itemView.findViewById(R.id.Sub_Category_Item_Name);
                Main_Category_Name = (TextView) itemView.findViewById(R.id.Main_Category_Name);


//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//
//
////                        Context context = v.getContext();
////                        Intent intent = new Intent(context, ItemDetailActivity_2.class);
////                        context.startActivity(intent);
//
//                    }
//                });

//
//                Sub_Category_Item_Image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Context context = v.getContext();
//                        //Toast.makeText(context, Sub_Category_Item_Name.getText().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//                Sub_Category_Item_Name.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Context context = v.getContext();
//                       // Toast.makeText(context, Sub_Category_Item_Name.getText().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });


            }
        }
    }

    @Override
    public void onPause() {
        if (loadingData.getStatus() == AsyncTask.Status.RUNNING){
            loadingData.cancel(true);
        }
        super.onPause();
    }

}
