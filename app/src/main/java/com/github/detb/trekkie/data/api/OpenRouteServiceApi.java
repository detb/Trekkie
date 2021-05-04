package com.github.detb.trekkie.data.api;


import com.github.detb.trekkie.data.remote.Root;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenRouteServiceApi {
    @GET("v2/directions/foot-hiking?api_key=5b3ce3597851110001cf6248b6552a0ab51f469f874a2253c22d11c2&start=8.681495,49.41461&end=8.687872,49.420318")
    Call<Root> getData();

    @Headers({"Authorization: 5b3ce3597851110001cf6248b6552a0ab51f469f874a2253c22d11c2", "Content-Type: application/json"})
    @POST("v2/directions/foot-hiking/geojson")
    Call<Root> getHikeData(@Body RequestBody body);
}
