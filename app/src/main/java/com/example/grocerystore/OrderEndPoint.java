package com.example.grocerystore;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
//For sending a fake order to the web server
public interface OrderEndPoint {
    @POST("posts")
    Call<Order> newOrder(@Body Order order);

}
