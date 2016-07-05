package com.plexosysconsult.hellofreshug;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by senyer on 6/12/2016.
 */
public class RecyclerViewAdapterVegetable extends RecyclerView.Adapter<RecyclerViewAdapterVegetable.ViewHolder> {

    Context context;
    List<Item> vegetableList;
    BigDecimalClass bigDecimalClass;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    MainActivity mainActivity;
    Cart cart;

    public RecyclerViewAdapterVegetable(Context context) {

        this.context = context;

        cart = myApplicationClass.getCart();

        mainActivity = (MainActivity) context;

        bigDecimalClass = new BigDecimalClass(context);

        vegetableList = new ArrayList();


        for (int i = 0; i < 30; i++) {

            Item veggie = new Item();

            veggie.setItemName("Veggie " + i);
            veggie.setItemPrice((i + 2) + "000");

            vegetableList.add(veggie);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vegetable_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Item veggie = (Item) this.vegetableList.get(position);

        holder.tvItemName.setText(veggie.getItemName());
        holder.tvItemPrice.setText(bigDecimalClass.convertStringToDisplayCurrencyString(veggie.getItemPrice()));

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, final int position, boolean isLongClick) {

                Toast.makeText(context, vegetableList.get(position).getItemName() + ": " + vegetableList.get(position).getItemPrice(), Toast.LENGTH_LONG).show();

                LayoutInflater inflater = LayoutInflater.from(context);

                View addToCartDialog = inflater.inflate(R.layout.dialog_add_item_to_cart_details, null);

                TextView tvCartItemName = (TextView) addToCartDialog.findViewById(R.id.tv_item_name);
                TextView tvCartItemPrice = (TextView) addToCartDialog.findViewById(R.id.tv_item_unit_price);
                final TextView tvAmount = (TextView) addToCartDialog.findViewById(R.id.tv_amount);
                final TextInputLayout tilQuantity = (TextInputLayout) addToCartDialog.findViewById(R.id.til_quantity);
                FloatingActionButton fabAddToCart = (FloatingActionButton) addToCartDialog.findViewById(R.id.fab_add_to_cart);

                tvCartItemName.setText(vegetableList.get(position).getItemName());
                tvCartItemPrice.setText(vegetableList.get(position).getItemPrice());

                tilQuantity.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (tilQuantity.getEditText().getText().toString().isEmpty()) {

                            tvAmount.setText("UGX 0");
                        } else {

                            tvAmount.setText(bigDecimalClass.convertLongToDisplayCurrencyString((bigDecimalClass.multiplyParameters(vegetableList.get(position).getItemPrice(), tilQuantity.getEditText().getText().toString()))));
                        }

                    }
                });



                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(addToCartDialog);

                final Dialog d = builder.create();


                fabAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (tilQuantity.getEditText().getText().toString().isEmpty()) {
                            tilQuantity.getEditText().setError("Enter Quantity");
                        } else {
                            CartItem cartItem = new CartItem(context);

                            cartItem.setItemName(vegetableList.get(position).getItemName());
                            cartItem.setQuantity(tilQuantity.getEditText().getText().toString());
                            cartItem.setItemUnitPrice(vegetableList.get(position).getItemPrice());

                            cart.addItemToCart(cartItem);

                            myApplicationClass.updateCart(cart);

                            mainActivity.checkCartForItems();

                            d.dismiss();
                        }

                    }
                });

                d.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return vegetableList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvItemName, tvItemPrice;
        ImageView ivItemImage;
        ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            tvItemPrice = (TextView) itemView.findViewById(R.id.tv_item_price);
            ivItemImage = (ImageView) itemView.findViewById(R.id.iv_item_image);

            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
