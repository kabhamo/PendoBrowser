package com.kabha.myapplication.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kabha.myapplication.models.MovieModel;
import com.kabha.myapplication.request.MovieApiClient;

import java.util.List;

public class MovieRepository {
    //this Class acting as repo
    // singleton class
    // this class will handle calls to TMDB api
    //we are sending the ViewModel's Data to the repo

    private static MovieRepository instance;
    private MovieApiClient movieApiClient;
    private String mQuery;
    private int mPageNumber;

    public static MovieRepository getInstance(){
        if (instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }
    private MovieRepository(){
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){return movieApiClient.getMovies();}

    public LiveData<List<MovieModel>> getMoviesPopular(){ return movieApiClient.getMoviesPopular(); }

    //2# Calling the method
    //SerachMovie
    public void searchMovieApi(String query, int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApi(query,pageNumber);
    }

    //Popular
    public void searchMoviesApiPopular(String category, int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApiPopular(category, pageNumber);
    }

    public void searchNextPage(){
        searchMovieApi(mQuery, mPageNumber+1);
    }

}
//searchMoviesApiTopRated


