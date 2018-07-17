package com.plexosysconsult.hellofreshug;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {


    int orderId;
    String dateCreated;
    int numberOfItems;
    String totalAmount;
    List<OrderLineItem> lineItems = new ArrayList<>();
    String status;
    String addressLine1;
    String addressLine2;
    String city;
    String state;
    String company;
    String note;
    String deliveryCharge; //this is called total_shipping
    String paymentMethod;


    public Order() {


        lineItems = new ArrayList<>();
    }


    protected Order(Parcel in) {

        this();


        orderId = in.readInt();
        dateCreated = in.readString();
        numberOfItems = in.readInt();
        totalAmount = in.readString();
        status = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        city = in.readString();
        state = in.readString();
        company = in.readString();
        note = in.readString();
        deliveryCharge = in.readString();
        paymentMethod = in.readString();

        //    lineItems = in.readList(lineItems, OrderLineItem.class.getClassLoader());

        //  lineItems = in.readTypedList(getLineItems(), OrderLineItem.CREATOR);

        //  lineItems = in.createTypedArrayList(OrderLineItem.CREATOR);

        in.readTypedList(getLineItems(), OrderLineItem.CREATOR);


     /*   lineItems = in.createTypedArrayList(new Creator<OrderLineItem>() {
            @Override
            public OrderLineItem createFromParcel(Parcel source) {
                return null;
            }

            @Override
            public OrderLineItem[] newArray(int size) {
                return new OrderLineItem[0];
            }
        });
*/
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderId);
        dest.writeString(dateCreated);
        dest.writeInt(numberOfItems);
        dest.writeString(totalAmount);
        dest.writeString(status);
        dest.writeString(addressLine1);
        dest.writeString(addressLine2);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(company);
        dest.writeString(note);
        dest.writeString(deliveryCharge);
        dest.writeString(paymentMethod);

        dest.writeTypedList(lineItems);
    }
}
