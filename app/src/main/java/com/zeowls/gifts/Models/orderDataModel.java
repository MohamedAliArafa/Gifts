package com.zeowls.gifts.Models;

/**
 * Created by root on 4/23/16.
 */
public class orderDataModel {
    public int item_id;
    public int user_id;
    public int status;

    public orderDataModel(){

    }
    public orderDataModel(int item_id, int user_id, int status) {
        this.item_id = item_id;
        this.user_id = user_id;
        this.status = status;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}