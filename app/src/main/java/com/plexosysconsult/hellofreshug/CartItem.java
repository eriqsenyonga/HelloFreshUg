package com.plexosysconsult.hellofreshug;

import android.content.Context;

/**
 * Created by senyer on 6/13/2016.
 */
public class CartItem {

    String itemName;
    int itemId;
    String itemVariationId;
    String itemUnitPrice;
    String quantity;
    Long total;
    BigDecimalClass bigDecimalClass;

    public CartItem(Context context){

        bigDecimalClass = new BigDecimalClass(context);

    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemVariationId() {
        return itemVariationId;
    }

    public void setItemVariationId(String itemVariationId) {
        this.itemVariationId = itemVariationId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(String itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    private void calculateTotal(){

        total = bigDecimalClass.multiplyParameters(itemUnitPrice, quantity);
    }

    public String getItemTotalForShow() {

        String totalForShow;

        calculateTotal();

        totalForShow = bigDecimalClass.convertLongToDisplayCurrencyString(total);


        return totalForShow;
    }

    public Long getItemTotalForCalculation(){

        calculateTotal();

        return total;
    }


}
