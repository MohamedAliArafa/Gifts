package com.zeowls.gifts.HomePage;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.R;
import com.zeowls.gifts.ShopDetailsPage.Shop_Detail_Fragment;
import com.zeowls.gifts.ShopsTap.ShopDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Provides UI for the view with Cards.
 */
public class ShopsContentFragment extends Fragment {

    static ArrayList<ShopDataModel> shops = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    ContentAdapter adapter;

    Context context;
    Picasso picasso;

    loadingData loadingData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadingData = new loadingData();
        if (loadingData.getStatus() != AsyncTask.Status.RUNNING){
            loadingData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        return inflater.inflate(R.layout.content_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById( R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        adapter = new ContentAdapter();
        context = getContext();
        picasso = Picasso.with(context);
        //recyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        super.onViewCreated(view, savedInstanceState);
    }

    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {
                shops.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setAdapter(adapter);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final TextView name,text;

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

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static final int LENGTH = 18;
        final Shop_Detail_Fragment endFragment = new Shop_Detail_Fragment();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder( final ViewHolder holder, final int position) {

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

                endFragment.setSharedElementEnterTransition(TransitionInflater.from(
                        getActivity()).inflateTransition(R.transition.change_image_trans));
                endFragment.setEnterTransition(TransitionInflater.from(
                        getActivity()).inflateTransition(android.R.transition.fade));
            }

            // no-op
            if (shops.size() != 0) {
                holder.name.setText(shops.get(position).getName());
                holder.text.setText(shops.get(position).getDescription());
                picasso.load(shops.get(position).getPictureUrl()).into(holder.imageView);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putString("TRANS_NAME", imageTransitionName);
                    bundle.putString("TRANS_TEXT", textTransitionName);
                    bundle.putString("ACTION", holder.name.getText().toString());
                    bundle.putParcelable("IMAGE", ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap());
                    endFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    endFragment.setId(shops.get(position).getId());
                    fragmentManager.beginTransaction()
                            .hide(getFragmentManager().findFragmentByTag("homeFragment"))
                            .add(R.id.fragment_main, endFragment)
//                            .replace(R.id.fragment_main, endFragment)
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
        if (loadingData.getStatus() == AsyncTask.Status.RUNNING){
            loadingData.cancel(true);
        }
        super.onDestroy();
    }
}