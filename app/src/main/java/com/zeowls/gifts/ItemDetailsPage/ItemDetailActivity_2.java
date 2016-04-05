package com.zeowls.gifts.ItemDetailsPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.zeowls.gifts.R;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class ItemDetailActivity_2 extends AppCompatActivity {

    int id;
    Item_Detail_Fragment Detail_Fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_main);

        // Set title of Detail page
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        if (Detail_Fragment != null) {
            fragmentTransaction.remove(Detail_Fragment);
        }
        Detail_Fragment = new Item_Detail_Fragment();
        Detail_Fragment.setId(id);
        fragmentTransaction.replace(R.id.fragment, Detail_Fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}
