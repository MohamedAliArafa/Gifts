package com.zeowls.gifts.Fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.zeowls.gifts.views.adapters.SectionedRecyclerViewAdapter;
import com.zeowls.gifts.views.SpacesItemDecoration;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;
import com.zeowls.gifts.Models.ShopDataModel;
import com.zeowls.gifts.provider.Contract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryContentFragment1 extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final int COL_CAT_ID = 0;
    static final int COL_CAT_UID = 1;
    static final int COL_CAT_NAME = 2;
    static final int COL_CAT_PARENT_ID = 2;

    private static final String[] SUB_CAT_COLUMNS = {
            Contract.CategoryEntry.TABLE_NAME + "." + Contract.CategoryEntry._ID,
            Contract.CategoryEntry.COLUMN_ID,
            Contract.CategoryEntry.COLUMN_NAME,
            Contract.CategoryEntry.COLUMN_PARENT_ID
    };

    static final int COL_SUB_CAT_ID = 0;
    static final int COL_SUB_CAT_UID = 1;
    static final int COL_SUB_CAT_NAME = 2;
    static final int COL_SUB_CAT_PARENT_ID = 3;

    private static final String[] CAT_COLUMNS = {
            Contract.ParentCategoryEntry.TABLE_NAME + "." + Contract.ParentCategoryEntry._ID,
            Contract.ParentCategoryEntry.COLUMN_ID,
            Contract.ParentCategoryEntry.COLUMN_NAME
    };

    private int CAT_LOADER = 0;
    private int SUB_CAT_LOADER = 1;

    static ArrayList<ShopDataModel> categories = new ArrayList<>();
    static ArrayList<ShopDataModel> SubCategoreis = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayout mErrorText;


    MainAdapter2 adapter;
    int Section_ItemsCount;
    loadingData loadingData;

    @Override
    public void onStart() {
        loadingData = new loadingData();
        if (loadingData.getStatus() != AsyncTask.Status.RUNNING) {
            loadingData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getLoaderManager().initLoader(CAT_LOADER, null, this);
        getLoaderManager().initLoader(SUB_CAT_LOADER, null, this);
        categories.clear();
        SubCategoreis.clear();
        return inflater.inflate(R.layout.content_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mErrorText = (LinearLayout) view.findViewById(R.id.error);
        adapter = new MainAdapter2();
        Section_ItemsCount = 0;
        int i = 0;
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(i));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {
            categories.clear();
            SubCategoreis.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (categories.size() != 0) {
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


    /**
     * Adapter to display recycler view.
     */


    public class MainAdapter2 extends SectionedRecyclerViewAdapter<MainAdapter2.MainVH> {

        final ItemsByCategoryIdFragment endFragment = new ItemsByCategoryIdFragment();

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
            } else {
                holder.Sub_Category_Item_Name.setText(SubCategoreis.get(absolutePosition).getName());
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FragmentManager fragmentManager = getFragmentManager();
                    endFragment.setId(SubCategoreis.get(absolutePosition).getId());
                    fragmentManager.beginTransaction()
                            .hide(getFragmentManager().findFragmentByTag("homeFragment"))
                            .add(R.id.fragment_main, endFragment)
                            .addToBackStack(null)
                            .commit();


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


        public class MainVH extends RecyclerView.ViewHolder {


            final ImageView Sub_Category_Item_Image;
            final TextView Sub_Category_Item_Name;
            final TextView Main_Category_Name;


            public MainVH(View itemView) {
                super(itemView);
                Sub_Category_Item_Image = (ImageView) itemView.findViewById(R.id.Sub_Category_Item_Image);
                Sub_Category_Item_Name = (TextView) itemView.findViewById(R.id.Sub_Category_Item_Name);
                Main_Category_Name = (TextView) itemView.findViewById(R.id.Main_Category_Name);
            }
        }
    }


    @Override
    public void onPause() {
        if (loadingData.getStatus() == AsyncTask.Status.RUNNING) {
            loadingData.cancel(true);
        }
        super.onPause();
    }

}
