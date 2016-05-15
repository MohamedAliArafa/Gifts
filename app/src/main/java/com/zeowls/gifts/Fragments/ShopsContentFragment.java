package com.zeowls.gifts.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;
import com.zeowls.gifts.Models.ShopDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.zeowls.gifts.provider.Contract.ShopEntry;
import com.zeowls.gifts.views.adapters.CursorRecyclerViewAdapter;

public class ShopsContentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final int COL_SHOP_ID = 0;
    static final int COL_SHOP_UID = 1;
    static final int COL_SHOP_FAV = 2;
    static final int COL_SHOP_TITLE = 3;
    static final int COL_SHOP_IMAGE = 4;
    static final int COL_SHOP_DESCRIPTION = 5;
    static final int COL_SHOP_EMAIL = 6;
    static final int COL_SHOP_MOBILE = 7;
    static final int COL_SHOP_ADDRESS = 9;
    static final int COL_SHOP_OWNER_NAME = 10;

    private static final String[] SHOP_COLUMNS = {
            ShopEntry.TABLE_NAME + "." + ShopEntry._ID,
            ShopEntry.COLUMN_ID,
            ShopEntry.COLUMN_FAV,
            ShopEntry.COLUMN_NAME,
            ShopEntry.COLUMN_PROFILE_PIC,
            ShopEntry.COLUMN_DESCRIPTION,
            ShopEntry.COLUMN_EMAIL,
            ShopEntry.COLUMN_MOBILE,
            ShopEntry.COLUMN_SHORT_DESCRIPTION,
            ShopEntry.COLUMN_ADDRESS,
            ShopEntry.COLUMN_OWNER_NAME,
            ShopEntry.COLUMN_COVER_PIC
    };

    private int mLoader;
    private int SHOPS_LOADER = 0;

    static ArrayList<ShopDataModel> shops = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    ContentAdapter adapter;

    Context context;
    Picasso picasso;

    loadingData loadingData;
    LinearLayout mErrorText;

    @Override
    public void onStart() {
        loadingData = new loadingData();
        if (loadingData.getStatus() != AsyncTask.Status.RUNNING) {
            loadingData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getLoaderManager().initLoader(SHOPS_LOADER, null, this);
        shops.clear();
        return inflater.inflate(R.layout.content_fragment_shops, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_shops);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mErrorText = (LinearLayout) view.findViewById(R.id.error);

//        adapter = new ContentAdapter(getActivity(), null);
        adapter = new ContentAdapter();
        context = getContext();
        picasso = Picasso.with(context);
        //recyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(SHOPS_LOADER, null, this);
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShopEntry.CONTENT_URI, SHOP_COLUMNS, null, null, ShopEntry._ID + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        adapter.swapCursor(data);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        adapter.swapCursor(null);
        mRecyclerView.setAdapter(adapter);
    }

    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {
            shops.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (shops.size() != 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setAdapter(adapter);
            } else {
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setVisibility(View.VISIBLE);
//                mErrorText.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getActivity());
                JSONArray itemsarray = core.getAllShops().getJSONArray("Shop");
                if (itemsarray.length() != 0) {
                    for (int i = 0; i < itemsarray.length(); i++) {
                        JSONObject item = itemsarray.getJSONObject(i);
                        ShopDataModel shop = new ShopDataModel();
                        shop.setId(item.getInt("id"));
                        shop.setName(item.getString("name"));
                        if (item.getString("description").equals("null") || item.getString("description").isEmpty())
                            shop.setDescription("No Description Available");
                        else
                            shop.setDescription(item.getString("description"));
//                        shop.setOwner(item.getString("owner"));
                        if (!item.getString("profile_pic").equals("null")) {
                            shop.setPictureUrl(item.getString("profile_pic"));
                        }
                        //shop.setOwner(item.getString("owner"));

                        if (!item.isNull("profile_pic")) {
                            shop.setPictureUrl(item.getString("profile_pic"));
                        } else {
                            shop.setPictureUrl("null");
                        }

                        if (!item.isNull("description")) {
                            shop.setDescription(item.getString("description"));
                        } else {
                            shop.setDescription("");
                        }


                        shops.add(shop);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final TextView name, text;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.list_title);
            text = (TextView) view.findViewById(R.id.list_desc);
            imageView = (ImageView) view.findViewById(R.id.list_avatar);
        }
    }

    /**
     * Adapter to display recycler view.
     */

//    public class ContentAdapter extends CursorRecyclerViewAdapter<ContentAdapter.ViewHolder> {
//        // Set numbers of Card in RecyclerView.
//        private static final int LENGTH = 18;
//        final Shop_Detail_Fragment endFragment = new Shop_Detail_Fragment();
//        final Shop_Detail_Fragment_2 endFragment2 = new Shop_Detail_Fragment_2();
//        final Shop_Detail_Fragment_3 endFragment3 = new Shop_Detail_Fragment_3();
//
//        public ContentAdapter(Context context, Cursor cursor) {
//            super(context, cursor);
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            ImageView imageView;
//            TextView name, text;
//
//            public ViewHolder(View view) {
//                super(view);
//                name = (TextView) view.findViewById(R.id.list_title);
//                text = (TextView) view.findViewById(R.id.list_desc);
//                imageView = (ImageView) view.findViewById(R.id.list_avatar);
//            }
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            context = parent.getContext();
//            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {
//
//            holder.name.setText(cursor.getString(ShopsContentFragment.COL_SHOP_TITLE));
//
//            final int id = cursor.getInt(ShopsContentFragment.COL_SHOP_UID);
//
//            final String imageTransitionName = "transition" + cursor.getPosition();
//            final String textTransitionName = "transtext" + cursor.getPosition();
//            final Bundle bundle = new Bundle();
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                holder.name.setTransitionName(textTransitionName);
//                holder.imageView.setTransitionName(imageTransitionName);
//                setSharedElementReturnTransition(TransitionInflater.from(
//                        getActivity()).inflateTransition(R.transition.change_image_trans));
//                setExitTransition(TransitionInflater.from(
//                        getActivity()).inflateTransition(android.R.transition.fade));
//
//                endFragment3.setSharedElementEnterTransition(TransitionInflater.from(
//                        getActivity()).inflateTransition(R.transition.change_image_trans));
//                endFragment3.setEnterTransition(TransitionInflater.from(
//                        getActivity()).inflateTransition(android.R.transition.fade));
//            }
//
//            // no-op
////            if (shops.size() != 0) {
//            holder.name.setText(cursor.getString(ShopsContentFragment.COL_SHOP_TITLE));
//            holder.text.setText(cursor.getString(ShopsContentFragment.COL_SHOP_DESCRIPTION));
//            if (cursor.getString(ShopsContentFragment.COL_SHOP_IMAGE) != null) {
//                picasso.load(cursor.getString(ShopsContentFragment.COL_SHOP_IMAGE)).into(holder.imageView);
//            }
//            if (cursor.getString(ShopsContentFragment.COL_SHOP_IMAGE).equals("http://bubble.zeowls.com/uploads/null")) {
//                holder.imageView.setImageResource(R.drawable.bubble_logo);
//            } else {
//                picasso.load(cursor.getString(ShopsContentFragment.COL_SHOP_IMAGE)).into(holder.imageView);
//            }
//
////            }
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    bundle.putString("TRANS_NAME", imageTransitionName);
//                    bundle.putString("TRANS_TEXT", textTransitionName);
//
//                    bundle.putString("TRANS_MOBILE", cursor.getString(ShopsContentFragment.COL_SHOP_MOBILE));
//                    bundle.putString("TRANS_EMAIL", cursor.getString(ShopsContentFragment.COL_SHOP_EMAIL));
//                    bundle.putString("TRANS_ADDRESS", cursor.getString(ShopsContentFragment.COL_SHOP_ADDRESS));
//                    bundle.putString("TRANS_OWNER_NAME", cursor.getString(ShopsContentFragment.COL_SHOP_OWNER_NAME));
//
//                    bundle.putString("ACTION", holder.name.getText().toString());
//                    try {
//                        bundle.putParcelable("IMAGE", ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    endFragment3.setArguments(bundle);
//                    FragmentManager fragmentManager = getFragmentManager();
//                    endFragment3.setId(id);
//                    fragmentManager.beginTransaction()
//                            .hide(getFragmentManager().findFragmentByTag("homeFragment"))
//                            .add(R.id.fragment_main, endFragment3)
//                            .addToBackStack(null)
//                            .addSharedElement(holder.imageView, imageTransitionName)
//                            .addSharedElement(holder.name, textTransitionName)
//                            .commit();
//                }
//            });
//        }
//    }

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static final int LENGTH = 18;
        final Shop_Detail_Fragment_3 endFragment3 = new Shop_Detail_Fragment_3();



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final String imageTransitionName = "transition" + position;
            final String textTransitionName = "transtext" + position;
            final Bundle bundle = new Bundle();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.name.setTransitionName(textTransitionName);
                holder.imageView.setTransitionName(imageTransitionName);
                setSharedElementReturnTransition(TransitionInflater.from(
                        getActivity()).inflateTransition(R.transition.change_image_trans));
                setExitTransition(TransitionInflater.from(
                        getActivity()).inflateTransition(android.R.transition.fade));

                endFragment3.setSharedElementEnterTransition(TransitionInflater.from(
                        getActivity()).inflateTransition(R.transition.change_image_trans));
                endFragment3.setEnterTransition(TransitionInflater.from(
                        getActivity()).inflateTransition(android.R.transition.fade));
            }

            // no-op
            if (shops.size() != 0) {
                holder.name.setText(shops.get(position).getName());
                holder.text.setText(shops.get(position).getDescription());
                if (shops.get(position).getPictureUrl() != null) {
                    picasso.load(shops.get(position).getPictureUrl()).into(holder.imageView);
                }
                if (shops.get(position).getPictureUrl().equals("http://bubble.zeowls.com/uploads/null")) {
                    holder.imageView.setImageResource(R.drawable.bubble_logo);
                } else {
                    picasso.load(shops.get(position).getPictureUrl()).into(holder.imageView);
                }

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putString("TRANS_NAME", imageTransitionName);
                    bundle.putString("TRANS_TEXT", textTransitionName);
                    bundle.putString("ACTION", holder.name.getText().toString());
                    try {
                        bundle.putParcelable("IMAGE", ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap());
                        endFragment3.setArguments(bundle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FragmentManager fragmentManager = getFragmentManager();
                    endFragment3.setId(shops.get(position).getId());
                    fragmentManager.beginTransaction()
                            .hide(getFragmentManager().findFragmentByTag("homeFragment"))
                            .add(R.id.fragment_main, endFragment3)
                            .addToBackStack(null)
                            .addSharedElement(holder.imageView, imageTransitionName)
                            .addSharedElement(holder.name, textTransitionName)
                            .commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return shops.size();
        }
    }

    @Override
    public void onDestroy() {
        if (loadingData.getStatus() == AsyncTask.Status.RUNNING) {
            loadingData.cancel(true);
        }
        super.onDestroy();
    }
}