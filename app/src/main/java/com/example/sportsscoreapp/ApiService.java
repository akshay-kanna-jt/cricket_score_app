package com.example.sportsscoreapp;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("currentMatches")
    Call<Map<String, Object>> getLiveMatches(
            @Query("apikey") String apiKey
    );

    @GET("matches")
    Call<Map<String, Object>> getAllMatches(
            @Query("apikey") String apiKey,
            @Query("offset") int offset
    );
}
