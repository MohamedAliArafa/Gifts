package com.zeowls.gifts;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.client.Firebase;

public class FireOwl {

    public final String FirebaseURL = "https://giftshop.firebaseio.com/";
    public Firebase firebaseRef;

    public String addItem(Context context){
        firebaseRef = new Firebase(FirebaseURL);
        Firebase.setAndroidContext(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Title");
        LinearLayout linearLayout = new LinearLayout(context);
        final EditText name = new EditText(context);
        name.setHint("Enter Product Name");
        final EditText desc = new EditText(context);
        desc.setHint("Enter Product Description");
        linearLayout.addView(name);
        linearLayout.addView(desc);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameTxt = name.getText().toString();
                String descTxt = desc.getText().toString();
                ItemData item = new ItemData();
                item.setName(nameTxt);
                item.setDesc(descTxt);
                firebaseRef.child("items").push().setValue(item);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        return name.getText().toString();
    }
}
