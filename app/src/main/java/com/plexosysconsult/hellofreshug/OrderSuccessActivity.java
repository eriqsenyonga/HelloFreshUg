package com.plexosysconsult.hellofreshug;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderSuccessActivity extends AppCompatActivity {

    Button bContinueShopping;
    SharedPreferences userSharedPrefs;
    SharedPreferences.Editor sharedPrefsEditor;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    String URL_SEND_FEEDBACK = "http://www.gari-share.com/example/sendCustomerFeedback.php";
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        userEmail = getIntent().getStringExtra("email"); //this is used for the feedback

        userSharedPrefs = getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);
        sharedPrefsEditor = userSharedPrefs.edit();

        bContinueShopping = (Button) findViewById(R.id.b_continue_shopping);

        bContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrderSuccessActivity.this, MainActivity.class);
                i.putExtra("beginning", 1);
                startActivity(i);
                finish();
            }
        });

        myApplicationClass.getCart().emptyCart();

        if (userSharedPrefs.getBoolean("rated", false)) {
            //if the user rated this app then do nothing

        } else {

            showDoYouLoveDialog();


        }


    }

    private void showDoYouLoveDialog() {
        //Dialog asking if you love the app

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderSuccessActivity.this);

        builder.setMessage("Do You Love HelloFresh?");

        builder.setPositiveButton("I love it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //if you love hellofresh, show dialog for rate us

                showRateUsDialog();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //if you dont love hellofresh, shoow dialog for feedback comment
                showFeedbackDialog();
                dialog.dismiss();

            }
        });

        builder.create().show();


    }

    private void showFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderSuccessActivity.this);

        LayoutInflater inflater = LayoutInflater.from(OrderSuccessActivity.this);
        View v = inflater.inflate(R.layout.dialog_feedback, null);
        final EditText etComment = v.findViewById(R.id.et_comment);

        builder.setView(v);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String comment = etComment.getText().toString();

                Toast.makeText(OrderSuccessActivity.this, comment, Toast.LENGTH_LONG).show();

                sendFeedbackToCompany(comment);

            }
        });

        builder.create().show();

    }

    private void sendFeedbackToCompany(final String comment) {

        Intent i = new Intent(Intent.ACTION_SENDTO);
       // i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:" + "care@hellofreshuganda.com"));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"care@hellofreshuganda.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "HelloFresh Customer Feedback");
        i.putExtra(Intent.EXTRA_TEXT, comment);
        try {
            startActivity(Intent.createChooser(i, "Send feedback..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(OrderSuccessActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();

            //if there is no email client, then send with volley

            StringRequest feedbackRequest = new StringRequest(Request.Method.POST, URL_SEND_FEEDBACK,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            showThanksForYourFeedbackDialog();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(OrderSuccessActivity.this, "An Error Occurred", Toast.LENGTH_LONG).show();

                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("comment", comment);
                    map.put("email", userEmail);

                    return map;
                }
            };

            feedbackRequest.setRetryPolicy(new RetryPolicy() {
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


            myApplicationClass.add(feedbackRequest);


        }

    }

    private void showThanksForYourFeedbackDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderSuccessActivity.this);

        builder.setMessage("Thanks for your feedback!");

        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void showRateUsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderSuccessActivity.this);

        builder.setMessage("Awesome! It would mean the world to us if you gave us 5 stars.\n\nThanks for your support!");

        builder.setPositiveButton("Rate 5 stars", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //if you love hellofresh, show dialog for rate us

                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarketIntent = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.


                int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
                } else {
                    flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
                }


                goToMarketIntent.addFlags(flags);
                try {
                    startActivity(goToMarketIntent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }


                 sharedPrefsEditor.putBoolean("rated", true).apply();

            }
        });


        builder.create().show();

    }
}
