package com.zeowls.gifts;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.zeowls.gifts.BackEndOwl.Core;

import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingCartActivity extends AppCompatActivity {

    int userId = 0;
    TextView shopName,itemName,price,Desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemName = (TextView) findViewById(R.id.cart_item_name);
        price = (TextView) findViewById(R.id.cart_item_price);

        setContentView(R.layout.activity_shopping_cart);
        SharedPreferences prefs = getSharedPreferences("Credentials", MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        if (restoredText != null) {
            userId = prefs.getInt("id", 0);
            if (userId != 0) {
                new GetCart().execute();
            }
        }
    }

    private class GetCart extends AsyncTask {

        JSONObject itemsJSON;

        @Override
        protected void onPreExecute() {
            try {
                itemName.setText(itemsJSON.getJSONArray("Cart").getJSONObject(0).getString("item_name"));
                price.setText(itemsJSON.getJSONArray("Cart").getJSONObject(0).getString( "$" + "item_price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Object o) {

        }

        @Override
        protected Object doInBackground(Object[] params) {
            Core core = new Core(getBaseContext());
            itemsJSON = core.getUserCart(userId);
            return true;
        }
    }
}
