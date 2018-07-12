package com.plexosysconsult.hellofreshug;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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


public class SearchableActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;

    String URL_GET_ITEMS = "http://www.hellofreshuganda.com/example/search.php";

    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    //  List<Item> itemsToShow;
    SwipeRefreshLayout swipeRefreshLayout;
    UsefulFunctions usefulFunctions;
    ProgressBar pbLoading;
    LinearLayout errorLayout;
    Button bReload;
    TextView tvErrorMsg;
    LinearLayout emptyLayout;
    String queryString;
    Toolbar toolbar;

    GridLayoutManager gridLayoutManager;
    RecyclerViewAdapterVegetable adapter;


    // Index from which pagination should start (1 is 1st page in our case)
    private static final int PAGE_START = 1;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 1;
    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        bReload = (Button) findViewById(R.id.b_reload);
        tvErrorMsg = (TextView) findViewById(R.id.tv_error_message);
        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        usefulFunctions = new UsefulFunctions(this);

        recyclerView.hasFixedSize();
        //  adapter = new RecyclerViewAdapterVegetable(this,RecyclerViewAdapterVegetable.KEY_SEARCHABLE_ACTIVITY);

        gridLayoutManager = new GridLayoutManager(this, 3);
        //  recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(gridLayoutManager);

        //  SlideInUpAnimator slideInUpAnimator = new SlideInUpAnimator();
        //  slideInUpAnimator.setAddDuration(500);

        //   recyclerView.setItemAnimator(slideInUpAnimator);

        //  recyclerView.setAdapter(adapter);
/*
        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1; //Increment page index to load the next one


                fetchItemsJson();


                //   loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
*/
        bReload.setOnClickListener(this);

        handleIntent(getIntent());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
            return true;


        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            queryString = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            //  Toast.makeText(this, "Search: " + queryString, Toast.LENGTH_LONG).show();

            getSupportActionBar().setTitle("Search: " + queryString);

            fetchItemsJson();

        }
    }

    private void fetchItemsJson() {

        StringRequest itemRequest = new StringRequest(Request.Method.POST, URL_GET_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   usefulFunctions.mCreateAndSaveFile(jsonFileName, response);

                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            recyclerView.setAdapter(new RecyclerViewAdapterVegetable(SearchableActivity.this, putJsonIntoList(jsonResponse), 2));
                            //  putJsonIntoList(jsonResponse);
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

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            tvErrorMsg.setText("Connection could not be established");


                        } else if (error instanceof ParseError) {

                            tvErrorMsg.setText("Oops! Something went wrong. Data unreadable");

                        }
                        errorLayout.setVisibility(View.VISIBLE);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("q", queryString);
                //   map.put("page", "" + currentPage);

                return map;
            }
        };

        itemRequest.setRetryPolicy(new RetryPolicy() {
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


        myApplicationClass.add(itemRequest);

    }

    private List<Item> putJsonIntoList(JSONObject jsonResponse) {

        List<Item> itemsToShow = new ArrayList<>();

        try {

//            TOTAL_PAGES = Integer.parseInt(jsonResponse.getString("num_pages").trim());

            JSONArray fruits = jsonResponse.getJSONArray("products");

            if (fruits.length() == 0) {
                emptyLayout.setVisibility(View.VISIBLE);
            } else {
                emptyLayout.setVisibility(View.GONE);


                for (int i = 0; i < fruits.length(); i++) {

                    JSONObject fruitJSON = fruits.getJSONObject(i);

                    Item fruit = new Item();

                    fruit.setItemName(fruitJSON.getString("title"));
                    fruit.setImageUrl(fruitJSON.getJSONArray("images").getJSONObject(0).getString("src"));
                    fruit.setItemId(fruitJSON.getInt("id"));
                    fruit.setItemPrice(fruitJSON.getString("price"));

                    fruit.setItemShortDescription(usefulFunctions.stripHtml(fruitJSON.getString("short_description")));

                    JSONArray variationArray = fruitJSON.getJSONArray("variations");

                    if (variationArray.length() > 0) {

                        fruit.setHasVariations(true);

                        List<Item> variations = new ArrayList<>();

                        for (int j = 0; j < variationArray.length(); j++) {

                            JSONObject variationJSONObject = variationArray.getJSONObject(j);

                            Item variationFruit = new Item();
                            variationFruit.setItemId(variationJSONObject.getInt("id"));
                            variationFruit.setItemPrice(variationJSONObject.getString("price"));
                            variationFruit.setOptionUnit(variationJSONObject.getJSONArray("attributes").getJSONObject(0).getString("option"));

                            variations.add(variationFruit);

                        }

                        fruit.setItemVariations(variations);


                    } else {

                        fruit.setHasVariations(false);
                    }

                    itemsToShow.add(fruit);

                }
            }

        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();

        }

        return itemsToShow;
        // recyclerView.setAdapter(new RecyclerViewAdapterVegetable(this, itemsToShow));


    }
/*
    private void loadPageOne(List<Item> itemsToShow) {

        adapter.addAll(itemsToShow);

        if(adapter.getItemCount() > 0){
            emptyLayout.setVisibility(View.GONE);
        }else{
            emptyLayout.setVisibility(View.VISIBLE);
        }

        if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;


    }

    private void loadSubsequentPages(List<Item> itemsToShow) {
        // List<Movie> movies = Movie.createMovies(adapter.getItemCount());  // 1

        adapter.removeLoadingFooter();  // 2
        isLoading = false;   // 3

        adapter.addAll(itemsToShow);   // 4

        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();  // 5
        else isLastPage = true;
    }
    */

    @Override
    public void onClick(View v) {
        if (v == bReload) {

            errorLayout.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
            fetchItemsJson();
        }
    }


}
