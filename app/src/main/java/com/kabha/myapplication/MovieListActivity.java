package com.kabha.myapplication;

import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Intent;
import android.util.Log;
import android.widget.Button;


import com.kabha.myapplication.adapters.MovieRecyclerView;
import com.kabha.myapplication.adapters.OnMovieListener;
import com.kabha.myapplication.models.MovieModel;
import com.kabha.myapplication.request.Servicey;
import com.kabha.myapplication.response.MovieSearchResponse;
import com.kabha.myapplication.utils.Credentials;
import com.kabha.myapplication.utils.MovieApi;
import com.kabha.myapplication.viewmodels.MovieListViewModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    //RecycleViewWidget
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;

    //ViewModel
    private MovieListViewModel movieListViewModel;

    boolean isPopular = true;

    Button Popular_btn;
    Button TopRated_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Popular_btn = (Button) findViewById(R.id.button);
        Popular_btn.setOnClickListener(v -> movieListViewModel.searchMoviePopular("popular",1));
        TopRated_btn = (Button) findViewById(R.id.top_rated);
        TopRated_btn.setOnClickListener(v -> movieListViewModel.searchMoviePopular("top_rated",1));
        Popular_btn = (Button) findViewById(R.id.popular);
        Popular_btn.setOnClickListener(v -> movieListViewModel.searchMoviePopular("popular",1));

        //Search View
        SetupSearchView();

        recyclerView = findViewById(R.id.recycleView);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        ConfigureRecyclerView();
        ObserveAnyChange();
        ObservePopularMovies();

        movieListViewModel.searchMoviePopular("popular",1);
    }

    // popular data
     private void ObservePopularMovies(){
        movieListViewModel.getPopularMovies().observe(this, movieModels -> {
            //Observing for any data change
            if(movieModels != null){
                for(MovieModel movieModel: movieModels){
                    //Get the data
                    Log.v("Tag","onChange"+movieModel.getTitle());
                    movieRecyclerAdapter.setmMovies(movieModels);
                }
            }
        });
    }

    //observing any data change
    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                //Observing for any data change
                if(movieModels != null){
                    for(MovieModel movieModel: movieModels){
                        //Get the data
                        Log.v("Tag","onChange"+movieModel.getTitle());
                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }
//
    //5# Initializing recycleView and adding data
    private void ConfigureRecyclerView(){
        movieRecyclerAdapter = new MovieRecyclerView( this);

        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Loading the next page of the api
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                //displaying the results
                if(!recyclerView.canScrollHorizontally(1)){
                    movieListViewModel.searchNextPage();
                }
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
//        Toast.makeText(this,"The Position " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MovieDetails.class);
        intent.putExtra("movie",movieRecyclerAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {}
    // Search View Method for getting data and make the api relative call
    private void SetupSearchView(){
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(query,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(v -> isPopular =false);
    }
}

