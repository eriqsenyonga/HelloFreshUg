package com.plexosysconsult.hellofreshug;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.w3c.dom.Text;

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
    boolean isMainActivityContext = false;

    public static int KEY_MAIN_ACTIVITY = 1;
    public static int KEY_SEARCHABLE_ACTIVITY = 2;


    public RecyclerViewAdapterVegetable(Context context, List<Item> veggiesToShow) {

        this.context = context;

        cart = myApplicationClass.getCart();

        mainActivity = (MainActivity) context;
        isMainActivityContext = true;


        bigDecimalClass = new BigDecimalClass(context);

        vegetableList = new ArrayList();


        vegetableList = veggiesToShow;


    }

    public RecyclerViewAdapterVegetable(Context context, List<Item> veggiesToShow, int whichActivity) {

        this.context = context;

        cart = myApplicationClass.getCart();

        //  mainActivity = (MainActivity) context;


        bigDecimalClass = new BigDecimalClass(context);

        vegetableList = new ArrayList();


        vegetableList = veggiesToShow;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vegetable_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Item veggie = (Item) this.vegetableList.get(position);

        holder.tvItemName.setText(veggie.getItemName());
        holder.tvItemPrice.setText(bigDecimalClass.convertStringToDisplayCurrencyString(veggie.getItemPrice()));

        //  final int selectedVariation = 0;

        Glide
                .with(context)
                .load(veggie.getImageUrl())
                .apply(new RequestOptions().centerCrop())
                .transition(new DrawableTransitionOptions().crossFade())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.loading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.loading.setVisibility(View.GONE);
                        return false;
                    }


                })
                .into(holder.ivItemImage);


        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, final int position, boolean isLongClick) {

                //  Toast.makeText(context, vegetableList.get(position).getItemName() + ": " + vegetableList.get(position).getItemPrice(), Toast.LENGTH_LONG).show();

                LayoutInflater inflater = LayoutInflater.from(context);

                View addToCartDialog = inflater.inflate(R.layout.dialog_add_item_to_cart_details, null);


                TextView tvCartItemName = (TextView) addToCartDialog.findViewById(R.id.tv_item_name);
                TextView tvLabelOptions = (TextView) addToCartDialog.findViewById(R.id.tv_label_options);
                final Spinner spnVariations = (Spinner) addToCartDialog.findViewById(R.id.spn_variations);

                final TextView tvCartItemPrice = (TextView) addToCartDialog.findViewById(R.id.tv_item_unit_price);
                final TextView tvAmount = (TextView) addToCartDialog.findViewById(R.id.tv_amount);
                final TextInputLayout tilQuantity = (TextInputLayout) addToCartDialog.findViewById(R.id.til_quantity);
                FloatingActionButton fabAddToCart = (FloatingActionButton) addToCartDialog.findViewById(R.id.fab_add_to_cart);


                tvCartItemName.setText(vegetableList.get(position).getItemName());
                tvCartItemPrice.setText(vegetableList.get(position).getItemPrice());


                if (tvCartItemName.getText().toString().contains("kg") || tvCartItemName.getText().toString().contains("Kg") || tvCartItemName.getText().toString().contains("KG")) {

                    tilQuantity.getEditText().setHint("Weight");
                    tilQuantity.setHint("Weight");
                    tilQuantity.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                } else {

                    tilQuantity.getEditText().setHint("Quantity");
                    tilQuantity.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);


                }


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

                            tvAmount.setText(bigDecimalClass.convertLongToDisplayCurrencyString((bigDecimalClass.multiplyParameters(tvCartItemPrice.getText().toString(), tilQuantity.getEditText().getText().toString()))));
                        }


                    }


                });


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(addToCartDialog);

                final Dialog d = builder.create();


                if (vegetableList.get(position).getHasVariations()) {

                    tvLabelOptions.setVisibility(View.VISIBLE);
                    spnVariations.setVisibility(View.VISIBLE);

                    final List<Item> variationsList = vegetableList.get(position).getItemVariations();

                    AdapterSpinnerVariations adapter = new AdapterSpinnerVariations(context, android.R.layout.simple_spinner_item, variationsList);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spnVariations.setAdapter(adapter);

                    spnVariations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                            Item item = variationsList.get(i);

                            // tvCartItemPrice.setText(bigDecimalClass.convertStringToDisplayCurrencyString(item.getItemPrice()));
                            tvCartItemPrice.setText(item.getItemPrice());

                            if (tilQuantity.getEditText().getText().toString().isEmpty()) {

                                tvAmount.setText("UGX 0");
                            } else {

                                tvAmount.setText(bigDecimalClass.convertLongToDisplayCurrencyString((bigDecimalClass.multiplyParameters(tvCartItemPrice.getText().toString(), tilQuantity.getEditText().getText().toString()))));
                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                } else {
                    tvLabelOptions.setVisibility(View.GONE);
                    spnVariations.setVisibility(View.GONE);
                }


                fabAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (tilQuantity.getEditText().getText().toString().isEmpty()) {
                            tilQuantity.getEditText().setError("Enter Quantity");
                        } else {
                            CartItem cartItem = new CartItem(context);

                            cartItem.setItemName(vegetableList.get(position).getItemName());
                            cartItem.setItemId(vegetableList.get(position).getItemId());

                            cartItem.setQuantity(tilQuantity.getEditText().getText().toString());
                            cartItem.setItemUnitPrice(vegetableList.get(position).getItemPrice());
                            cartItem.setItemImageUrl(vegetableList.get(position).getImageUrl());

                            if (vegetableList.get(position).getHasVariations()) {

                                cartItem.setIsVariation(true);

                                int selectedVariationPosition = spnVariations.getSelectedItemPosition();


                                //this will be used when creating the order request as variation_id
                                cartItem.setItemVariationId(vegetableList.get(position)
                                        .getItemVariations()
                                        .get(selectedVariationPosition)
                                        .getItemId());

                                //we use the unit price for the variation
                                cartItem.setItemUnitPrice(vegetableList.get(position)
                                        .getItemVariations()
                                        .get(selectedVariationPosition)
                                        .getItemPrice());

                                //name of the item is the parent + the variation item name
                                cartItem.setItemName(vegetableList.get(position).getItemName() + " (" + vegetableList.get(position)
                                        .getItemVariations()
                                        .get(selectedVariationPosition)
                                        .getOptionUnit() + ")");


                            }

                            cart.addItemToCart(cartItem);

                            myApplicationClass.updateCart(cart);
                            if (isMainActivityContext) {
                                mainActivity.checkCartForItems();
                            }

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
        ProgressBar loading;

        public ViewHolder(View itemView) {
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            tvItemPrice = (TextView) itemView.findViewById(R.id.tv_item_price);
            ivItemImage = (ImageView) itemView.findViewById(R.id.iv_item_image);
            loading = (ProgressBar) itemView.findViewById(R.id.pb_loading);

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
