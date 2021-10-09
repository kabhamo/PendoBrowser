package com.kabha.myapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kabha.myapplication.models.MovieModel;
import com.kabha.myapplication.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    // this Class is used for view model

    private MovieRepository movieRepository;

    //constructor
    public MovieListViewModel(){
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }

    public LiveData<List<MovieModel>> getPopularMovies(){ return movieRepository.getMoviesPopular(); }


    //3# Calling method in view-model
    public void searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovieApi(query,pageNumber);
    }

    public void searchMoviePopular(String category, int pageNumber){
        movieRepository.searchMoviesApiPopular(category, pageNumber);
    }

    public void searchNextPage(){
        movieRepository.searchNextPage();
    }

}
