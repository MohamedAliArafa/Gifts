package com.zeowls.gifts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeowls.SectionedREcycler.MainAdapter;
import com.zeowls.SectionedREcycler.SpacesItemDecoration;

/**
 * Provides UI for the view with Cards.
 */
public class GiftsContentFragment1 extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        MainAdapter adapter = new MainAdapter();

        GridLayoutManager manager = new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.grid_span));
        recyclerView.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);



        //ContentAdapter adapter = new ContentAdapter();
        //recyclerView.setAdapter(adapter);

    //    recyclerView.setHasFixedSize(true);
    //    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

 //       LinearLayoutManager llm = new LinearLayoutManager(getActivity());
     //  llm.setOrientation(LinearLayoutManager.VERTICAL);
     //   recyclerView.setLayoutManager(llm);

       // recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        return recyclerView;
    }



//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//
//        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.item_card, parent, false));
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, ItemDetailActivity.class);
//                    context.startActivity(intent);
//                }
//            });
//
//            // Adding Snackbar to Action Button inside card
//            Button button = (Button)itemView.findViewById(R.id.action_button);
//            button.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    Snackbar.make(v, "Action is pressed",
//                            Snackbar.LENGTH_LONG).show();
//                }
//            });
//
//            ImageButton favoriteImageButton =
//                    (ImageButton) itemView.findViewById(R.id.favorite_button);
//            favoriteImageButton.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    Snackbar.make(v, "Added to Favorite",
//                            Snackbar.LENGTH_LONG).show();
//                }
//            });
//
//            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
//            shareImageButton.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    Snackbar.make(v, "Share article",
//                            Snackbar.LENGTH_LONG).show();
//                }
//            });
//        }
//
//
//    }

    /**
     * Adapter to display recycler view.
     */
//    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
//        // Set numbers of Card in RecyclerView.
//        private static final int LENGTH = 1;
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            // no-op
//        }
//
//        @Override
//        public int getItemCount() {
//            return LENGTH;
//        }
//    }
}




