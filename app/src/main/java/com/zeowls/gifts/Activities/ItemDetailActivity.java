package com.zeowls.gifts.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zeowls.gifts.Fragments.HomePageFragment;
import com.zeowls.gifts.Fragments.Item_Detail_Fragment;
import com.zeowls.gifts.R;

public class ItemDetailActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Item_Detail_Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        int item_id = intent.getIntExtra("id", 0);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new Item_Detail_Fragment();
        fragment.setId(item_id);
        fragmentTransaction.replace(R.id.fragment_main, fragment, "homeFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
