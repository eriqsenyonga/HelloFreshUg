package com.plexosysconsult.hellofreshug;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    List<OrderLineItem> orderLineItems;
    TabLayout tabs;
    ViewPager viewPager;
    TextView tvOrderNumber, tvOrderAmount, tvOrderDate, tvOrderStatus, tvOrderNumberOfItems, tvOrderTime;
    Toolbar toolbar;
    CollapsingToolbarLayout collapseToolbar;
    Intent i;
    Order order;
    BigDecimalClass bigDecimalClass;
    ConversionClass mCC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        collapseToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tvOrderNumber = (TextView) findViewById(R.id.tv_order_number);
        tvOrderAmount = (TextView) findViewById(R.id.tv_order_amount);
        tvOrderDate = (TextView) findViewById(R.id.tv_order_date);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
        tvOrderNumberOfItems = (TextView) findViewById(R.id.tv_number_of_items);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        orderLineItems = new ArrayList<>();

        i = getIntent();
       // i.setExtrasClassLoader(OrderLineItem.class.getClassLoader());
       order = (Order) i.getParcelableExtra("order");
      //  orderLineItems = order.getLineItems();

     //   Log.d("Order Line Items", "" + orderLineItems.size());


        bigDecimalClass = new BigDecimalClass(OrderDetailsActivity.this);
        mCC = new ConversionClass(OrderDetailsActivity.this);


        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed (make button visible and fab invisible)
                    collapseToolbar.setTitle("Order Details");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);

                } else if (verticalOffset == 0) {
                    // Expanded (make fab visible and toolbar button invisible)
                    collapseToolbar.setTitle(" ");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);

                } else {
                    // Somewhere in between
                    collapseToolbar.setTitle(" ");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);

                }
            }
        });


        //    collapseToolbar.setCollapsedTitleTextColor("#FFFFFF");


        PagerAdapterShop adapterOrderDetails = new PagerAdapterShop(getSupportFragmentManager(), this, PagerAdapterShop.ORDERDETAILSADAPTER);
        viewPager.setAdapter(adapterOrderDetails);

        tabs.setupWithViewPager(viewPager);


        tvOrderNumber.setText("Order #" + order.getOrderId());
        tvOrderDate.setText(mCC.formatDateForDisplayInOrders(order.getDateCreated()));
        tvOrderTime.setText("@ " + mCC.formatTimeForDisplayInOrders(order.getDateCreated()));
        tvOrderNumberOfItems.setText(order.getNumberOfItems() + " items");
        tvOrderStatus.setText(order.getStatus());
        tvOrderAmount.setText(bigDecimalClass.convertStringToDisplayCurrencyString(order.getTotalAmount()));

    }


    public List<OrderLineItem> getOrderLineItems() {


        return orderLineItems;
    }

    public Order getOrder(){
        return  order;
    }
}
