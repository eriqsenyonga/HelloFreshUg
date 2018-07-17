package com.plexosysconsult.hellofreshug;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderInfoFragment extends Fragment {


    TextView tvAddress1, tvAddress2, tvCity, tvPaymentMethod, tvDeliveryFee;
    View v;

    Order order;
    OrderDetailsActivity orderDetailsActivity;


    public OrderInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_order_info, container, false);
        tvAddress1 =(TextView)v.findViewById(R.id.tv_address_1);
        tvAddress2 =(TextView)v.findViewById(R.id.tv_address_2);
        tvCity = (TextView)v.findViewById(R.id.tv_city);
        tvPaymentMethod =(TextView)v.findViewById(R.id.tv_payment_method);
        tvDeliveryFee = (TextView)v.findViewById(R.id.tv_delivery_amount);
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        orderDetailsActivity = (OrderDetailsActivity) getActivity();

        order = orderDetailsActivity.getOrder();


        tvAddress1.setText(order.getAddressLine1());
        tvAddress2.setText(order.getAddressLine2());
        tvCity.setText(order.getCity());
        tvPaymentMethod.setText(order.getPaymentMethod());
        tvDeliveryFee.setText(order.getDeliveryCharge());


    }
}
