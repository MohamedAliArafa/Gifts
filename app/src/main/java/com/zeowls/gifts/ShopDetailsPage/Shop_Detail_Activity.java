package com.zeowls.gifts.ShopDetailsPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.zeowls.gifts.R;

public class Shop_Detail_Activity extends AppCompatActivity {

    int id;
    Shop_Detail_Fragment Detail_Fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop__detail_);

        // Set title of Detail page
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);


        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        if (Detail_Fragment != null) {
            fragmentTransaction.remove(Detail_Fragment);
        }
        Detail_Fragment = new Shop_Detail_Fragment();
        Detail_Fragment.setId(id);
        fragmentTransaction.replace(R.id.fragment, Detail_Fragment);
        fragmentTransaction.commit();
    }

}
