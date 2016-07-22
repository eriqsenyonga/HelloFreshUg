package com.plexosysconsult.hellofreshug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class CartActivity extends AppCompatActivity {

    RecyclerView rvCart;
    LinearLayoutManager linearLayoutManager;
    Toolbar toolbar;

    RecyclerViewAdapterCart adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");

        rvCart = (RecyclerView) findViewById(R.id.recycler_view);
        rvCart.hasFixedSize();

        linearLayoutManager = new LinearLayoutManager(this);

        rvCart.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapterCart(this);

        rvCart.setAdapter(adapter);


    }
}
