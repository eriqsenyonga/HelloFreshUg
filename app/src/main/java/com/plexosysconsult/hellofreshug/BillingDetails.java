package com.plexosysconsult.hellofreshug;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingDetails extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout tilFirstName, tilSurName, tilEmail, tilPhoneNumber,
            tilPassword, tilReenterPassword, tilDeliveryAddress, tilTownCity;
    Button bPlaceOrder;
    CheckBox cbCreateAccount;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    Cart cart;
    String URL_PLACE_ORDER = "http://www.hellofreshuganda.com/example/placeOrder.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tilFirstName = (TextInputLayout) findViewById(R.id.til_firstname);
        tilSurName = (TextInputLayout) findViewById(R.id.til_surname);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilPhoneNumber = (TextInputLayout) findViewById(R.id.til_phone_number);
        tilDeliveryAddress = (TextInputLayout) findViewById(R.id.til_address_line_1);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        tilTownCity = (TextInputLayout) findViewById(R.id.til_city_town);
        tilReenterPassword = (TextInputLayout) findViewById(R.id.til_reenter_password);
        bPlaceOrder = (Button) findViewById(R.id.b_place_order);
        cbCreateAccount = (CheckBox) findViewById(R.id.cb_create_account);


        bPlaceOrder.setOnClickListener(this);
        cbCreateAccount.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view == bPlaceOrder) {

            //on clicking Place Order, organise the order details to JSON ie the cart and the billing details plus the mode of payment


            try {
                JSONObject orderObject = new JSONObject();

                orderObject.put("payment_method", "COD");
                orderObject.put("payment_method_title", "Cash on Delivery");
                orderObject.put("set_paid", true);
             //   orderObject.put("status", "processing");
                orderObject.put("shipping_total", 10000);

                //add billing jsonArray

                JSONArray billingJsonArray = new JSONArray();

                JSONObject billingJson = new JSONObject();
                billingJson.put("first_name", tilFirstName.getEditText().getText().toString());
                billingJson.put("last_name", tilSurName.getEditText().getText().toString());
                billingJson.put("address_1", tilDeliveryAddress.getEditText().getText().toString());
                billingJson.put("address_2", "");
                billingJson.put("email", tilEmail.getEditText().getText().toString());
                billingJson.put("phone", tilPhoneNumber.getEditText().getText().toString());
                billingJson.put("city", tilTownCity.getEditText().getText().toString());
                billingJson.put("country", "UG");
                billingJson.put("state", "Uganda");
                billingJson.put("postcode", "256");

                billingJsonArray.put(billingJson);

                orderObject.put("billing_address", billingJson);

                //add shipping jsonArray

                JSONArray shippingJsonArray = new JSONArray();

                JSONObject shippingJson = new JSONObject();
                shippingJson.put("first_name", tilFirstName.getEditText().getText().toString());
                shippingJson.put("last_name", tilSurName.getEditText().getText().toString());
                shippingJson.put("address_1", tilDeliveryAddress.getEditText().getText().toString());
                shippingJson.put("address_2", "");
                shippingJson.put("city", tilTownCity.getEditText().getText().toString());
                shippingJson.put("country", "UG");
                shippingJson.put("state", "Uganda");
                shippingJson.put("postcode", "256");

                shippingJsonArray.put(shippingJson);

                orderObject.put("shipping_address", shippingJson);

                //add line_items json array
                JSONArray lineItemsJsonArray = new JSONArray();

                cart = myApplicationClass.getCart();

                List<CartItem> cartItems = cart.getCurrentCartItems();

                for (CartItem cartItem : cartItems) {

                    JSONObject lineItem = new JSONObject();



                    if (cartItem.isVariation()) {
                        lineItem.put("product_id", cartItem.getItemVariationId());
                    }else{

                        lineItem.put("product_id", cartItem.getItemId());
                    }

                    lineItem.put("quantity", cartItem.getQuantity());

                    lineItemsJsonArray.put(lineItem);
                }

                orderObject.put("line_items", lineItemsJsonArray);


                //add shipping_lines object

                JSONArray shippingLinesJsonArray = new JSONArray();

                JSONObject shippingLinesObject = new JSONObject();

                shippingLinesObject.put("method_id", "Flat Rate");
                shippingLinesObject.put("method_title", "Delivery Fee");
                shippingLinesObject.put("total", 10000);

                shippingLinesJsonArray.put(shippingLinesObject);


                orderObject.put("shipping_lines", shippingLinesJsonArray);


                Log.d("order", orderObject.toString());

                placeOrderOnline(orderObject);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (view == cbCreateAccount) {


            if (cbCreateAccount.isChecked()) {

                tilPassword.setVisibility(View.VISIBLE);
                tilReenterPassword.setVisibility(View.VISIBLE);

            } else {

                tilPassword.setVisibility(View.GONE);
                tilReenterPassword.setVisibility(View.GONE);

            }


        }
    }

    private void placeOrderOnline(final JSONObject orderObject) {

        StringRequest placeOrderOnlineRequest = new StringRequest(Request.Method.POST, URL_PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("order response string", response);

                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d("return_order", response);

                            Intent i = new Intent(BillingDetails.this, OrderSuccessActivity.class);
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(BillingDetails.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("order_details_json_string", orderObject.toString());

                return map;
            }
        };

        myApplicationClass.add(placeOrderOnlineRequest);


    }
}
