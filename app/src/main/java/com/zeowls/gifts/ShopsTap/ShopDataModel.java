package com.zeowls.gifts.ShopsTap;

/**
 * Created by nora on 3/23/2016.
 */
public class ShopDataModel {
    int id, ParentCatID;
    String name, description, owner, pictureUrl;

    public int getParentCatID() {
        return ParentCatID;
    }

    public void setParentCatID(int parentCatID) {
        ParentCatID = parentCatID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPictureUrl() {
        return "http://bubble-zeowls.herokuapp.com/uploads/" + pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
