package com.plexosysconsult.hellofreshug;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class BillingDetails extends AppCompatActivity {

    TextInputLayout tilFirstName, tilSurName, tilEmail, tilPhoneNumber, tilPassword, tilReenterPassword, tilDeliveryAddress;
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
        tilDeliveryAddress = (TextInputLayout) findViewById(R.id.til_address);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        tilReenterPassword = (TextInputLayout) findViewById(R.id.til_reenter_password);
        bPlaceOrder = (Button) findViewById(R.id.b_place_order);
        cbCreateAccount = (CheckBox) findViewById(R.id.cb_create_account);


    }

}
