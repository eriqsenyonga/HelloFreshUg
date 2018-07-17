package com.plexosysconsult.hellofreshug;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    /*
     * THE POINT OF THIS PAGE IS REALLY TO GET FIRST NAME, LAST NAME, EMAIL AND PHONE NUMBER
     * */


    private static final String EMAIL = "email";
    private static final String FIRSTNAME = "first_name";
    private static final String LASTNAME = "last_name";
    private static final String PUBLICPROFILE = "public_profile";
    private static final String AUTH_TYPE = "rerequest";
    private static final String LOGIN_URL = "http://www.hellofreshuganda.com/api/user/generate_auth_cookie/?insecure=cool";

    String nonce;
    private static final String NONCE_URL = "http://www.hellofreshuganda.com/api/get_nonce/?controller=user&method=register&json=get_nonce";
    // private static final String REGISTER_URL = "http://hellofreshuganda.com/api/user/register/?insecure=cool";
    private static final String REGISTER_URL = "http://www.hellofreshuganda.com/example/createCustomer.php";

    LoginButton fbLoginButton;
    TextView tvForgotPassword, tvRegister, tvContinueAsGuest;
    private CallbackManager mCallbackManager;
    SharedPreferences userSharedPrefs;
    SharedPreferences.Editor editor;
    Button bLogin;
    TextInputLayout tilEmail, tilPassword;
    int fbCustomerId = 0;

    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        bLogin = (Button) findViewById(R.id.b_login);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        tvForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvContinueAsGuest = (TextView) findViewById(R.id.tv_continue_guest);


        mCallbackManager = CallbackManager.Factory.create();

        userSharedPrefs = getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);
        editor = userSharedPrefs.edit();


        if (userSharedPrefs.getBoolean("available", false)) {

            goToMainActivity();


        }


        bLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvContinueAsGuest.setOnClickListener(this);


        // Set the initial permissions to request from the user while logging in
        fbLoginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLICPROFILE));

        fbLoginButton.setAuthType(AUTH_TYPE);

        // Register a callback to respond to the user
        fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);
                Toast.makeText(LoginActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.d("LoginResult", object.toString());
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


                                    if (customerExists(email)) {

                                        saveUserDetailsInSharedPrefs(fname, lname, email, fbCustomerId);
                                        editor.putBoolean("facebooklogin", true);
                                        editor.apply();
                                        goToMainActivity();

                                    } else {

                                        //   getNonce();

                                        //attempt to register the ninja
                                        registerNewPersonFbVersion(fname, lname, email, username);


                                    }


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
                Toast.makeText(LoginActivity.this, "CANCELLED", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private boolean customerExists(final String email) {
//when signing in using fb, check whether the customer already exists in the system and then proceed
        // if the customer exists, just proceed with the details received
        //if not, register the customer and then proceed

        final boolean[] exists = new boolean[1];
        //  exists[0] = false;


        StringRequest checkForCustomerExistanceRequest = new StringRequest(Request.Method.POST, "http://www.hellofreshuganda.com/example/getCustomerByEmail.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //    usefulFunctions.mCreateAndSaveFile(jsonFileName, response);

                        exists[0] = true;

                        try {
                            JSONObject customerObject = new JSONObject(response);


                            fbCustomerId = customerObject.getJSONObject("customer").getInt("id");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//
                        exists[0] = false;


                    }
                }) {

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", email);

                return map;
            }
        };

        checkForCustomerExistanceRequest.setRetryPolicy(new RetryPolicy() {
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


        myApplicationClass.add(checkForCustomerExistanceRequest);


        return exists[0];


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void goToMainActivity() {

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.putExtra("beginning", 1);
        startActivity(i);
        finish();
    }


    public void saveUserDetailsInSharedPrefs(String fname, String lname, String email) {

        editor.putString("fname", fname);
        editor.putString("lname", lname);
        editor.putString("email", email);
        editor.putBoolean("available", true);
        editor.apply();

    }


    public void saveUserDetailsInSharedPrefs(String fname, String lname, String email, int customerId) {

        editor.putString("fname", fname);
        editor.putString("lname", lname);
        editor.putString("email", email);
        editor.putBoolean("available", true);
        editor.putInt("customerId", customerId);
        editor.apply();

    }


    @Override
    public void onClick(View v) {
        if (v == bLogin) {
            /*
            1. First check whether the form is full with email and password
            2. Submit email and password to server
            3. Receive response from the server to know whether details are there or not
            4. If details are there, save them to SharedPrefs and move on to MainActivity
            5. If details not there, show error and do nothing
            6.
             */
            bLogin.setActivated(false);
            bLogin.setText("Please Wait...");

            if (validEntries() == true) {
                //if entries are kawa, send details to server and get the response

                validateDetailsWithServer();


            }


        }

        if (v == tvForgotPassword) {
            /*
            1. insert email
            2. send to server and see whether the account is even there
            3. tell nigga to reset via his email or some shit

             */


        }

        if (v == tvRegister) {
            /*
            1. Open new page for Sign up
            2. Send info to server to open new person and then new customer
            3. Receive response of person's details and save to sharedprefs
            4. Go on to the next level
             */

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);


        }

        if(v == tvContinueAsGuest){
            /*
            * In shared preferences save the boolean for available as false
            * Then open main activity
            *
            * */
            editor.putString("fname", "");
            editor.putString("lname", "");
            editor.putString("email", "");
            editor.putBoolean("available", false);
            editor.putInt("customerId", 0);
            editor.apply();

            goToMainActivity();

        }
    }

    private void validateDetailsWithServer() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                LOGIN_URL + "&email=" + tilEmail.getEditText().getText().toString().trim() + "&password=" + tilPassword.getEditText().getText().toString().trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));

                        Log.d("jsonresponse", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            checkTheReturnedJson(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Email not available", Toast.LENGTH_LONG).show();
                            bLogin.setActivated(true);
                            bLogin.setText("Login");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");

                //   pbLoading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(LoginActivity.this, "Connection could not be established", Toast.LENGTH_LONG).show();
                    showDialogMessage("Connection could not be established. Check your internet connection!");
                    bLogin.setActivated(true);
                    bLogin.setText("Login");

                } else if (error instanceof ParseError) {

                    Toast.makeText(LoginActivity.this, "Oops! Something went wrong. Data unreadable", Toast.LENGTH_LONG).show();
                    showDialogMessage("Oops! Something went wrong. Try again!");
                    bLogin.setActivated(true);
                    bLogin.setText("Login");

                } else {
                    Toast.makeText(LoginActivity.this, "Email not available", Toast.LENGTH_LONG).show();
                    tilEmail.getEditText().setError("Email not registered");
                    bLogin.setActivated(true);
                    bLogin.setText("Login");


                }

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

    private void checkTheReturnedJson(JSONObject jsonResponse) {

        try {
            String status = jsonResponse.getString("status");
            if (status.equalsIgnoreCase("error")) {
                //if details have a zib

                String errorDetails = jsonResponse.getString("error");

                if (errorDetails.equalsIgnoreCase("email does not exist.")) {
                    //if email does not exist
                    tilEmail.getEditText().setError("Email not registered");

                } else {

                    tilEmail.getEditText().setError("Wrong email or password");

                }

                bLogin.setActivated(true);
                bLogin.setText("Login");


            } else if (status.equalsIgnoreCase("ok")) {
                //if details are fine and the login is de
                /*
                1. Extract the details we want and then save them and move on to next step
                 */


                JSONObject userDetailsJson = jsonResponse.getJSONObject("user");

                String fname = userDetailsJson.getString("firstname");
                String lname = userDetailsJson.getString("lastname");
                String email = userDetailsJson.getString("email");
                int customerId = userDetailsJson.getInt("id");


                saveUserDetailsInSharedPrefs(fname, lname, email, customerId);
                editor.putBoolean("facebooklogin", false);
                bLogin.setActivated(true);
                bLogin.setText("Login");

                goToMainActivity();


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private boolean validEntries() {
        //method to validate that the input fields are valid eg if email field then it user should have entered a valid email

        if (tilEmail.getEditText().getText().toString().trim().isEmpty()) {

            tilEmail.getEditText().setError("Please enter your email");
            bLogin.setActivated(true);
            bLogin.setText("Login");

            return false;
        }

        if (isValidEmaillId(tilEmail.getEditText().getText().toString().trim()) == false) {
            tilEmail.getEditText().setError("Enter valid email");
            bLogin.setActivated(true);
            bLogin.setText("Login");
            return false;

        }

        if (tilPassword.getEditText().getText().toString().trim().isEmpty()) {

            tilPassword.getEditText().setError("Please enter your password");
            bLogin.setActivated(true);
            bLogin.setText("Login");
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

                            Toast.makeText(LoginActivity.this, "nonce=" + nonce, Toast.LENGTH_LONG).show();
                            // nonce = nonceValue;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");

                //   pbLoading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(LoginActivity.this, "Connection could not be established", Toast.LENGTH_LONG).show();


                } else if (error instanceof ParseError) {

                    Toast.makeText(LoginActivity.this, "Oops! Something went wrong. Data unreadable", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(LoginActivity.this, "Something went wrong.Try Again", Toast.LENGTH_LONG).show();

                }

                LoginManager.getInstance().logOut();
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
                        //  + "&nonce=" + nonce
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

                            int customerId = jsonResponse.getJSONObject("customer").getInt("id");
                            saveUserDetailsInSharedPrefs(fname, lname, email, customerId);
                            editor.putBoolean("facebooklogin", true);
                            editor.apply();
                            goToMainActivity();


                            /*
                            JSONObject jsonResponse = new JSONObject(response);

                            String status = jsonResponse.getString("status");
                            if (status.equalsIgnoreCase("ok")) {

                                int customerId = jsonResponse.getInt("user_id");

                                saveUserDetailsInSharedPrefs(fname, lname, email, customerId);
                                editor.putBoolean("facebooklogin", true);
                                editor.apply();
                                goToMainActivity();


                            } else if (status.equalsIgnoreCase("error")) {

                                String error = jsonResponse.getString("error");

                                Toast.makeText(LoginActivity.this, "User already exists", Toast.LENGTH_LONG).show();

                                //saveUserDetailsInSharedPrefs(fname, lname, email);
                                saveUserDetailsInSharedPrefs(fname, lname, email);
                                editor.putBoolean("facebooklogin", true);
                                editor.apply();
                                goToMainActivity();


                            }

                            */

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Account already exists", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");

                //   pbLoading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(LoginActivity.this, "Connection could not be established", Toast.LENGTH_LONG).show();
                    showDialogMessage("Connection could not be established. Try again!");

                } else if (error instanceof ParseError) {

                    Toast.makeText(LoginActivity.this, "Oops! Something went wrong. Data unreadable", Toast.LENGTH_LONG).show();
                    showDialogMessage("Oops! Something went wrong. Parse Error. Try again!");


                } else {

                    Toast.makeText(LoginActivity.this, "Something went wrong. Try Again", Toast.LENGTH_LONG).show();
                    showDialogMessage("Oops! Something went wrong. Random! Try again!");

                }
                LoginManager.getInstance().logOut();

                // bRegister.setActivated(true);
                //  bRegister.setText("Register");
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


    public void showDialogMessage(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
