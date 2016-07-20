package com.plexosysconsult.hellofreshug;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
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
public class ShopFruitsFragment extends Fragment {

    RecyclerView recyclerView;
    View v;
    String URL_GET_FRUITS = "http://www.hellofreshuganda.com/example/getAllFruits.php";
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    List<Item> fruitsToShow;
    SwipeRefreshLayout swipeRefreshLayout;

    public ShopFruitsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_shop_fruits, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));


        fruitsToShow = new ArrayList();

        fetchFruitsJson();
    }

    private void fetchFruitsJson() {

        StringRequest vegetableRequest = new StringRequest(Request.Method.POST, URL_GET_FRUITS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            putJsonIntoList(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("category", "Fruits");

                return map;
            }
        };


        myApplicationClass.add(vegetableRequest);

    }

    private void putJsonIntoList(JSONObject jsonResponse) {


        try {

            JSONArray fruits = jsonResponse.getJSONArray("products");

            for (int i = 0; i < fruits.length(); i++) {

                JSONObject fruitJSON = fruits.getJSONObject(i);

                Item fruit = new Item();

                fruit.setItemName(fruitJSON.getString("title"));
                fruit.setImageUrl(fruitJSON.getJSONArray("images").getJSONObject(0).getString("src"));
                fruit.setItemId(fruitJSON.getInt("id"));
                fruit.setItemPrice(fruitJSON.getString("price"));

                fruitsToShow.add(fruit);


            }


        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();


        }


        recyclerView.setAdapter(new RecyclerViewAdapterVegetable(getActivity(), fruitsToShow));


    }


}
