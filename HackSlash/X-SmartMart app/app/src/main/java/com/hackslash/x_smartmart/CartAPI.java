package com.hackslash.x_smartmart;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface CartAPI {
    @FormUrlEncoded
    @POST("/assigncart.php")
    public void sendData(
            @Field("id") String id,
            Callback<Response> callback);
}