package com.plexosysconsult.hellofreshug;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OrderLineItem implements Parcelable {


    String imageUrl;
    String itemName;
    String itemPrice;
    String itemTotalPrice;
    String itemQuantity;
    int itemId;

    public OrderLineItem() {

    }

    protected OrderLineItem(Parcel in) {

        this();
        imageUrl = in.readString();
        itemName = in.readString();
        itemPrice = in.readString();
        itemTotalPrice = in.readString();
        itemQuantity = in.readString();
        itemId = in.readInt();
    }

    public static final Creator<OrderLineItem> CREATOR = new Creator<OrderLineItem>() {
        @Override
        public OrderLineItem createFromParcel(Parcel in) {
            return new OrderLineItem(in);
        }

        @Override
        public OrderLineItem[] newArray(int size) {
            return new OrderLineItem[size];
        }
    };

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

    public String getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(itemName);
        dest.writeString(itemPrice);
        dest.writeString(itemTotalPrice);
        dest.writeString(itemQuantity);
        dest.writeInt(itemId);
    }
}
