package com.kabha.myapplication.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kabha.myapplication.models.MovieModel;

import java.util.List;

//This Class for getting multiple movies list/ popular movies
public class MovieSearchResponse {
    //The total_count will contain the number of movies in the json call
    //Start reading from 'total_results' entry
    @SerializedName("total_results")
    @Expose()
    private int total_count;

    //The List here will contain the model objects/the movies
    @SerializedName("results")
    @Expose()
    private List<MovieModel> movies;

    public int getTotal_count(){
        return total_count;
    }

    public List<MovieModel> getMovies(){
        return movies;
    }

    //to print the results
    @NonNull
    @Override
    public String toString() {
        return "MovieSearchResponse{" +
                "total_count=" + total_count +
                ", movies=" + movies +
                '}';
    }
}
