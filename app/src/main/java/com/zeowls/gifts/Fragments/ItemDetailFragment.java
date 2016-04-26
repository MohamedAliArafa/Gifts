package com.zeowls.gifts.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.BackEndOwl.FireOwl;
import com.zeowls.gifts.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemDetailFragment extends Fragment {

    int item_id = 0;
    int shop_id = 0;
    int user_id = 0;

    TextView name, description, price, itemNameToolbar, shopName;
    Button visitShop, addToCart;
    ImageView itemPic;

    String item_name,item_price,item_image,item_desc,shop_name_txt;

    Shop_Detail_Fragment endFragment;

    Picasso picasso;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        picasso = Picasso.with(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = (TextView) view.findViewById(R.id.item_detail_name);
        description = (TextView) view.findViewById(R.id.item_detail_desc);
        price = (TextView) view.findViewById(R.id.item_detail_price);
        shopName = (TextView) view.findViewById(R.id.item_detail_shop_name);
        visitShop = (Button) view.findViewById(R.id.item_detail_shop_visit);
        addToCart = (Button) view.findViewById(R.id.item_detail_addtocart);
        itemPic = (ImageView) view.findViewById(R.id.item_detail_image);

        Bundle bundle = getArguments();
        String Title;
        Bitmap imageBitmap;
        String transText;
        String transitionName;

        if (bundle != null) {
            transitionName = bundle.getString("TRANS_NAME");
            Title = bundle.getString("ACTION");
            imageBitmap = bundle.getParcelable("IMAGE");
            transText = bundle.getString("TRANS_TEXT");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                name.setTransitionName(transitionName);
                itemPic.setTransitionName(transText);
            }

            name.setText(Title);
            itemPic.setImageBitmap(imageBitmap);
        }


        new loadingData().execute();
    }

    private void updateUI() {
        name.setText(item_name);
        description.setText(item_desc);
        price.setText(item_price);
        shopName.setText(shop_name_txt);
        picasso.load("http://bubble.zeowls.com/uploads/" + item_image).fit().centerCrop().into(itemPic);
    }

    public void setId(int id) {
        this.item_id = id;
    }

    private class loadingData extends AsyncTask {

        JSONObject itemsJSON;

        @Override
        protected void onPostExecute(Object o) {
            try {
                JSONObject item = itemsJSON.getJSONArray("Items").getJSONObject(0);

                item_name = item.getString("name");
                item_price = "$" + item.getString("price");
                item_image = item.getString("image");
                item_desc = item.getString("description");
                shop_name_txt = item.getString("shop_name");
                shop_id = item.getInt("shop_id");

                updateUI();

                endFragment = new Shop_Detail_Fragment();
                visitShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fragmentTag = "ShopFragment";
                        String backStateName = this.getClass().getName();
                        FragmentManager manager = getFragmentManager();

                        if (manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
                            FragmentTransaction ft = manager.beginTransaction();
                            endFragment.setId(shop_id);
                            ft.add(R.id.fragment_main, endFragment, fragmentTag);
                            ft.addToBackStack(backStateName);
                            ft.commit();
                        }
                    }
                });

                addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user_id != 0) {
                            if (item_id != 0 && shop_id != 0) {
                                new Core(getActivity()).addToCart(shop_id,item_id,item_name,item_price,item_image,item_desc,shop_name_txt);
                                FireOwl fireOwl = new FireOwl(getActivity());
                                fireOwl.addOrder(shop_id,item_id,user_id);
                            } else {
                                Log.d("Id Empty", "Item And Shop Ids are Empty");
                            }
                        } else {
                            DialogFragment newFragment = new LoginFragment();
                            newFragment.show(getFragmentManager(), "missiles");
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Core core = new Core(getContext());
            try {
                itemsJSON = core.getItem(item_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
