package com.plexosysconsult.hellofreshug;

import android.content.Context;
import android.content.Intent;
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

public class RecyclerViewAdapterOrders extends RecyclerView.Adapter<RecyclerViewAdapterOrders.ViewHolder> {

    Context context;
    List<Order> orderList;
    BigDecimalClass bigDecimalClass;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    ConversionClass mCC;


    public RecyclerViewAdapterOrders(Context c, List<Order> orders) {


        context = c;
        bigDecimalClass = new BigDecimalClass(context);
        mCC = new ConversionClass(context);
        orderList = new ArrayList<>();
        orderList = orders;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);

        RecyclerViewAdapterOrders.ViewHolder viewHolder = new RecyclerViewAdapterOrders.ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Order order = (Order) this.orderList.get(position);

        holder.tvOrderNumber.setText("Order #" + order.getOrderId());
        holder.tvOrderDate.setText(mCC.formatDateForDisplayInOrders(order.getDateCreated()));
        holder.tvOrderTime.setText("@ " + mCC.formatTimeForDisplayInOrders(order.getDateCreated()));
        holder.tvOrderNumberOfItems.setText(order.getNumberOfItems() + " items");
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvOrderAmount.setText(bigDecimalClass.convertStringToDisplayCurrencyString(order.getTotalAmount()));

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, final int position, boolean isLongClick) {


                myApplicationClass.setSelectedOrderLineItems(order.getLineItems());
                Log.d("orderItemsListLength1", "" + order.getLineItems().size());
                Intent i = new Intent(context, OrderDetailsActivity.class);

                i.putExtra("order", order);

                context.startActivity(i);


            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvOrderNumber, tvOrderAmount, tvOrderDate, tvOrderStatus, tvOrderNumberOfItems, tvOrderTime;

        ItemClickListener itemClickListener;
        ProgressBar loading;


        public ViewHolder(View itemView) {
            super(itemView);

            tvOrderNumber = (TextView) itemView.findViewById(R.id.tv_order_number);
            tvOrderAmount = (TextView) itemView.findViewById(R.id.tv_order_amount);
            tvOrderDate = (TextView) itemView.findViewById(R.id.tv_order_date);
            tvOrderTime = (TextView) itemView.findViewById(R.id.tv_order_time);
            tvOrderStatus = (TextView) itemView.findViewById(R.id.tv_order_status);
            tvOrderNumberOfItems = (TextView) itemView.findViewById(R.id.tv_number_of_items);
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
