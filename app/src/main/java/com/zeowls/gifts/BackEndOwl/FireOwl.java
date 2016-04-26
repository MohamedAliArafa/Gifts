package com.zeowls.gifts.BackEndOwl;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.zeowls.gifts.Models.orderDataModel;

public class FireOwl {

    private Firebase firebaseRef;

    public FireOwl(Context context) {
        Firebase.setAndroidContext(context);
        String firebaseURL = "https://giftshop.firebaseio.com/";
        firebaseRef = new Firebase(firebaseURL);
    }

    public void addOrder(int shop_id, int item_id, int user_id) {
        firebaseRef.child("orders").child(String.valueOf(shop_id)).push().setValue(new orderDataModel(item_id, user_id, 0));
//        firebaseRef.child("orders").child(String.valueOf(user_id)).push().setValue(new orderDataModel(item_id,user_id,0));
    }

    public void getOrders(int shop_id) {
        firebaseRef.child("orders").child(String.valueOf(shop_id)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    orderDataModel post = postSnapshot.getValue(orderDataModel.class);
                    Log.e("Get Data", String.valueOf(post.getItem_id()));
                }
//                GenericTypeIndicator<List<orderDataModel>> t = new GenericTypeIndicator<List<orderDataModel>>() {};
//                List<orderDataModel> messages = snapshot.getValue(t);
//                if( messages == null ) {
//                    System.out.println("No messages");
//                }
//                else {
//                    System.out.println("The first message is: " + messages.get(0).getItem_id());
//                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }


}
