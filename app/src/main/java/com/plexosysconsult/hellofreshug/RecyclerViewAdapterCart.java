package com.plexosysconsult.hellofreshug;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.plexosysconsult.hellofreshug.ItemClickListener;

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
    BigDecimalClass bigDecimalClass;
    CartDataChanged cartDataChanged;

    public RecyclerViewAdapterCart(Context context) {

        this.context = context;

        cartItemList = new ArrayList<>();

        cart = myApplicationClass.getCart();

        cartItemList = cart.getCurrentCartItems();

        bigDecimalClass = new BigDecimalClass(context);

        this.cartDataChanged = null;

    }

    public void setCartDataChangedListener(CartDataChanged listener) {
        this.cartDataChanged = listener;
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_new, parent, false);
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

            final CartItem cartItem = cartItemList.get(position);

            cartItemViewHolder.tvProduct.setText(cartItem.getItemName());
            cartItemViewHolder.tvQuantity.setText(cartItem.getQuantity());
            cartItemViewHolder.tvPrice.setText(cartItem.getItemTotalForShow());

            Glide
                    .with(context)
                    .load(cartItem.getItemImageUrl())
                    .centerCrop()
                    //      .placeholder(R.drawable.placeholder_veggie)
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            //  cartItemViewHolder.loading.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            //  itemViewHolder.loading.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(cartItemViewHolder.ivImage);


            cartItemViewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, final int position, boolean isLongClick) {


                    LayoutInflater inflater = LayoutInflater.from(context);

                    View addToCartDialog = inflater.inflate(R.layout.dialog_cart_item_edit, null);

                    final TextView tvCartItemName = (TextView) addToCartDialog.findViewById(R.id.tv_item_name);
                    final TextView tvCartItemPrice = (TextView) addToCartDialog.findViewById(R.id.tv_item_unit_price);
                    final TextView tvAmount = (TextView) addToCartDialog.findViewById(R.id.tv_amount);
                    final TextInputLayout tilQuantity = (TextInputLayout) addToCartDialog.findViewById(R.id.til_quantity);
                    final Button bSave = (Button) addToCartDialog.findViewById(R.id.b_save);
                    final Button bCancel = (Button) addToCartDialog.findViewById(R.id.b_cancel);


                    tvCartItemName.setText(cartItem.getItemName());
                    tvCartItemPrice.setText(cartItem.getItemUnitPrice());
                    tilQuantity.getEditText().setText(cartItem.getQuantity());
                    tvAmount.setText(cartItem.getItemTotalForShow());

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

                    bCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            d.dismiss();
                        }
                    });

                    bSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (tilQuantity.getEditText().getText().toString().isEmpty()) {
                                tilQuantity.getEditText().setError("Enter Quantity");
                            } else {

                                if (tilQuantity.getEditText().getText().toString().equals("0")) {

                                    cartItemList.remove(cartItem);

                                } else {

                                    CartItem newCartItem = new CartItem(context);


                                    newCartItem.setItemName(tvCartItemName.getText().toString());
                                    newCartItem.setItemId(cartItem.getItemId());

                                    newCartItem.setQuantity(tilQuantity.getEditText().getText().toString());
                                    newCartItem.setItemUnitPrice(cartItem.getItemUnitPrice());


                                    cartItemList.set(position, newCartItem);

                                }

                                d.dismiss();

                                notifyDataSetChanged();

                                cartDataChanged.onCartDataChanged();

                            }
                        }
                    });

                    d.show();


                }
            });


        }
    }

    @Override
    public int getItemViewType(int position) {
        //   if (isPositionHeader(position)) {
        //     return TYPE_HEADER;
        //  } else

        if (isPositionFooter(position)) {
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


        //  return position != getItemCount() + 1;

        if (position == getItemCount() - 1) {

            return true;
        } else {
            return false;
        }


    }

    @Override
    public int getItemCount() {
        return cartItemList.size() + 1;
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

    public static class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvQuantity, tvProduct, tvPrice;
        ItemClickListener itemClickListener;
        ImageView ivImage;


        public CartItemViewHolder(View itemView) {
            super(itemView);

            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tvProduct = (TextView) itemView.findViewById(R.id.tv_product);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_cart_item_image);

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
