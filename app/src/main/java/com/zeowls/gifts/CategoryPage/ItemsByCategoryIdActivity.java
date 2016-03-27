package com.zeowls.gifts.CategoryPage;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zeowls.gifts.R;

public class ItemsByCategoryIdActivity extends AppCompatActivity {

    ItemsByCategoryIdFragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_by_category_id);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        fragment = new ItemsByCategoryIdFragment();
        fragment.setId(id);
        fragmentTransaction.replace(R.id.item1, fragment);
        fragmentTransaction.commit();
    }
}
