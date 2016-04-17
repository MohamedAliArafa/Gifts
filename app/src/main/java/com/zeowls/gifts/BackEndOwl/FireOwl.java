package com.zeowls.gifts.BackEndOwl;

import android.content.Context;

import com.firebase.client.Firebase;

public class FireOwl {

    private String FirebaseURL = "https://giftshop.firebaseio.com/";
    private Firebase firebaseRef;

    public FireOwl(Context context){
        Firebase.setAndroidContext(context);
        firebaseRef = new Firebase(FirebaseURL);
    }

    public void addOrder(int shop_id, int item_id, int user_id){
        firebaseRef.child("orders").child(String.valueOf("Shop")).child(String.valueOf(shop_id)).child("item:"+item_id+",user:"+user_id).setValue(new orderDataModel(shop_id,item_id,user_id));
        firebaseRef.child("orders").child(String.valueOf("User")).child(String.valueOf(user_id)).child("item:"+item_id+",shop:"+shop_id).setValue(new orderDataModel(shop_id,item_id,user_id));
    }

    public class orderDataModel {
        public int item_id;
        public int shop_id;
        public int user_id;

        public orderDataModel(int shop_id, int item_id, int user_id) {
            this.item_id = item_id;
            this.shop_id = shop_id;
            this.user_id = user_id;
        }
    }
}
