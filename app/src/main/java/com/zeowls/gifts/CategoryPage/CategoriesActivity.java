package com.zeowls.gifts.CategoryPage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zeowls.gifts.R;

public class CategoriesActivity extends AppCompatActivity {

    CategoryContentFragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        fragment = new CategoryContentFragment();
       // fragment.setId(id);
        fragmentTransaction.replace(R.id.category1, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragmentManager.popBackStack();
    }
}
