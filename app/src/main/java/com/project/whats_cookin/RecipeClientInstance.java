package com.project.whats_cookin;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.spoonacular.com";

    public static Call getRecipes(String queryStr, Context context) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response response = chain.proceed(chain.request());
                        Log.d("request = " , chain.request().url().toString());
                        Log.d("response = " , String.valueOf(response));
                        return response;
                    }
                }).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        String api_key = context.getString(R.string.api_key);
        RecipeService service = retrofit.create(RecipeService.class);

        Map params = new LinkedHashMap();
        params.put("query", queryStr);
        params.put("number", "100");
        params.put("apiKey", api_key);

        return service.doGetAllMatchingRecipes(params);
    }
}
