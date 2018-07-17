package com.plexosysconsult.hellofreshug;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {


    private static final String EMAIL = "email";
    private static final String PUBLICPROFILE = "public_profile";


    private static final String AUTH_TYPE = "rerequest";

    View root;
    TextView tvName, tvEmail, tvPhone;
    Button bLogOut;
    SharedPreferences userSharedPrefs;
    CallbackManager mCallbackManager;
    LoginButton fbLoginButton;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_settings, container, false);
        fbLoginButton = (LoginButton) root.findViewById(R.id.fb_login_button);
        tvName = (TextView) root.findViewById(R.id.tv_name);
        tvEmail = (TextView) root.findViewById(R.id.tv_email);
        tvPhone = (TextView) root.findViewById(R.id.tv_phone);
        bLogOut = (Button) root.findViewById(R.id.b_logout);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        userSharedPrefs = getActivity().getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);


        if (userSharedPrefs.getBoolean("available", false)){

            bLogOut.setText("LOGIN");
            bLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                }
            });


        }



        mCallbackManager = CallbackManager.Factory.create();

        // Set the initial permissions to request from the user while logging in
        fbLoginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLICPROFILE));

        fbLoginButton.setAuthType(AUTH_TYPE);

        fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        if (userSharedPrefs.getBoolean("facebooklogin", false)) {
            //if the user logged in with facebook, initiate log out from facebook

            fbLoginButton.setVisibility(View.GONE);
            bLogOut.setVisibility(View.VISIBLE);

            fbLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor;

                    editor = userSharedPrefs.edit();
                    editor.putBoolean("available", false);
                    editor.apply();


                    //logOut();

                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                }
            });


        } else {
            fbLoginButton.setVisibility(View.GONE);
            bLogOut.setVisibility(View.VISIBLE);
        }


        // editor = userSharedPrefs.edit();

        String fname = userSharedPrefs.getString("fname", "");
        String lname = userSharedPrefs.getString("lname", "");
        String email = userSharedPrefs.getString("email", "");


        tvName.setText(fname + " " + lname);
        tvEmail.setText(email);


        bLogOut.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        if (v == bLogOut) {


            //Do some logout stuff here and then go to Login Screen
            /*
            1. make available false and go back to login screen
             */

            if (userSharedPrefs.getBoolean("facebooklogin", false)) {
                LoginManager.getInstance().logOut();
            }

            SharedPreferences.Editor editor;

            editor = userSharedPrefs.edit();
            editor.putBoolean("available", false);
            editor.apply();

            //logOut();

            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
            getActivity().finish();


        }
    }
}
