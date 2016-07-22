package com.plexosysconsult.hellofreshug;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by senyer on 7/22/2016.
 */
public class RecyclerViewAdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    Cart cart;
    List<CartItem> cartItemList;
    Context context;

    public RecyclerViewAdapterCart(Context context) {

        this.context = context;

        cartItemList = new ArrayList<>();

        cart = myApplicationClass.getCart();

        cartItemList = cart.getCurrentCartItems();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_header, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_footer, parent, false);
            return new FooterViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            return new CartItemViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.tvTotalPrice.setText(cart.getCartTotalDisplayString());

        } else if (holder instanceof CartItemViewHolder) {

            CartItemViewHolder cartItemViewHolder = (CartItemViewHolder) holder;

            CartItem cartItem = cartItemList.get(position - 1);

            cartItemViewHolder.tvProduct.setText(cartItem.getItemName());
            cartItemViewHolder.tvQuantity.setText(cartItem.getQuantity());
            cartItemViewHolder.tvPrice.setText(cartItem.getItemTotalForShow());


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position) {

        if (position == 0) {

            return true;
        } else {
            return false;
        }


    }

    private boolean isPositionFooter(int position) {


        if (position == cartItemList.size() + 1) {

            return true;
        } else {
            return false;
        }


    }

    @Override
    public int getItemCount() {
        return cartItemList.size() + 2;
    }


    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalPrice;

        public FooterViewHolder(View itemView) {
            super(itemView);

            tvTotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        //  TextView txtTitleHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            //  this.txtTitleHeader = (TextView) itemView.findViewById (R.id.txtHeader);
        }
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuantity, tvProduct, tvPrice;


        public CartItemViewHolder(View itemView) {
            super(itemView);

            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tvProduct = (TextView) itemView.findViewById(R.id.tv_product);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}
