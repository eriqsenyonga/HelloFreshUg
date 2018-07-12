package com.plexosysconsult.hellofreshug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EMAIL = "email";
    private static final String FIRSTNAME = "first_name";
    private static final String LASTNAME = "last_name";
    private static final String PUBLICPROFILE = "public_profile";
    private static final String AUTH_TYPE = "rerequest";


    TextInputLayout tilFirstName, tilLastName, tilEmail, tilPassword;
    String nonce;

    Button bRegister;
    LoginButton fbLoginButton;
    private CallbackManager mCallbackManager;

    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();

    private static final String NONCE_URL = "http://www.hellofreshuganda.com/api/get_nonce/?controller=user&method=register";
    private static final String REGISTER_URL = "http://hellofreshuganda.com/api/user/register/?insecure=cool";

    SharedPreferences userSharedPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tilFirstName = (TextInputLayout) findViewById(R.id.til_firstname);
        tilLastName = (TextInputLayout) findViewById(R.id.til_lastname);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        bRegister = (Button) findViewById(R.id.b_register);
        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        userSharedPrefs = getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);
        editor = userSharedPrefs.edit();

        mCallbackManager = CallbackManager.Factory.create();

        // Set the initial permissions to request from the user while logging in
        fbLoginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLICPROFILE));

        fbLoginButton.setAuthType(AUTH_TYPE);

        // Register a callback to respond to the user
        fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);
                Toast.makeText(RegisterActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                               // Log.d("LoginResult", object.toString());
                                // Application code

                              /*
                                Format of json received
                                {
                                    "id": "12345678",
                                        "birthday": "1/1/1950",
                                        "first_name": "Chris",
                                        "gender": "male",
                                        "last_name": "Colm",
                                        "link": "http://www.facebook.com/12345678",
                                        "location": {
                                    "id": "110843418940484",
                                            "name": "Seattle, Washington"
                                },
                                    "locale": "en_US",
                                        "name": "Chris Colm",
                                        "timezone": -8,
                                        "updated_time": "2010-01-01T16:40:43+0000",
                                        "verified": true
                                }

                                */


                                try {
                                    String fname = object.getString(FIRSTNAME);
                                    String lname = object.getString(LASTNAME);
                                    String email = object.getString(EMAIL);
                                    String username = email;
                                    getNonce();

                                    //attempt to register the ninja
                                    registerNewPersonFbVersion(fname, lname, email, username);



                                  //  saveUserDetailsInSharedPrefs(fname, lname, email);


                                //    goToMainActivity();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                Toast.makeText(RegisterActivity.this, "CANCELLED", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        bRegister.setOnClickListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public String getUsernameFromEmail(String email) {
        //     String filename = "abc.def.ghi";     // full file name
        int iend = email.indexOf("@"); //this finds the first occurrence of "@"
//in string thus giving you the index of where it is in the string

// Now iend can be -1, if lets say the string had no "." at all in it i.e. no "." is found.
//So check and account for it.

        String subString = "";
        if (iend != -1) {
            subString = email.substring(0, iend); //this will give abc

        }

        return subString;
    }

    @Override
    public void onClick(View v) {
        if (v == bRegister) {
            if (validEntries()) {

                bRegister.setActivated(false);
                bRegister.setText("Please Wait...");

                String fname = tilFirstName.getEditText().getText().toString().trim();
                String lname = tilLastName.getEditText().getText().toString().trim();
                String email = tilEmail.getEditText().getText().toString().trim();
                String password = tilPassword.getEditText().getText().toString().trim();
                String username = email;

                //get nonce
                getNonce();

                //attempt to register the ninja
                registerNewPerson(fname, lname, email, password, username);


            }


        }
    }

    private void registerNewPerson(final String fname, final String lname, final String email, String password, String username) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                REGISTER_URL
                        + "&nonce=" + nonce
                        + "&username=" + username
                        + "&display_name=" + fname
                        + "&first_name=" + fname
                        + "&last_name=" + lname
                        + "&user_pass=" + password
                        + "&email=" + email,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));

                        //Log.d("jsonresponse", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            String status = jsonResponse.getString("status");
                            if (status.equalsIgnoreCase("ok")) {

                                saveUserDetailsInSharedPrefs(fname, lname, email);
                                goToMainActivity();


                            } else if (status.equalsIgnoreCase("error")) {

                                String error = jsonResponse.getString("error");

                                Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_LONG).show();

                                //saveUserDetailsInSharedPrefs(fname, lname, email);
                                goToLoginActivity();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                            bRegister.setActivated(true);
                            bRegister.setText("Register");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");

                //   pbLoading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(RegisterActivity.this, "Connection could not be established", Toast.LENGTH_LONG).show();


                } else if (error instanceof ParseError) {

                    Toast.makeText(RegisterActivity.this, "Oops! Something went wrong. Data unreadable", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(RegisterActivity.this, "Something went wrong. Try Again", Toast.LENGTH_LONG).show();

                }

                bRegister.setActivated(true);
                bRegister.setText("Register");
                //  errorLayout.setVisibility(View.VISIBLE);
            }
        }) {


            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        myApplicationClass.add(stringRequest);


    }


    private void registerNewPersonFbVersion(final String fname, final String lname, final String email, String username) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                REGISTER_URL
                        + "&nonce=" + nonce
                        + "&username=" + username
                        + "&display_name=" + fname
                        + "&first_name=" + fname
                        + "&last_name=" + lname
                        + "&email=" + email,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));

                        //Log.d("jsonresponse", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            String status = jsonResponse.getString("status");
                            if (status.equalsIgnoreCase("ok")) {

                                saveUserDetailsInSharedPrefs(fname, lname, email);
                                editor.putBoolean("facebooklogin", true);
                                goToMainActivity();


                            } else if (status.equalsIgnoreCase("error")) {

                                String error = jsonResponse.getString("error");

                                Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_LONG).show();

                                //saveUserDetailsInSharedPrefs(fname, lname, email);
                                saveUserDetailsInSharedPrefs(fname, lname, email);
                                editor.putBoolean("facebooklogin", true);
                                editor.apply();
                                goToMainActivity();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                            bRegister.setActivated(true);
                            bRegister.setText("Register");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");

                //   pbLoading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(RegisterActivity.this, "Connection could not be established", Toast.LENGTH_LONG).show();


                } else if (error instanceof ParseError) {

                    Toast.makeText(RegisterActivity.this, "Oops! Something went wrong. Data unreadable", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(RegisterActivity.this, "Something went wrong. Try Again", Toast.LENGTH_LONG).show();

                }

                bRegister.setActivated(true);
                bRegister.setText("Register");
                //  errorLayout.setVisibility(View.VISIBLE);
            }
        }) {


            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        myApplicationClass.add(stringRequest);


    }








    private void goToLoginActivity() {

        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();

    }

    private void getNonce() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                NONCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));

                        //Log.d("jsonresponse", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            nonce = jsonResponse.getString("nonce");

                            Toast.makeText(RegisterActivity.this, "nonce=" + nonce + " !", Toast.LENGTH_LONG).show();
                            // nonce = nonceValue;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                            bRegister.setActivated(true);
                            bRegister.setText("Register");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");

                //   pbLoading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(RegisterActivity.this, "Connection could not be established", Toast.LENGTH_LONG).show();


                } else if (error instanceof ParseError) {

                    Toast.makeText(RegisterActivity.this, "Oops! Something went wrong. Data unreadable", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                }

                bRegister.setActivated(true);
                bRegister.setText("Register");
                //  errorLayout.setVisibility(View.VISIBLE);
            }
        }) {


            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        myApplicationClass.add(stringRequest);


    }


    private boolean validEntries() {
        //method to validate that the input fields are valid eg if email field then it user should have entered a valid email

        if (tilFirstName.getEditText().getText().toString().trim().isEmpty()) {

            tilFirstName.getEditText().setError("Please enter your first name");


            return false;
        }

        if (tilLastName.getEditText().getText().toString().trim().isEmpty()) {

            tilLastName.getEditText().setError("Please enter your surname");


            return false;
        }


        if (tilEmail.getEditText().getText().toString().trim().isEmpty()) {

            tilEmail.getEditText().setError("Please enter your email");


            return false;
        }

        if (isValidEmaillId(tilEmail.getEditText().getText().toString().trim()) == false) {
            tilEmail.getEditText().setError("Enter valid email");
            return false;

        }

        if (tilPassword.getEditText().getText().toString().trim().isEmpty()) {

            tilPassword.getEditText().setError("Please enter your password");

            return false;
        }


        return true;
    }

    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void saveUserDetailsInSharedPrefs(String fname, String lname, String email) {

        editor.putString("fname", fname);
        editor.putString("lname", lname);
        editor.putString("email", email);
        editor.putBoolean("available", true);
        editor.apply();

    }

    private void goToMainActivity() {

        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        i.putExtra("beginning", 1);
        startActivity(i);
        finish();
    }
}
