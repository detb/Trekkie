package com.github.detb.trekkie.db;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static OpenRouteServiceApi openRouteServiceApi = retrofit.create(OpenRouteServiceApi.class);

    public static OpenRouteServiceApi getOpenRouteServiceApi()
    {
        return openRouteServiceApi;
    }
}
