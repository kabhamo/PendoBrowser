package com.kabha.myapplication.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kabha.myapplication.R;


public class PopularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageView;
    RatingBar ratingBar;
    //Click Listener
    OnMovieListener onMovieListener;

    public PopularViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);
        this.onMovieListener = onMovieListener;
        imageView = itemView.findViewById(R.id.movie_img_popular);
        ratingBar = itemView.findViewById(R.id.rating_bar_popular);
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
