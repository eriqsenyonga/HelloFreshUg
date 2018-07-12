package com.plexosysconsult.hellofreshug;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    View v;
    RecyclerView rvCart;
    LinearLayoutManager linearLayoutManager;
    Toolbar toolbar;
    Button bCheckOut, bShopNow;
    TextView tvGrandTotal;
    TextView tvEmptyMsg;
    String testVCS;
    RecyclerViewAdapterCart adapter;
    LinearLayout emptyLayout;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    Cart cart;
    MainActivity mainActivity;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cart, container, false);

        tvGrandTotal = (TextView) v.findViewById(R.id.tv_grand_total);
        bCheckOut = (Button) v.findViewById(R.id.b_checkout);
        rvCart = (RecyclerView) v.findViewById(R.id.recycler_view);
        emptyLayout = (LinearLayout) v.findViewById(R.id.empty_layout);
        tvEmptyMsg = (TextView) v.findViewById(R.id.tv_empty_message);
        bShopNow = (Button) v.findViewById(R.id.b_shop_now);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cart = myApplicationClass.getCart();
        mainActivity = (MainActivity) getActivity();


        rvCart.hasFixedSize();

        linearLayoutManager = new LinearLayoutManager(getActivity());

        rvCart.setLayoutManager(linearLayoutManager);
//        rvCart.addItemDecoration(new ListDividerDecoration(getActivity()));

        adapter = new RecyclerViewAdapterCart(getActivity());

        adapter.setCartDataChangedListener(new CartDataChanged() {
            @Override
            public void onCartDataChanged() {
                tvGrandTotal.setText(cart.getCartGrandTotal());
            }
        });

        rvCart.setAdapter(adapter);

        if (adapter.getItemCount() > 1) {
            rvCart.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            bCheckOut.setEnabled(true);
        } else {
            rvCart.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            tvEmptyMsg.setText("Your cart is empty");
            bShopNow.setVisibility(View.VISIBLE);
            bCheckOut.setEnabled(false);
        }

        tvGrandTotal.setText(cart.getCartGrandTotal());

        bCheckOut.setOnClickListener(this);
        bShopNow.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == bCheckOut) {

            //   Toast.makeText(getActivity(),"Go to checkout", Toast.LENGTH_LONG).show();


            Intent i = new Intent(getActivity(), BillingDetails.class);
            startActivity(i);

        }

        if(v == bShopNow){


            mainActivity.showShopFragment();


        }
    }


}
