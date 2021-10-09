package com.kabha.myapplication.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kabha.myapplication.models.MovieModel;

// This Class for single movie request
public class MovieResponse {
    // Finding the movie object
    //@SerializedName("results") will till the Gson to start search from inside results[]
    @SerializedName("results")
    @Expose
    private MovieModel movie;

    public MovieModel getMovie(){
        return movie;
    }

    //to print the results
    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
