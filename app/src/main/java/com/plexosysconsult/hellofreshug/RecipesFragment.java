package com.plexosysconsult.hellofreshug;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends Fragment {


    RecyclerView rvRecipes;
    String URL_GET_RECIPES = "http://www.hellofreshuganda.com/api/get_posts/?post_type=recipe";
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    List<Recipe> recipeList;
    ProgressBar pbLoading;
    View v;


    public RecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_recipes, container, false);

        rvRecipes = (RecyclerView) v.findViewById(R.id.recycler_view);

        pbLoading = (ProgressBar) v.findViewById(R.id.pb_loading);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        rvRecipes.hasFixedSize();
        rvRecipes.setLayoutManager(new LinearLayoutManager(getActivity()));


        recipeList = new ArrayList();


fetchFruitsJson();
    }

    private void fetchFruitsJson() {

        StringRequest recipeRequest = new StringRequest(Request.Method.POST, URL_GET_RECIPES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d("recipes", response);
                            JSONObject jsonResponse = new JSONObject(response);
                           // putJsonIntoList(jsonResponse);
                            pbLoading.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        pbLoading.setVisibility(View.GONE);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("category", "recipe");

                return map;
            }
        };


        myApplicationClass.add(recipeRequest);

    }
}
