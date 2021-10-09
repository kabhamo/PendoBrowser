package com.kabha.myapplication.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kabha.myapplication.AppExecutors;
import com.kabha.myapplication.models.MovieModel;
import com.kabha.myapplication.response.MovieSearchResponse;
import com.kabha.myapplication.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

//now we getting the LiveData from API
public class MovieApiClient {
    private static MovieApiClient instance;

    private MutableLiveData<List<MovieModel>> mMovies; //LiveData
    //Making Global Runnable
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    //LiveData for popularMovies
    private MutableLiveData<List<MovieModel>> mMoviesPopular;
    //Making popular Runnable
    private RetrieveMoviesRunnablePopular retrieveMoviesRunnablePopular;

    public static MovieApiClient getInstance(){
        if(instance == null){
            instance = new MovieApiClient();
        }
        return instance;
    }
    //store LiveData
    private MovieApiClient(){
        mMovies = new MutableLiveData<>();
        mMoviesPopular = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){ return mMovies; }

    public LiveData<List<MovieModel>> getMoviesPopular(){ return mMoviesPopular; }

    //#1 Calling through the classes
    public void  searchMoviesApi(String query, int pageNumber){

        if(retrieveMoviesRunnable != null){
            retrieveMoviesRunnable = null;
        }
            retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            //cancelling the retrofit call
            myHandler.cancel(true);
        },2500, TimeUnit.MILLISECONDS);
    }

    public void  searchMoviesApiPopular(String category, int pageNumber){

        if(retrieveMoviesRunnablePopular != null){
            retrieveMoviesRunnablePopular = null;
        }
        retrieveMoviesRunnablePopular = new RetrieveMoviesRunnablePopular(category, pageNumber);

        final Future myHandlerPop = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePopular);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            //cancelling the retrofit call
            myHandlerPop.cancel(true);
        },6000, TimeUnit.MILLISECONDS);
    }

    //Retriving data from REstAPI by runnable class
    //We have 2 types of Queries : the ID & searchMovieQuery
    private class RetrieveMoviesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run(){
            //Getting the response objects
            try {
                Response response = getMovies(query, pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    assert response.body() != null;
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber == 1){
                        //Sendeing data to Live Data
                        //PostValue: used for background thred
                        //setValue: not for background thread
                        mMovies.postValue(list);
                    }else{
                        List<MovieModel> currentMovies = mMovies.getValue();
                        assert currentMovies != null;
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                }else{
                    assert response.errorBody() != null;
                    String error = response.errorBody().string();
                    Log.v("Tag","Error" + error);
                    mMovies.postValue(null);
                }
            }catch (IOException e){
                e.printStackTrace();
                mMovies.postValue(null);
            }
        }
        //Search Method/query
        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }
        private void CancelRequest(){
            Log.v("Tag","Cancelling Search Request");
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnablePopular implements Runnable{

        private int pageNumber;
        private String category;
        private boolean cancelRequest;

        public RetrieveMoviesRunnablePopular(String category, int pageNumber) {
            this.category = category;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run(){
            //Getting the response objects
            try {
                Response responsePopular = getMoviesPopular(category, pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(responsePopular.code() == 200){
                    assert responsePopular.body() != null;
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)responsePopular.body()).getMovies());
                    if (pageNumber == 1){
                        mMoviesPopular.postValue(list);
                    }else{
                        List<MovieModel> currentMovies = mMoviesPopular.getValue();
                        assert currentMovies != null;
                        currentMovies.addAll(list);
                        mMoviesPopular.postValue(currentMovies);
                    }
                }else{
                    assert responsePopular.errorBody() != null;
                    String error = responsePopular.errorBody().string();
                    Log.v("Tag","Error" + error);
                    mMoviesPopular.postValue(null);
                }
            }catch (IOException e){
                e.printStackTrace();
                mMoviesPopular.postValue(null);
            }
        }
        //Search Method/query
        private Call<MovieSearchResponse> getMoviesPopular(String category, int pageNumber){
            return Servicey.getMovieApi().getPopular(
                    category,
                    Credentials.API_KEY,
                    pageNumber
            );
        }
        private void CancelRequest(){
            Log.v("Tag","Cancelling Search Request");
            cancelRequest = true;
        }
    }

}
