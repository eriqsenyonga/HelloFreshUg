package com.plexosysconsult.hellofreshug;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    Toolbar toolbar;
    FragmentManager fm;
    public FloatingActionButton fab;
    View navigationHeader;
    TextView tvClientName;
    TextView tvClientEmail;
    DrawerLayout drawer;
    ImageView redDot;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    SharedPreferences mPositionSavedPrefs;
    SharedPreferences.Editor posSavedEditor;
    ImageView ivClientProfilePic;
    SharedPreferences userSharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationHeader = navigationView.getHeaderView(0);
        ivClientProfilePic = (ImageView) navigationHeader.findViewById(R.id.iv_client_profile_pic);
        tvClientName = (TextView) navigationHeader.findViewById(R.id.tv_display_name);
        tvClientEmail = (TextView) navigationHeader.findViewById(R.id.tv_email);
        printhashkey();

        userSharedPrefs = getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);
        // editor = userSharedPrefs.edit();

        if (userSharedPrefs.getBoolean("available", false)) {

            String fname = userSharedPrefs.getString("fname", "");
            String lname = userSharedPrefs.getString("lname", "");
            String email = userSharedPrefs.getString("email", "");


            tvClientName.setText("Welcome " + fname);
            tvClientEmail.setText(email);
        } else {

            tvClientName.setText("Welcome Guest");
            tvClientEmail.setText("hellofreshug@gmail.com");

        }

        fm = getSupportFragmentManager();

        mPositionSavedPrefs = getSharedPreferences("mPositionSaved",
                Context.MODE_PRIVATE);
        posSavedEditor = mPositionSavedPrefs.edit();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (getIntent().hasExtra("beginning")) {

            fm.beginTransaction().replace(R.id.contentMain, new ShopFragment()).commit();
            drawer.openDrawer(GravityCompat.START);
            posSavedEditor.putInt("last_main_position", R.id.nav_shop).apply();
            getSupportActionBar().setTitle("Shop");

        } else {

            Fragment fragment = null;
            CharSequence title = null;

            int id = mPositionSavedPrefs.getInt(
                    "last_main_position", 1);


            if (id == R.id.nav_shop) {
                fragment = new ShopFragment();
                title = "Shop";
            }
/*
            if (id == R.id.nav_my_account) {
                fragment = new MyAccountFragment();
                title = "My Account";

            }

            if (id == R.id.nav_orders) {
                fragment = new OrdersFragment();
                title = "Orders";
            }

            if (id == R.id.nav_recipes) {
                fragment = new RecipesFragment();
                title = "Recipes";
            }
*/
            if (fragment != null) {

                fm.beginTransaction().replace(R.id.contentMain, fragment).commit();
                getSupportActionBar().setTitle(title);

                drawer.closeDrawer(GravityCompat.START);

            }
        }

        //checkCartForItems();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        final View notifications = menu.findItem(R.id.action_cart).getActionView();

        redDot = (ImageView) notifications.findViewById(R.id.iv_red_notification);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            openCart();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Fragment fragment = null;


        if (id == R.id.nav_shop) {
            fragment = new ShopFragment();
        } else if (id == R.id.nav_cart) {

            fragment = new CartFragment();

        }
/*
        if (id == R.id.nav_my_account) {
            fragment = new MyAccountFragment();
        }
*/
        if (id == R.id.nav_orders) {
            fragment = new OrdersFragment();
        }
/*
        if (id == R.id.nav_recipes) {
            fragment = new RecipesFragment();
        }
*/
        if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        }


        if (id == R.id.nav_about) {

            fragment = new AboutUsFragment();
        }

        if (fragment != null) {

            fm.beginTransaction().replace(R.id.contentMain, fragment).commit();
            getSupportActionBar().setTitle(item.getTitle());
            posSavedEditor.putInt("last_main_position", id).apply();
            drawer.closeDrawer(GravityCompat.START);
            item.setChecked(true);
        }


        return true;
    }

    public void checkCartForItems() {

        if (!myApplicationClass.getCart().getCurrentCartItems().isEmpty()) {
            redDot.setVisibility(View.VISIBLE);
        } else {
            redDot.setVisibility(View.GONE);
        }


    }

    public void showShopFragment() {

        fm.beginTransaction().replace(R.id.contentMain, new ShopFragment()).commit();
        getSupportActionBar().setTitle(R.string.shop);
        getSupportActionBar().setSubtitle("");
        setNavigationViewCheckedItem(R.id.nav_shop);


    }

    public void OpenCart(View v) {

        //  Toast.makeText(this, "Open cart on click", Toast.LENGTH_LONG).show();

        if (myApplicationClass.getCart().getCurrentCartItems().isEmpty()) {

            Toast.makeText(this, "Cart is empty", Toast.LENGTH_LONG).show();


        } else {

            openCart();
        }

    }


    public void openCart() {

        fm.beginTransaction().replace(R.id.contentMain, new CartFragment()).addToBackStack(null).commit();
        setActionBarTitleAndSubtitle("Cart", "");
        setNavigationViewCheckedItem(R.id.nav_cart);


    }

    public void setActionBarTitleAndSubtitle(String title, String subTitle) {

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subTitle);
    }

    public void setNavigationViewCheckedItem(int itemId) {


        navigationView.setCheckedItem(itemId);
        posSavedEditor.putInt("last_main_position", itemId).apply();

    }

    public void printhashkey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.plexosysconsult.hellofreshug",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
}
