package com.plexosysconsult.hellofreshug;

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

import org.json.JSONException;
import org.json.JSONObject;

public class BillingDetails extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout tilFirstName, tilSurName, tilEmail, tilPhoneNumber, tilPassword, tilReenterPassword, tilDeliveryAddress, tilTownCity;
    Button bPlaceOrder;
    CheckBox cbCreateAccount;

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

                orderObject.put("payment_method", "cod");
                orderObject.put("payment_method_title", "Cash on Delivery");
                orderObject.put("set_paid", false);

                //add billing json object

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

                orderObject.put("billing", billingJson);

                //add shipping json object
                JSONObject shippingJson = new JSONObject();
                shippingJson.put("first_name", tilFirstName.getEditText().getText().toString());
                shippingJson.put("last_name", tilSurName.getEditText().getText().toString());
                shippingJson.put("address_1", tilDeliveryAddress.getEditText().getText().toString());
                shippingJson.put("address_2", "");
                shippingJson.put("city", tilTownCity.getEditText().getText().toString());
                shippingJson.put("country", "UG");
                shippingJson.put("state", "Uganda");
                shippingJson.put("postcode", "256");


                orderObject.put("shipping", shippingJson);




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if(view == cbCreateAccount){

            Log.d("in","in");
            if(cbCreateAccount.isChecked()){

                tilPassword.setVisibility(View.VISIBLE);
                tilReenterPassword.setVisibility(View.VISIBLE);

            }else{

                tilPassword.setVisibility(View.GONE);
                tilReenterPassword.setVisibility(View.GONE);

            }


        }
    }
}
