package com.hackslash.x_smartmart;

        import retrofit.Callback;
        import retrofit.client.Response;
        import retrofit.http.Field;
        import retrofit.http.FormUrlEncoded;
        import retrofit.http.POST;

public interface DataAPI {
    @FormUrlEncoded
    @POST("/dataquery.php")
    public void getData(
            @Field("id") String id,
            Callback<Response> callback);
}