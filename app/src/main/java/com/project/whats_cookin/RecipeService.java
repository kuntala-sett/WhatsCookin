package com.project.whats_cookin;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RecipeService {

    @GET("/recipes/complexSearch")
    Call<RecipeResponse> doGetAllMatchingRecipes(@QueryMap Map<String,String> parameters);;

//    @FormUrlEncoded
//    @POST("/recipes/complexSearch?")
//    Call<RecipeResponse> doGetAllMatchingRecipes(@FieldMap Map<String,String> parameters);
}
