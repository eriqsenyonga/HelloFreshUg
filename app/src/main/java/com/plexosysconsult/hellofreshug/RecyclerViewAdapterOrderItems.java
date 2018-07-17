package com.plexosysconsult.hellofreshug;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterOrderItems extends RecyclerView.Adapter<RecyclerViewAdapterOrderItems.ViewHolder> {

    Context context;
    List<OrderLineItem> orderItemsList;
    BigDecimalClass bigDecimalClass;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    ConversionClass mCC;


    public RecyclerViewAdapterOrderItems(Context c, List<OrderLineItem> orderLineItems) {


        context = c;
        bigDecimalClass = new BigDecimalClass(context);
        mCC = new ConversionClass(context);
        orderItemsList = new ArrayList<>();
        orderItemsList = myApplicationClass.getSelectedOrderLineItems();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_line_item, parent, false);

        RecyclerViewAdapterOrderItems.ViewHolder viewHolder = new RecyclerViewAdapterOrderItems.ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        OrderLineItem orderItem = (OrderLineItem) orderItemsList.get(position);

        holder.tvItemName.setText(orderItem.getItemName());
        holder.tvPrice.setText(bigDecimalClass.convertStringToDisplayCurrencyString(orderItem.getItemTotalPrice()));
        holder.tvQuantity.setText("x " + orderItem.getItemQuantity());



    }

    @Override
    public int getItemCount() {
        return orderItemsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPrice, tvItemName, tvQuantity;


        public ViewHolder(View itemView) {
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_item_price);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_item_quantity);


        }


    }
}
