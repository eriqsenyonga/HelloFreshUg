package com.plexosysconsult.hellofreshug;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by senyer on 10/6/2016.
 */
public class RecyclerViewAdapterRecipe extends RecyclerView.Adapter<RecyclerViewAdapterRecipe.ViewHolder> {

    Context context;
    List<Recipe> recipeList;

    public RecyclerViewAdapterRecipe(Context c, List<Recipe> recipes) {

        this.context = c;

        this.recipeList = recipes;

    }


    @Override
    public RecyclerViewAdapterRecipe.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerViewAdapterRecipe.ViewHolder holder, int position) {


        final Recipe recipe = (Recipe) this.recipeList.get(position);

        holder.tvRecipeTitle.setText(recipe.getTitle());
        //   holder.tvItemPrice.setText(bigDecimalClass.convertStringToDisplayCurrencyString(veggie.getItemPrice()));

        //  final int selectedVariation = 0;

        Glide
                .with(context)
                .load(recipe.getImageUrl())
                .apply(new RequestOptions().centerCrop())
                .transition(new DrawableTransitionOptions().crossFade())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.loading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.loading.setVisibility(View.GONE);
                        return false;
                    }


                })
                .into(holder.ivRecipeImage);


        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {


                Bundle recipeBundle = new Bundle();
                recipeBundle.putString("title", recipe.getTitle());
                recipeBundle.putString("body", recipe.getBody());
                recipeBundle.putString("imageUrl", recipe.getImageUrl());

                Intent i = new Intent(context, RecipeDetails.class);
                i.putExtra("recipe_details", recipeBundle);
                context.startActivity(i);


            }
        });


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvRecipeTitle;
        ImageView ivRecipeImage;
        ItemClickListener itemClickListener;
        ProgressBar loading;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRecipeTitle = (TextView) itemView.findViewById(R.id.tv_recipe_title);

            ivRecipeImage = (ImageView) itemView.findViewById(R.id.iv_recipe_image);
            loading = (ProgressBar) itemView.findViewById(R.id.pb_loading);

            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
