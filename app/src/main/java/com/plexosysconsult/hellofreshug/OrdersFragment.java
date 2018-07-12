package com.plexosysconsult.hellofreshug;


import android.content.Context;
import android.content.SharedPreferences;
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
public class OrdersFragment extends Fragment {

    TextView tvOrders;
    View root;
    SharedPreferences userSharedPrefs;

    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_orders, container, false);
        tvOrders = (TextView) root.findViewById(R.id.tv_orders);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userSharedPrefs = getActivity().getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);

        String email = userSharedPrefs.getString("email", "non");

        tvOrders.setText("Show all orders by " + email);


    }
}
