package com.plexosysconsult.hellofreshug;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {

    private static final String URL_GET_ORDERS = "http://www.gari-share.com/example/getCustomerOrdersByEmail.php";
//app files are stored on gari share server for security purposes
    

    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    List<Order> ordersToShow;
    SwipeRefreshLayout swipeRefreshLayout;
    UsefulFunctions usefulFunctions;
    ProgressBar pbLoading;
    LinearLayout errorLayout;
    Button bReload;
    TextView tvErrorMsg;
    RecyclerView recyclerView;
    View v;
    SharedPreferences userSharedPrefs;
    Button errorButton;
    TextView errorMessage;

    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_orders, container, false);
        //  tvOrders = (TextView) v.findViewById(R.id.tv_orders);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        pbLoading = (ProgressBar) v.findViewById(R.id.pb_loading);
        errorLayout = (LinearLayout) v.findViewById(R.id.error_layout);
        bReload = (Button) v.findViewById(R.id.b_reload);
        tvErrorMsg = (TextView) v.findViewById(R.id.tv_error_message);
        errorLayout = (LinearLayout) v.findViewById(R.id.error_layout);
        errorButton = (Button) v.findViewById(R.id.b_reload);
        errorMessage = (TextView) v.findViewById(R.id.tv_error_message);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userSharedPrefs = getActivity().getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);

        if (userSharedPrefs.getBoolean("available", false)){

            //if the user is logged in

            String email = userSharedPrefs.getString("email", "non");

            // tvOrders.setText("Show all orders by " + email);

            recyclerView.hasFixedSize();


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            //  mLayoutManager.setReverseLayout(true);
            //  mLayoutManager.setStackFromEnd(true);


            recyclerView.setLayoutManager(mLayoutManager);


            ordersToShow = new ArrayList();


            fetchCustomerOrders(email);

        }else{

            //if user is not logged in or if this is a guest, show error message and button to login

            pbLoading.setVisibility(View.GONE);
            errorMessage.setText("Oops! You are not logged in");
            errorButton.setText("Login");
            errorLayout.setVisibility(View.VISIBLE);

            errorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                }
            });




        }


    }

    private void fetchCustomerOrders(final String email) {

        pbLoading.setVisibility(View.VISIBLE);

        StringRequest ordersRequest = new StringRequest(Request.Method.POST, URL_GET_ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //  usefulFunctions.mCreateAndSaveFile(jsonFileName, response);

                        Log.d("OrdersFragment", response);

                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            putJsonIntoList(jsonResponse);
                            pbLoading.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//
                        pbLoading.setVisibility(View.GONE);

                        if (error instanceof NoConnectionError) {

                            tvErrorMsg.setText("Connection could not be established");
                            errorButton.setText("Reload");


                        } else if (error instanceof TimeoutError) {

                            tvErrorMsg.setText("Request timed out");
                            errorButton.setText("Reload");

                        } else if (error instanceof ParseError) {

                            tvErrorMsg.setText("Oops! Something went wrong. Data unreadable");
                            errorButton.setText("Reload");

                        }

                        errorButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorLayout.setVisibility(View.GONE);
                                fetchCustomerOrders(email);
                            }
                        });

                        errorLayout.setVisibility(View.VISIBLE);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", email);

                return map;
            }
        };

        ordersRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });


        myApplicationClass.add(ordersRequest);


    }

    private void putJsonIntoList(JSONObject jsonResponse) {


        try {

            JSONArray orders = jsonResponse.getJSONArray("orders");

            for (int i = orders.length() - 1; i >= 0; i--) {

                JSONObject orderJSON = orders.getJSONObject(i);

                Order order = new Order();

                order.setOrderId(orderJSON.getInt("id"));
                order.setDateCreated(orderJSON.getString("created_at"));
                order.setNumberOfItems(orderJSON.getInt("total_line_items_quantity"));
                order.setTotalAmount(orderJSON.getString("total"));
                order.setDeliveryCharge(orderJSON.getString("total_shipping"));
                order.setStatus(orderJSON.getString("status"));
                order.setNote(orderJSON.getString("note"));


                JSONObject shippingJson = orderJSON.getJSONObject("shipping_address");
                order.setAddressLine1(shippingJson.getString("address_1"));
                order.setAddressLine2(shippingJson.getString("address_2"));
                order.setCompany(shippingJson.getString("company"));
                order.setCity(shippingJson.getString("city"));
                order.setState(shippingJson.getString("state"));

                order.setPaymentMethod(orderJSON.getJSONObject("payment_details").getString("method_title"));


                JSONArray orderItemsJSONArray = orderJSON.getJSONArray("line_items");
                if (orderItemsJSONArray.length() > 0) {


                    List<OrderLineItem> orderLineItemsList = new ArrayList<>();

                    for (int j = 0; j < orderItemsJSONArray.length(); j++) {

                        JSONObject orderItemJSON = orderItemsJSONArray.getJSONObject(j);

                        OrderLineItem orderItem = new OrderLineItem();
                        orderItem.setItemId(orderItemJSON.getInt("product_id"));
                        orderItem.setItemName(orderItemJSON.getString("name"));
                        orderItem.setItemPrice(orderItemJSON.getString("price"));
                        orderItem.setItemQuantity(orderItemJSON.getString("quantity"));
                        orderItem.setItemTotalPrice(orderItemJSON.getString("total"));

                        orderLineItemsList.add(orderItem);

                    }

                    order.setLineItems(orderLineItemsList);


                }


                ordersToShow.add(order);

            }


        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();

        }

        if (ordersToShow.size() < 1) {

            errorButton.setText("Shop Now");
            errorMessage.setText("No orders yet");

            final MainActivity mainActivity = (MainActivity) getActivity();

            errorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.showShopFragment();
                }
            });

            errorLayout.setVisibility(View.VISIBLE);


        }
        recyclerView.setAdapter(new RecyclerViewAdapterOrders(getActivity(), ordersToShow));


    }

}
