package com.plexosysconsult.hellofreshug;

/**
 * Created by senyer on 6/12/2016.
 */
public class Item {

    String imageUrl;
    String itemName;
    String itemPrice;
    int itemId;
    

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }




    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
}
