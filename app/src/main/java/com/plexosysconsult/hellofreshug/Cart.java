package com.plexosysconsult.hellofreshug;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by senyer on 6/13/2016.
 */
public class Cart {


    List<CartItem> currentCartItems;
    Long cartTotal = Long.valueOf(0);
    Context context;
    BigDecimalClass bigDecimalClass;

    public Cart(Context context) {

        this.context = context;

        bigDecimalClass = new BigDecimalClass(this.context);

        currentCartItems = new ArrayList();

    }

    private void calculateCartTotal() {

        for (int i = 0; i < currentCartItems.size(); i++) {

            cartTotal = bigDecimalClass.addParameters(currentCartItems.get(i).getItemTotalForCalculation(), cartTotal);
        }

    }

    public String getCartTotalDisplayString() {


        calculateCartTotal();

        return bigDecimalClass.convertLongToDisplayCurrencyString(cartTotal);
    }

    public void addItemToCart(CartItem cartItem) {

        currentCartItems.add(cartItem);
    }

    public void removeItemFromCart(int position) {

        currentCartItems.remove(position);
    }

    public List<CartItem> getCurrentCartItems() {
        return this.currentCartItems;
    }

    public void emptyCart() {

        currentCartItems.removeAll(currentCartItems);
    }


}
