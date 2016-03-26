package com.zeowls.SectionedREcycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zeowls.gifts.ItemDetailsPage.ItemDetailActivity_2;
import com.zeowls.gifts.R;


/**
 * @author Aidan Follestad (afollestad)
 */
public class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.MainVH> {


    @Override
    public int getSectionCount() {
        return 4;
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
    public void onBindViewHolder(MainVH holder, int section, int relativePosition, int absolutePosition) {
        holder.ShopName.setText(String.format("S:%d, P:%d, A:%d", section, relativePosition, absolutePosition));
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

            ShopName = (TextView) itemView.findViewById(R.id.title);
            ItemName = (TextView) itemView.findViewById(R.id.card_text);
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
                    Toast.makeText(context, ShopName.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });



            ItemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Toast.makeText(context, "Item  name", Toast.LENGTH_SHORT).show();
                }
            });



            ItemPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Toast.makeText(context, "item price", Toast.LENGTH_SHORT).show();
                }
            });




        }
    }
}