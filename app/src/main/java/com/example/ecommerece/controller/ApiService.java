package com.example.ecommerece.controller;// ApiService.java

import com.example.ecommerece.model.ProductListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("products")
    Call<ProductListResponse> getProducts(
            @Query("limit") int limit,
            @Query("skip") int skip
    );

    @GET("products/search")
    Call<ProductListResponse> getProductsBySearch(@Query("q") String query);

    @GET("products/category/{category}")
    Call<ProductListResponse> getProductsByCategory(@Path("category") String category);
}
