package com.zeowls.gifts.ItemDetailsPage;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemDataMode implements Parcelable {
    public int catId, id, shopId;
    String name;
    String desc;
    String imgUrl;
    String price, shortDesc, shopName;

    protected ItemDataMode(Parcel in) {
        catId = in.readInt();
        id = in.readInt();
        shopId = in.readInt();
        name = in.readString();
        desc = in.readString();
        imgUrl = in.readString();
        price = in.readString();
        shortDesc = in.readString();
        shopName = in.readString();
    }

    public static final Creator<ItemDataMode> CREATOR = new Creator<ItemDataMode>() {
        @Override
        public ItemDataMode createFromParcel(Parcel in) {
            return new ItemDataMode(in);
        }

        @Override
        public ItemDataMode[] newArray(int size) {
            return new ItemDataMode[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return "http://bubble.zeowls.com/uploads/" + imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(catId);
        dest.writeInt(id);
        dest.writeInt(shopId);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(imgUrl);
        dest.writeString(price);
        dest.writeString(shortDesc);
        dest.writeString(shopName);
    }
}