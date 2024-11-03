package com.example.mealcompass.Home;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/user_id")
    Call<UserId> sendUserId(@Body UserId userId);

    @POST("/recommend")
    Call<List<RestaurantRecommendationResponse>> getRecommendations(@Body UserId userId);

}
