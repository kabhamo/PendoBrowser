package com.kabha.myapplication.utils;

import com.kabha.myapplication.models.MovieModel;
import com.kabha.myapplication.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    //Search for movie
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );

//    /3/movie/popular PopularMovies
    @GET("/3/movie/{category}")
    Call<MovieSearchResponse> getPopular(
            @Path("category") String category,
            @Query("api_key") String key ,
            @Query("page") int page
    );

    // Making Search with ID
    // https://api.themoviedb.org/3/movie/550?api_key=8039618a6c1dba3133ec5ee188374b77
    @GET("/3/movie/{movie_id}?")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );



}
// Making Search with ID https://api.themoviedb.org/3/movie/550?api_key=8039618a6c1dba3133ec5ee188374b77